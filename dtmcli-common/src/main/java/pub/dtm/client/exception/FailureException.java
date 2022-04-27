package pub.dtm.client.exception;

public class FailureException extends Exception {
    public FailureException(String msg) {
        super(msg);
    }

    public FailureException(Throwable e) {
        super(e);
    }
}
