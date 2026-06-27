package we.ytc.disbordissimo.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//TODO: documentation

/**
 * <h1>Hash utilities class</h1>
 *
 *
 */
public class HashUtils {
    private HashUtils() {}

    /** //TODO: documentation
     * Generates the SHA3-256 hash
     *
     * @param toHash
     * @return
     */
    public static String fromStringToHashedHex(String toHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] encodedHash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /** //TODO: documentation
     * Converts {@code hash} from a byte array to an HEX String
     *
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
