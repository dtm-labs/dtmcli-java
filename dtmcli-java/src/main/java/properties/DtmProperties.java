package properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DtmProperties {
    private static Properties dtmProperties;

    private static void loadNacosProperties() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(DtmProperties.class.getResource("/dtm-conf.properties").getPath());
        properties.load(in);
        dtmProperties = properties;
    }

    public static String get(String key) throws IOException {
        if (dtmProperties == null) {
            loadNacosProperties();
        }
        return dtmProperties.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) throws IOException {
        if (dtmProperties == null) {
            loadNacosProperties();
        }
        return dtmProperties.getProperty(key, defaultValue);
    }

    public static Properties getNacosProperties() throws IOException {
        if (dtmProperties == null) {
            loadNacosProperties();
        }
        return dtmProperties;
    }
}
