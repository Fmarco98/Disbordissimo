package we.ytc.disbordissimo.cli;

import de.maxhenkel.rnnoise4j.Denoiser;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MicManager {
    private AudioInputStream convertedMicStream;
    private TargetDataLine micLine;
    private Denoiser deno;

    private final AudioFormat FORMAT = new AudioFormat(48000.0f, 16, 1, true, false); // Mono, 16-bit, Little Endian

    public MicManager() {
        try {
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, FORMAT);
            micLine = (TargetDataLine) AudioSystem.getLine(micInfo);

            AudioInputStream micStream = new AudioInputStream(micLine);
            convertedMicStream = AudioSystem.getAudioInputStream(FORMAT, micStream);

            deno = new Denoiser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void open() throws LineUnavailableException {
        micLine.open(FORMAT);
        micLine.start();
    }

    public void close() {
        micLine.close();
    }

    public byte[] getMicBytes() {
        try {
            int packetSize = deno.getFrameSize() * 2;
            byte[] micBuffer = new byte[packetSize];

            int micBytesRead = convertedMicStream.read(micBuffer, 0, micBuffer.length);
            short[] micBufDenosd = deno.denoise(toShortArrayLittleEndian(micBuffer));

            toByteArray(micBufDenosd, micBuffer);

            return micBuffer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static short[] toShortArrayLittleEndian(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        short[] shortArray = new short[byteArray.length / 2];
        buffer.asShortBuffer().get(shortArray);

        return shortArray;
    }

    public static void toByteArray(short[] shortArr, byte[] destination) {
        for (int i = 0; i < shortArr.length; i++) {
            destination[i * 2]     = (byte) (shortArr[i] & 0xFF);
            destination[i * 2 + 1] = (byte) ((shortArr[i] >> 8) & 0xFF);
        }
    }
}