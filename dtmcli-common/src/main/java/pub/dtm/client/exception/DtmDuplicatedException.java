package pub.dtm.client.exception;

/**
 * dtm duplicated exception
 *
 * @author yxou
 */
public class DtmDuplicatedException extends DtmException {

    public DtmDuplicatedException() {
        super(ERR_DUPLICATED);
    }

    public DtmDuplicatedException(String message) {
        super(message);
    }
}
