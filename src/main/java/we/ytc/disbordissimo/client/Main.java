package we.ytc.disbordissimo.client;

import we.ytc.disbordissimo.common.logger.YtcLogger;

import java.net.InetAddress;

public class Main {

    //Test
    public static void main(String[] args) throws Exception {
        long userID = 1234;
        long chID = 9876;

        DisbordissimoClient.Config config = new DisbordissimoClient.Config(InetAddress.getByName("localhost"), 6969, 500);
        DisbordissimoClient client = DisbordissimoClient.create(config, new YtcLogger());

        //client.signUp("gigio", "123456");
        client.login("pippo", "123456");
        client.join("voice1", "pippo's server");
        boolean f = client.isConnectedTo("voice1", "pippo's server");
        new YtcLogger().logMsg(String.valueOf(f));
    }
}
