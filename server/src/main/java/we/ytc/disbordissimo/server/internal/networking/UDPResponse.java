package we.ytc.disbordissimo.server.internal.networking;

import we.ytc.disbordissimo.common.AudioUtils;
import we.ytc.disbordissimo.server.internal.ActiveUser;
import we.ytc.disbordissimo.server.DisbordissimoServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

//TODO: documentation

/**
 * <h1>UDP Response</h1>
 *
 *
 */
public class UDPResponse {

    public static DatagramPacket response(InetAddress address, int port, byte[] rawData) {
        DisbordissimoServer.getServer().getLogger().logDebug("Responding to " + address + ":" + port);

        ByteBuffer recvBytes = ByteBuffer.wrap(rawData);
        long userID = recvBytes.getLong();
        byte[] mic_frame = new byte[AudioUtils.MIC_FRAME_LENGTH];
        recvBytes.position(8);
        recvBytes.get(mic_frame);

        long voiceChannelID = DisbordissimoServer.getServer().getActiveVoiceChannels().getVoiceChannel(userID);
        if(voiceChannelID == -1) {
            DisbordissimoServer.getServer().getLogger().logWarning(
                    "Responding to an NOT IN VOICE CHAT user(" + address + ":" + port + ")"
            );
            return null;
        }

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

        ByteBuffer responsePacket = ByteBuffer.allocate(UDPServer.DATAGRAM_PACKET_SIZE);
        responsePacket.putLong(voiceChannelID);
        responsePacket.put(mixed_audio);
        responsePacket.flip();

        return new DatagramPacket(responsePacket.array(), responsePacket.array().length, address, port);
    }
}
