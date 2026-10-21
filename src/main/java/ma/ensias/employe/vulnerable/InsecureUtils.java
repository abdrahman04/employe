package ma.ensias.employe.vulnerable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HexFormat;
import java.util.Random;

/**
 * DELIBERATELY INSECURE helpers, used only to test SonarCloud detection.
 */
public final class InsecureUtils {

    // VULN A07 / secret in plaintext: hard-coded credentials
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "Sup3rS3cret!2026";
    // Fake AWS key pair (AWS documentation example values)
    private static final String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    private InsecureUtils() {
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/employedemo", "admin", "Sup3rS3cret!2026");
    }

    // VULN A02 Cryptographic Failures: MD5 used to hash passwords
    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return HexFormat.of().formatHex(md.digest(password.getBytes()));
    }

    // VULN A02 Cryptographic Failures: DES in ECB mode
    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec("12345678".getBytes(), "DES"));
        return cipher.doFinal(data);
    }

    // VULN A02 Cryptographic Failures: predictable random for a security token
    public static String resetToken() {
        return Long.toHexString(new Random().nextLong());
    }

    public static String awsCredentials() {
        return AWS_ACCESS_KEY_ID + ":" + AWS_SECRET_ACCESS_KEY + "@" + DB_USER + ":" + DB_PASSWORD;
    }
}
