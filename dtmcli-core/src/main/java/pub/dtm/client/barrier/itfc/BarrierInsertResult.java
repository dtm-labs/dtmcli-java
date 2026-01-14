package pub.dtm.client.barrier.itfc;

/**
 * barrier insert result
 *
 * @author yxou
 */
public class BarrierInsertResult {
    private final int affectedRows;
    private final Exception exception;

    public BarrierInsertResult(int affectedRows) {
        this.affectedRows = affectedRows;
        this.exception = null;
    }

    public BarrierInsertResult(int affectedRows, Exception exception) {
        this.affectedRows = affectedRows;
        this.exception = exception;
    }

    public int getAffectedRows() { return affectedRows; }
    public Exception getException() { return exception; }
}
