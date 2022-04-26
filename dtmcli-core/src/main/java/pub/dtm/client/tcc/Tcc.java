package pub.dtm.client.tcc;

import pub.dtm.client.base.BranchIdGenerator;
import com.google.gson.Gson;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.constant.ParamFieldConstants;
import pub.dtm.client.enums.TransTypeEnum;
import pub.dtm.client.exception.DtmException;
import pub.dtm.client.interfaces.dtm.DtmConsumer;
import pub.dtm.client.interfaces.feign.IDtmFeignClient;
import pub.dtm.client.model.dtm.TransBase;
import pub.dtm.client.model.feign.ServiceMessage;
import pub.dtm.client.model.param.OperatorParam;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.dtm.client.model.responses.DtmResponse;
import pub.dtm.client.utils.FeignUtils;
import pub.dtm.client.utils.HttpUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Tcc extends TransBase {
    private static final Logger log = LoggerFactory.getLogger(Tcc.class);

    private static final String OP = "try";

    private final BranchIdGenerator branchIdGenerator;

    private IDtmFeignClient feignClient;

    public Tcc(String gid, IDtmFeignClient feignClient) {
        super(gid, TransTypeEnum.TCC, false);
        this.branchIdGenerator = new BranchIdGenerator(Constants.EMPTY_STRING);
        this.feignClient = feignClient;
    }

    public void setFeignClient(IDtmFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public String tccGlobalTransaction(DtmConsumer<Tcc> consumer) throws Exception {
        if (StringUtils.isEmpty(this.getGid())) {
            this.setGid(FeignUtils.parseGid(feignClient.newGid()));
        }
        log.info("the tcc transaction's gid is {}", this.getGid());

        OperatorParam operatorParam = new OperatorParam(this.getGid(), TransTypeEnum.TCC);
        DtmResponse resp = feignClient.prepare(operatorParam);
        log.info("prepare response: {}", resp);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction prepare fail. returned dtm_result is: {}, transaction gid: {}", resp.getDtmResult(), this.getGid());
            throw new DtmException("TCC Transaction prepare fail");
        }
        try {
            consumer.accept(this);
            feignClient.submit(operatorParam);
        } catch (Exception e) {
            log.error("TCC transaction submit fail, start abort it. transaction gid: {}", this.getGid());
            feignClient.abort(operatorParam);
            throw new DtmException(e);
        }
        return this.getGid();
    }

    public feign.Response callBranch(Object body, ServiceMessage tryMessage, ServiceMessage confirmMessage, ServiceMessage cancelMessage) throws Exception {
        Gson gson = new Gson();
        log.info("call method Tcc.callBranch, tryMessage: {}, confirmMessage: {}, cancelMessage: {}",
                gson.toJson(tryMessage), gson.toJson(confirmMessage), gson.toJson(cancelMessage));
        String branchId = this.branchIdGenerator.genBranchId();

        OperatorParam operatorParam = new OperatorParam(this.getGid(), TransTypeEnum.TCC, Constants.DEFAULT_STATUS, branchId,
                gson.toJson(body), FeignUtils.generatorURI(confirmMessage, false), FeignUtils.generatorURI(cancelMessage, false));
        DtmResponse resp = feignClient.registerBranch(operatorParam);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction register branch fail. transaction gid: {}",  this.getGid());
            throw new DtmException("TCC Transaction register branch fail");
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(ParamFieldConstants.GID, this.getGid());
        paramsMap.put(ParamFieldConstants.TRANS_TYPE, TransTypeEnum.TCC.toString());
        paramsMap.put(ParamFieldConstants.BRANCH_ID, branchId);
        paramsMap.put(ParamFieldConstants.OP, OP);

        feign.Response response = feignClient.busiPost(new URI(FeignUtils.generatorURI(tryMessage, true)),
                tryMessage.getPath(), paramsMap, body);
        log.info("busi post is: {}", response);
        FeignUtils.checkResult(response);
        return response;
    }

    public Response callBranch(Object body, String tryUrl, String confirmUrl, String cancelUrl) throws Exception {
        log.info("call method Tcc.callBranch, tryUrl: {}, confirmUrl: {}, cancelUrl: {}", tryUrl, confirmUrl, cancelUrl);
        String branchId = branchIdGenerator.genBranchId();
        Gson gson = new Gson();

        OperatorParam operatorParam = new OperatorParam(this.getGid(), TransTypeEnum.TCC, Constants.DEFAULT_STATUS,
                branchId, gson.toJson(body), confirmUrl, cancelUrl);
        DtmResponse resp = feignClient.registerBranch(operatorParam);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction register branch fail. transaction gid: {}",  this.getGid());
            throw new DtmException("TCC Transaction register branch fail");
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(ParamFieldConstants.GID, this.getGid());
        paramsMap.put(ParamFieldConstants.TRANS_TYPE, TransTypeEnum.TCC.toString());
        paramsMap.put(ParamFieldConstants.BRANCH_ID, branchId);
        paramsMap.put(ParamFieldConstants.OP, OP);
        Response tryResponse = HttpUtils.post(HttpUtils.splicingUrl(tryUrl, paramsMap), gson.toJson(body));
        log.info("try response is: {}", tryResponse);
        HttpUtils.checkResult(tryResponse);
        return tryResponse;
    }
}
