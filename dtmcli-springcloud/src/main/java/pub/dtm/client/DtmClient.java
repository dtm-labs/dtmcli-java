/*
 * MIT License
 *
 * Copyright (c) 2022 dtm-labs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pub.dtm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pub.dtm.client.interfaces.stub.IDtmServerStub;
import pub.dtm.client.interfaces.dtm.DtmConsumer;
import pub.dtm.client.saga.Saga;
import pub.dtm.client.tcc.Tcc;

/**
 * dtm client for spring
 *
 * @author horseLk
 */
@Component
public class DtmClient {
    @Autowired
    private IDtmServerStub dtmServerStub;

    /**
     * start a tcc transaction without gid, client send a request to dtm svr for obtain a new gid.
     * @return gid
     */
    public String tccGlobalTransaction(DtmConsumer<Tcc> function) throws Exception {
        Tcc tcc = new Tcc(null, dtmServerStub);
        return tcc.tccGlobalTransaction(function);
    }

    /**
     * start a tcc transaction with custom gid
     * @param gid gid
     * @return gid
     */
    public String tccGlobalTransaction(String gid, DtmConsumer<Tcc> function) throws Exception {
        Tcc tcc = new Tcc(gid, dtmServerStub);
        return tcc.tccGlobalTransaction(function);
    }

    /**
     * start a saga transaction with custom gid
     * @param gid gid
     * @return Saga
     */
    public Saga newSaga(String gid) {
        return new Saga(gid, dtmServerStub);
    }

    /**
     * start a saga transaction without gid, client send a request to dtm svr for obtain a new gid.
     * @return Saga
     */
    public Saga newSaga() {
        return new Saga(null, dtmServerStub);
    }
}
