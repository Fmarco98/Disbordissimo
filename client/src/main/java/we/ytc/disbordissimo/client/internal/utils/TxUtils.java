package we.ytc.disbordissimo.client.internal.utils;

import java.util.UUID;

public class TxUtils {

    /**
     * Generates a transaction ID. <br>
     * That ID is generated according the format: {@code contest|userID|<random string> }
     *
     * @param userID
     * @param contest
     * @return
     */
    public static String gen(long userID, String contest) {
        return contest +"|"+ userID +"|"+ UUID.randomUUID().toString().substring(0, 8);
    }
}
