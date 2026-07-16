package we.ytc.disbordissimo.server.internal.networking;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kcp.Ukcp;
import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.server.DisbordissimoServer;
import we.ytc.disbordissimo.server.internal.ActiveUser;

import java.util.ArrayList;
import java.util.List;

public class KCPResponse {

    public static void response(ByteBuf in, Ukcp ukcp) {
        byte[] mic_frame = new byte[AudioUtils.MIC_FRAME_LENGTH];
        long userID = in.getLong(0);
        in.getBytes(8, mic_frame);

        long voiceChannelID = DisbordissimoServer.getServer().getActiveVoiceChannels().getVoiceChannel(userID);
        if(voiceChannelID == -1) return;

        List<ActiveUser> connectedUsers = DisbordissimoServer.getServer().getActiveVoiceChannels().getConnectedUsers(voiceChannelID);
        List<byte[]> streams = new ArrayList<>();
        synchronized (connectedUsers) {
            connectedUsers.stream().forEach(user -> {
                if (user.getUserID() != userID) {
                    streams.add(user.getMicFrame());
                } else {
                    user.setMicFrame(mic_frame);
                }
            });
        }

        //Mix audio
        byte[] mixed_audio = streams.size() > 0 ? AudioUtils.mixListOfStreams(streams) : new byte[AudioUtils.MIC_FRAME_LENGTH];

        ByteBuf resp = Unpooled.buffer(KCPServer.KCP_PACKET_SIZE);
        resp.writeLong(voiceChannelID);
        resp.writeBytes(mixed_audio);

        ukcp.write(resp);

        resp.release();
    }
}
