package pub.dtm.client.exception;

/**
 * dtm failure exception
 *
 * @author yxou
 */
public class DtmFailureException extends DtmException{

    public DtmFailureException(){
        super(ERR_FAILURE);
    }

    public DtmFailureException(String message){
        super(message);
    }
}
