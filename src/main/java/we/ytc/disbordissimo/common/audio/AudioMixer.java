package we.ytc.disbordissimo.common.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AudioMixer {
    public static void main(String[] args) throws FileNotFoundException {
        File audioFile = new File("mp3.wav");
        FileInputStream fis = new FileInputStream(audioFile);

        try {
            // 1. Configura il formato audio comune
            AudioFormat format = new AudioFormat(48000, 16, 1, true, false); // Mono, 16-bit, Little Endian

            // 2. Apri il file audio
            AudioInputStream fileStream = AudioSystem.getAudioInputStream(audioFile);
            // Forza il file ad avere lo stesso formato se necessario (opzionale ma sicuro)
            AudioInputStream convertedFileStream = AudioSystem.getAudioInputStream(format, fileStream);

            // 3. Apri il microfono (TargetDataLine)
            DataLine.Info micInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine micLine = (TargetDataLine) AudioSystem.getLine(micInfo);
            micLine.open(format);
            micLine.start();

            // 4. Apri la linea di uscita (SourceDataLine)
            DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine outLine = (SourceDataLine) AudioSystem.getLine(outInfo);
            outLine.open(format);
            outLine.start();

            // Buffer per la lettura (1024 byte ad esempio)
            byte[] micBuffer = new byte[1024];
            byte[] fileBuffer = new byte[1024];
            byte[] mixedBuffer = new byte[1024];

            System.out.println("Mixaggio in corso... Premi CTRL+C per fermare.");

            while (true) {
                // Leggi dal microfono
                int micBytesRead = micLine.read(micBuffer, 0, micBuffer.length);

                // Leggi dal file
                int fileBytesRead = convertedFileStream.read(fileBuffer, 0, fileBuffer.length);

                // Se il file finisce, azzera il buffer del file per sentire solo il mic
                if (fileBytesRead == -1) {
                    java.util.Arrays.fill(fileBuffer, (byte) 0);
                    fileBytesRead = micBytesRead;
                }

                // Determina quanti byte processare in questo ciclo
                int bytesToProcess = Math.min(micBytesRead, fileBytesRead);
                if (bytesToProcess <= 0) break;

                for (int i = 0; i < bytesToProcess; i += 2) {

                    // CORRETTO: Estrazione dei campioni a 16-bit (Little Endian)
                    int micSample = (short) ((micBuffer[i] & 0xFF) | (micBuffer[i + 1] << 8));
                    int fileSample = (short) ((fileBuffer[i] & 0xFF) | (fileBuffer[i + 1] << 8));

                    // Attenuazione dinamica: abbassiamo il volume di entrambe le sorgenti
                    // Moltiplicare per 0.4 riduce il guadagno complessivo lasciando un "margine di sicurezza" (headroom)
                    float micVolume = 1f;
                    float fileVolume = 1f;

                    int mixed = (int) ((micSample * micVolume) + (fileSample * fileVolume));

                    // Hard Clipping Prevention (Taglio netto sui limiti di sicurezza)
                    if (mixed > 32767) {
                        mixed = 32767;
                    } else if (mixed < -32768) {
                        mixed = -32768;
                    }

                    // Riconversione nei due byte originari
                    mixedBuffer[i] = (byte) (mixed & 0xFF);
                    mixedBuffer[i + 1] = (byte) ((mixed >> 8) & 0xFF);
                }

                // 6. Scrivi il risultato nella SourceDataLine
                outLine.write(mixedBuffer, 0, bytesToProcess);
            }

            // Chiusura delle risorse (se il loop dovesse interrompersi)
            outLine.drain();
            outLine.close();
            micLine.close();
            convertedFileStream.close();

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}