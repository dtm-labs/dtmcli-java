package pub.dtm.client.model.feign;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ServiceMessage {
    private String serviceName;

    private String groupName = "DEFAULT_GROUP";

    private List<String> cluster = new ArrayList<>();

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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getCluster() {
        return cluster;
    }

    public void setCluster(List<String> cluster) {
        this.cluster = cluster;
    }

    public String getPath() {
        if (StringUtils.startsWith(path, "/")) {
            return path;
        }
        return "/" + path;
    }

    public void setPath(String path) {
        this.path = path;
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
