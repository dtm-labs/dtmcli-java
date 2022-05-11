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

package pub.dtm.client.properties;

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
