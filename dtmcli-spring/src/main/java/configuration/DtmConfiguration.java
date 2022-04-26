package configuration;

import barrier.BranchBarrierService;
import feign.DtmFeignClient;
import feign.URIParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import properties.DtmProperties;
import tcc.TccService;

@Component
@ComponentScan({"feign", "client"})
@Configuration
@EnableFeignClients("feign")
@EnableConfigurationProperties(DtmProperties.class)
public class DtmConfiguration {
    @Autowired
    private DtmProperties dtmProperties;

    @Bean
    @ConditionalOnMissingBean(TccService.class)
    public TccService tccService(DtmFeignClient feignClient) {
        return new TccService(feignClient);
    }

    @Bean
    @ConditionalOnMissingBean(BranchBarrierService.class)
    public BranchBarrierService branchBarrierService() {
        return new BranchBarrierService();
    }

    @Bean
    @ConditionalOnMissingBean(URIParser.class)
    public URIParser uriParser() {
        URIParser.setRegistryType(dtmProperties.getRegistryType());
        return new URIParser();
    }
}
