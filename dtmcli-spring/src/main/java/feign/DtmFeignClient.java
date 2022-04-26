package feign;

import constant.Constants;
import interfaces.feign.IDtmFeignClient;
import model.param.OperatorParam;
import model.response.ResponseBase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Map;

@FeignClient(value = "${dtm.service.name}")
public interface DtmFeignClient extends IDtmFeignClient {
    @Override
    @GetMapping(Constants.NEW_GID_URL)
    ResponseBase newGid();

    @Override
    @GetMapping(Constants.PING_URL)
    ResponseBase ping();

    @Override
    @PostMapping(Constants.PREPARE_URL)
    ResponseBase prepare(@RequestBody OperatorParam body);

    @Override
    @PostMapping(Constants.SUBMIT_URL)
    ResponseBase submit(@RequestBody OperatorParam body);

    @Override
    @PostMapping(Constants.ABORT_URL)
    ResponseBase abort(@RequestBody OperatorParam body);

    @Override
    @PostMapping(Constants.REGISTER_BRANCH_URL)
    ResponseBase registerBranch(@RequestBody OperatorParam body);

    @Override
    @GetMapping(value = "{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response busiGet(URI host, @PathVariable("path") String path, @SpringQueryMap Map<String, Object> queryMap);

    @Override
    @PostMapping(value = "{path}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Response busiPost(URI host, @PathVariable("path") String path, @SpringQueryMap Map<String, Object> queryMap, @RequestBody Object body);
}
