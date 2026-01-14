package pub.dtm.client.exception;

/**
 * dtm exception
 *
 * @author yxou
 */
public class DtmException  extends Exception{
    public DtmException(String message) {
        super(message);
    }

    public static final String ERR_FAILURE = "FAILURE";
    public static final String ERR_ONGOING = "ONGOING";
    public static final String ERR_DUPLICATED = "DUPLICATED";
}
