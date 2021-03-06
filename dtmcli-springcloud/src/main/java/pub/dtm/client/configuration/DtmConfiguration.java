/*
 * MIT License
 *
 * Copyright (c) 2022 dtm-labs
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

package pub.dtm.client.configuration;

import pub.dtm.client.stub.URIParser;
import pub.dtm.client.properties.DtmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Dtm client configuration
 *
 * @author horse
 */
@Component
@ComponentScan({"pub.dtm.client"})
@Configuration
@EnableFeignClients("pub.dtm.client.stub")
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

    /**
     * if you need read detail log, please cancel note.
     */
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }
//
//    @Bean
//    Logger feignLogger() {
//        return new DtmFeignLogger();
//    }

}
