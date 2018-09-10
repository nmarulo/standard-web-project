package red.softn.standard.security.crypto;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BasicCipher {
    
    public static String cipherString(String value, String secretKey) throws Exception {
        return Base64.encodeBase64String(init(value.getBytes(StandardCharsets.UTF_8), secretKey, Cipher.ENCRYPT_MODE));
    }
    
    public static byte[] cipherBytes(String value, String secretKey) throws Exception {
        return init(value.getBytes(StandardCharsets.UTF_8), secretKey, Cipher.ENCRYPT_MODE);
    }
    
    public static String cipherURLSafeString(String value, String secretKey) throws Exception {
        return Base64.encodeBase64URLSafeString(init(value.getBytes(StandardCharsets.UTF_8), secretKey, Cipher.ENCRYPT_MODE));
    }
    
    public static String decipher(String value, String secretKey) throws Exception {
        return new String(init(Base64.decodeBase64(value), secretKey, Cipher.DECRYPT_MODE), StandardCharsets.UTF_8);
    }
    
    private static byte[] init(byte[] value, String secretKey, int encryptMode) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
        digest.update(secretKey.getBytes(StandardCharsets.UTF_8));
        final SecretKeySpec secretKeySpec = new SecretKeySpec(digest.digest(), 0, 16, "AES");
        //https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html
        final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(encryptMode, secretKeySpec);
        
        return aes.doFinal(value);
    }
}
