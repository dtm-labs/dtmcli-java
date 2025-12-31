package pub.dtm.client.busi;

public class TransReq {

    /**
     * 用户id
     */
    public int userId;

    /**
     * 转入/转出金额
     */
    public int amount;

    public TransReq(int userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}