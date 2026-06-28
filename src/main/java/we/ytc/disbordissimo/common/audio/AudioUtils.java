package we.ytc.disbordissimo.common.audio;

import java.util.List;

public class AudioUtils {
    private AudioUtils() {}

    public static final int MIC_FRAME_LENGTH = 1024;

    public static byte[] mixListOfStreams(List<byte[]> streams) {
        byte[] finalBuffer = streams.get(0);

        for (int i = 1; i < streams.size(); i++) {
            finalBuffer = mixSteams(finalBuffer, streams.get(i));
        }

        return finalBuffer;
    }

    public static byte[] mixSteams(byte[] streamA, byte[] streamB) {
        int bytesToProcess = Math.min(streamA.length, streamB.length);
        byte[] mixedBuffer = new byte[bytesToProcess];

        for (int i = 0; i < bytesToProcess; i += 2) {
            int micSample = (short) ((streamA[i] & 0xFF) | (streamA[i + 1] << 8));
            int fileSample = (short) ((streamB[i] & 0xFF) | (streamB[i + 1] << 8));

            float volume = 1f;
            int mixed = (int) ((micSample * volume) + (fileSample * volume));

            if (mixed > 32767) {
                mixed = 32767;
            } else if (mixed < -32768) {
                mixed = -32768;
            }

            // Riconversione nei due byte originari
            mixedBuffer[i] = (byte) (mixed & 0xFF);
            mixedBuffer[i + 1] = (byte) ((mixed >> 8) & 0xFF);
        }

        return mixedBuffer;
    }
}
