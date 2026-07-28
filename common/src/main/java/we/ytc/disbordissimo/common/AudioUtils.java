/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.common;

import java.util.List;

/**
 * <h1>AudioUtils static class</h1>
 *
 * Constants:<br>
 *  - MIC_FRAME_LENGTH<br>
 * <br>
 * Functions:<br>
 *  - mixListOfStreams(..)<br>
 *  - mixSteams(..)<br>
 */
public class AudioUtils {
    private AudioUtils() {}

    /**
     * Length of a MIC_FRAME.
     */
    public static final int MIC_FRAME_LENGTH = 480 * 2;

    /**
     * Mix all input PCM {@code streams} into a single stream.
     *
     * @param streams
     *        List of PCM streams
     *
     * @return mixed stream
     */
    public static byte[] mixListOfStreams(List<byte[]> streams) {
        byte[] finalBuffer = streams.get(0);

        for (int i = 1; i < streams.size(); i++) {
            finalBuffer = mixSteams(finalBuffer, streams.get(i));
        }

        return finalBuffer;
    }

    /**
     * Mix {@code streamA} and {@code streamB} into a single stream.
     *
     * @param streamA
     *        PCM stream A
     *
     * @param streamB
     *        PCM stream B
     *
     * @return mixed stream
     */
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
