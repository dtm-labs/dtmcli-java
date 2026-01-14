package pub.dtm.client.exception;

/**
 * dtm onging exception
 *
 * @author yxou
 */
public class DtmOngingException extends DtmException {

    public DtmOngingException() {
        super(ERR_ONGOING);
    }

    public DtmOngingException(String message) {
        super(message);
    }
}
