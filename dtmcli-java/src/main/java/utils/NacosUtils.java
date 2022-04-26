package utils;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import properties.DtmProperties;

import java.util.List;

public class NacosUtils {
    private static NamingService namingService;

    public static void buildNamingService() throws Exception {
        NacosUtils.namingService = NamingFactory.createNamingService(DtmProperties.getNacosProperties());
    }

    public static Instance selectOneHealthyInstance(String serviceName, String groupName, List<String> cluster) throws Exception {
        if (namingService == null) {
            buildNamingService();
        }
        return namingService.selectOneHealthyInstance(serviceName, groupName, cluster);
    }
}
