package tcc;

import com.google.gson.Gson;
import constant.Constants;
import constant.ParamFieldConstants;
import enums.TransTypeEnum;
import exception.DtmException;
import interfaces.dtm.DtmConsumer;
import interfaces.dtm.IDtmService;
import interfaces.feign.IDtmFeignClient;
import model.feign.ServiceMessage;
import model.param.OperatorParam;
import model.response.ResponseBase;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tcc.model.Tcc;
import utils.FeignUtils;
import utils.HttpUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class TccService implements IDtmService {
    private static final Logger log = LoggerFactory.getLogger(TccService.class);

    private static final String OP = "try";

    private IDtmFeignClient feignClient;

    public TccService(IDtmFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public void setFeignClient(IDtmFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public String tccGlobalTransaction(String gid, DtmConsumer<TccService, Tcc> consumer) throws Exception {
        if (StringUtils.isEmpty(gid)) {
            gid = FeignUtils.parseGid(feignClient.newGid());
        }
        log.info("the tcc transaction's gid is {}", gid);
        Tcc tcc = new Tcc(gid);


        OperatorParam operatorParam = new OperatorParam(gid, TransTypeEnum.TCC);
        ResponseBase resp = feignClient.prepare(operatorParam);
        log.info("prepare response: {}", resp);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction prepare fail. returned dtm_result is: {}, transaction gid: {}", resp.getDtmResult(), gid);
            throw new DtmException("TCC Transaction prepare fail");
        }
        try {
            consumer.accept(this, tcc);
            feignClient.submit(operatorParam);
        } catch (Exception e) {
            log.error("TCC transaction submit fail, start abort it. transaction gid: {}", gid);
            feignClient.abort(operatorParam);
            throw new DtmException(e);
        }
        return gid;
    }

    public feign.Response callBranch(Tcc tcc, Object body, ServiceMessage tryMessage, ServiceMessage confirmMessage, ServiceMessage cancelMessage) throws Exception {
        Gson gson = new Gson();
        log.info("call method Tcc.callBranch, tryMessage: {}, confirmMessage: {}, cancelMessage: {}",
                gson.toJson(tryMessage), gson.toJson(confirmMessage), gson.toJson(cancelMessage));
        String branchId = tcc.getBranchIdGenerator().genBranchId();

        OperatorParam operatorParam = new OperatorParam(tcc.getGid(), TransTypeEnum.TCC, Constants.DEFAULT_STATUS, branchId,
                gson.toJson(body), FeignUtils.generatorURI(confirmMessage, false), FeignUtils.generatorURI(cancelMessage, false));
        ResponseBase resp = feignClient.registerBranch(operatorParam);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction register branch fail. transaction gid: {}",  tcc.getGid());
            throw new DtmException("TCC Transaction register branch fail");
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(ParamFieldConstants.GID, tcc.getGid());
        paramsMap.put(ParamFieldConstants.TRANS_TYPE, TransTypeEnum.TCC.toString());
        paramsMap.put(ParamFieldConstants.BRANCH_ID, branchId);
        paramsMap.put(ParamFieldConstants.OP, OP);

        feign.Response response = feignClient.busiPost(new URI(FeignUtils.generatorURI(tryMessage, true)),
                tryMessage.getPath(), paramsMap, body);
        log.info("busi post is: {}", response);
        FeignUtils.checkResult(response);
        return response;
    }

    public Response callBranch(Tcc tcc, Object body, String tryUrl, String confirmUrl, String cancelUrl) throws Exception {
        log.info("call method Tcc.callBranch, tryUrl: {}, confirmUrl: {}, cancelUrl: {}", tryUrl, confirmUrl, cancelUrl);
        String branchId = tcc.getBranchIdGenerator().genBranchId();
        Gson gson = new Gson();

        OperatorParam operatorParam = new OperatorParam(tcc.getGid(), TransTypeEnum.TCC, Constants.DEFAULT_STATUS,
                branchId, gson.toJson(body), confirmUrl, cancelUrl);
        ResponseBase resp = feignClient.registerBranch(operatorParam);
        if (!Constants.SUCCESS_RESULT.equals(resp.getDtmResult())) {
            log.error("TCC transaction register branch fail. transaction gid: {}",  tcc.getGid());
            throw new DtmException("TCC Transaction register branch fail");
        }

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(ParamFieldConstants.GID, tcc.getGid());
        paramsMap.put(ParamFieldConstants.TRANS_TYPE, TransTypeEnum.TCC.toString());
        paramsMap.put(ParamFieldConstants.BRANCH_ID, branchId);
        paramsMap.put(ParamFieldConstants.OP, OP);
        Response tryResponse = HttpUtils.post(HttpUtils.splicingUrl(tryUrl, paramsMap), gson.toJson(body));
        log.info("try response is: {}", tryResponse);
        HttpUtils.checkResult(tryResponse);
        return tryResponse;
    }
}
