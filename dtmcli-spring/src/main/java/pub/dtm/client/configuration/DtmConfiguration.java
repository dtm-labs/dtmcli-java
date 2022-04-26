package pub.dtm.client.configuration;

import pub.dtm.client.feign.URIParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import pub.dtm.client.properties.DtmProperties;

@Component
@ComponentScan({"pub.dtm.client.feign", "pub.dtm.client"})
@Configuration
@EnableFeignClients("pub.dtm.client.feign")
@EnableConfigurationProperties(DtmProperties.class)
public class DtmConfiguration {
    @Autowired
    private DtmProperties dtmProperties;

    @Bean
    @ConditionalOnMissingBean(URIParser.class)
    public URIParser uriParser() {
        URIParser.setRegistryType(dtmProperties.getRegistryType());
        return new URIParser();
    }
}
