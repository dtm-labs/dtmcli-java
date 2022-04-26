package properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DtmProperties.PREFIX)
@Data
public class DtmProperties {
    public static final String PREFIX = "dtm.service";

    private String registryType;

    private String name;

}
