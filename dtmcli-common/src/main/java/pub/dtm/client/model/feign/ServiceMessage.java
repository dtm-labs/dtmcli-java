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

package pub.dtm.client.model.feign;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service message for micro service
 *
 * @author horseLk
 */
@Data
@NoArgsConstructor
public class ServiceMessage {
    /**
     * service name
     */
    private String serviceName;

    /**
     * group name
     */
    private String groupName = "DEFAULT_GROUP";

    /**
     * clusters
     */
    private List<String> cluster = new ArrayList<>();

    /**
     * request path
     */
    private String path;

    public ServiceMessage(String serviceName, String path){
        this.serviceName = serviceName;
        this.path = path;
    }

    public ServiceMessage(String serviceName, String groupName, List<String> cluster, String path) {
        this.serviceName = serviceName;
        this.groupName = groupName;
        this.cluster.addAll(cluster);
        this.path = path;
    }

    public String getPath() {
        if (StringUtils.startsWith(path, "/")) {
            return path;
        }
        return "/" + path;
    }

    @Override
    public String toString() {
        String _path = path;
        if (!StringUtils.startsWith(path, "/")) {
            _path = "/" + path;
        }
        return serviceName + _path + "?groupName=" + groupName;
    }
}
