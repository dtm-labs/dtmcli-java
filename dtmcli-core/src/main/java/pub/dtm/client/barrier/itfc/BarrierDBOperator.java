package pub.dtm.client.barrier.itfc;

/**
 * BarrierDBOperator is a interface to insert barrier, you can implements the interface according to your db.
 *
 * @author horseLk
 * @date 2022-09-28 22:02
 */
public interface BarrierDBOperator {
    /**
     *
     * @param transType Trans type
     * @param gid gid
     * @param branchId branch ID
     * @param op operator
     * @param barrierId barrier ID
     * @return Insert Success
     */
    boolean insertBarrier(String transType, String gid, String branchId, String op, int barrierId) throws Exception;

    /**
     * commit transaction
     * @throws Exception
     */
    void commit() throws Exception;

    /**
     * rollback transaction
     * @throws Exception
     */
    void rollback() throws Exception;
}
