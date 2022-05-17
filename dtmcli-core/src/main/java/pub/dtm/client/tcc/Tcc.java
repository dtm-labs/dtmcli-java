/*
 * MIT License
 *
 * Copyright (c) 2022 yedf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pub.dtm.client.tcc;

import lombok.NoArgsConstructor;
import pub.dtm.client.base.BranchIdGenerator;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.FailureException;
import pub.dtm.client.interfaces.communication.IDtmCommunicationClient;
import pub.dtm.client.interfaces.dtm.DtmConsumer;
import pub.dtm.client.model.dtm.TransBase;
import pub.dtm.client.model.feign.ServiceMessage;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.model.param.OperatorParam;
import pub.dtm.client.model.param.TccOperatorParam;
import pub.dtm.client.model.responses.DtmResponse;
import pub.dtm.client.utils.FeignUtils;
import pub.dtm.client.utils.HttpUtils;
import pub.dtm.client.utils.JsonUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Tcc trans type service
 *
 * @author horseLk
 */
@NoArgsConstructor
public class Tcc extends TransBase {
    private static final Logger log = LoggerFactory.getLogger(Tcc.class);

    private static final String OP = "try";

    /**
     * branch id generator
     */
    private BranchIdGenerator branchIdGenerator;

    /**
     * feign client
     */
    private IDtmCommunicationClient dtmCommunicationClient;

    public Tcc(String gid, IDtmCommunicationClient dtmCommunicationClient) {
        super(gid, TransTypeEnum.TCC, false);
        this.branchIdGenerator = new BranchIdGenerator(Constants.EMPTY_STRING);
        this.dtmCommunicationClient = dtmCommunicationClient;
    }

    public void setdtmCommunicationClient(IDtmCommunicationClient dtmCommunicationClient) {
        this.dtmCommunicationClient = dtmCommunicationClient;
    }

    /**
     * start a tcc distribute transaction
     * @param consumer consumer
     * @return gid
     * @throws Exception exception
     */
    public String tccGlobalTransaction(DtmConsumer<Tcc> consumer) throws Exception {
        // if tcc's gid is empty, need to request a new gid from dtm svr
        if (StringUtils.isEmpty(this.getGid())) {
            this.setGid(FeignUtils.parseGid(dtmCommunicationClient.newGid()));
        }
        log.info("the tcc transaction's gid is {}", this.getGid());

        OperatorParam operatorParam = new OperatorParam(this.getGid(), TransTypeEnum.TCC);
        DtmResponse resp = dtmCommunicationClient.prepare(operatorParam);
        log.info("prepare response: {}", resp);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction prepare fail. returned dtm_result is: {}, transaction gid: {}", resp.getDtmResult(), this.getGid());
            throw new FailureException("TCC Transaction prepare fail");
        }
        try {
            consumer.accept(this);
            dtmCommunicationClient.submit(operatorParam);
        } catch (Exception e) {
            log.error("TCC transaction submit fail, start abort it. transaction gid: {}", this.getGid());
            dtmCommunicationClient.abort(operatorParam);
            throw new FailureException(e);
        }
        return this.getGid();
    }

    /**
     * busi can call method callBranch() to set tcc transaction's branch.
     * the callBranch for micro service driver
     * @param body busi body
     * @param tryMessage service message of try operator
     * @param confirmMessage service message of confirm operator
     * @param cancelMessage service message of cancel operator
     * @return feign response
     * @throws Exception exception
     */
    public feign.Response callBranch(Object body, ServiceMessage tryMessage, ServiceMessage confirmMessage, ServiceMessage cancelMessage) throws Exception {
        log.info("call method Tcc.callBranch, tryMessage: {}, confirmMessage: {}, cancelMessage: {}",
                JsonUtils.toJson(tryMessage), JsonUtils.toJson(confirmMessage), JsonUtils.toJson(cancelMessage));
        String branchId = this.branchIdGenerator.genBranchId();

        TccOperatorParam operatorParam = new TccOperatorParam(this.getGid(), TransTypeEnum.TCC, branchId, Constants.DEFAULT_STATUS,
                JsonUtils.toJson(body), FeignUtils.generatorURI(confirmMessage, false), FeignUtils.generatorURI(cancelMessage, false));
        DtmResponse resp = dtmCommunicationClient.registerBranch(operatorParam);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction register branch fail. transaction gid: {}",  this.getGid());
            throw new FailureException("TCC Transaction register branch fail");
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(ParamFieldConstants.GID, this.getGid());
        paramsMap.put(ParamFieldConstants.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        paramsMap.put(ParamFieldConstants.BRANCH_ID, branchId);
        paramsMap.put(ParamFieldConstants.OP, OP);

        feign.Response response = dtmCommunicationClient.busiPost(new URI(FeignUtils.generatorURI(tryMessage, true)),
                tryMessage.getPath(), paramsMap, body);
        log.info("busi post is: {}", response);
        FeignUtils.checkResult(response);
        return response;
    }

    /**
     * busi can call method callBranch() to set tcc transaction's branch.
     * the callBranch for http driver
     * @param body busi body
     * @param tryUrl  http url of try operator
     * @param confirmUrl http url of confirm operator
     * @param cancelUrl http url of cancel operator
     * @return http response
     * @throws Exception exception
     */
    public Response callBranch(Object body, String tryUrl, String confirmUrl, String cancelUrl) throws Exception {
        log.info("call method Tcc.callBranch, tryUrl: {}, confirmUrl: {}, cancelUrl: {}", tryUrl, confirmUrl, cancelUrl);
        String branchId = this.branchIdGenerator.genBranchId();

        TccOperatorParam operatorParam = new TccOperatorParam(this.getGid(), TransTypeEnum.TCC, branchId, Constants.DEFAULT_STATUS,
                JsonUtils.toJson(body), confirmUrl, cancelUrl);
        DtmResponse resp = dtmCommunicationClient.registerBranch(operatorParam);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction register branch fail. transaction gid: {}",  this.getGid());
            throw new FailureException("TCC Transaction register branch fail");
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(ParamFieldConstants.GID, this.getGid());
        paramsMap.put(ParamFieldConstants.TRANS_TYPE, TransTypeEnum.TCC.getValue());
        paramsMap.put(ParamFieldConstants.BRANCH_ID, branchId);
        paramsMap.put(ParamFieldConstants.OP, OP);
        Response tryResponse = HttpUtils.post(HttpUtils.splicingUrl(tryUrl, paramsMap), JsonUtils.toJson(body));
        log.info("try response is: {}", tryResponse);
        HttpUtils.checkResult(tryResponse);
        return tryResponse;
    }
}
