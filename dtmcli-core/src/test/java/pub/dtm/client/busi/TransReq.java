package pub.dtm.client.busi;

import java.time.Duration;

public class TransReq {

    public Exception ex;

    public Duration sleepDuration;

    /**
     * 用户id
     */
    public int userId;

    /**
     * 转入/转出金额
     */
    public int amount;

    public TransReq() {
    }

    public TransReq(int userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }
}