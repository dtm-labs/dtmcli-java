package pub.dtm.client.exception;

public class DtmException extends Exception {
    public DtmException(String msg) {
        super(msg);
    }

    public DtmException(Throwable e) {
        super(e);
    }
}
