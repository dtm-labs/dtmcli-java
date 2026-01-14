package pub.dtm.client.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * barrier constants
 *
 * @author yxou
 */
public class BarrierConstants {

    public static final Map<String, String> OP_DICT;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("cancel", "try");
        map.put("compensate", "action");
        OP_DICT = Collections.unmodifiableMap(map);
    }
}
