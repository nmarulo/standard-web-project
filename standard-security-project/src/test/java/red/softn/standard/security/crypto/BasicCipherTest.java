package red.softn.standard.security.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicCipherTest {
    
    public static final String SOFT_N_SECRET = "SoftN_Secret";
    
    public static final String SOFT_N = "SoftN";
    
    @Test
    void testCipherString() {
        try {
            assertNotNull(BasicCipher.cipherString(SOFT_N, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCipherBytes() {
        try {
            assertNotNull(BasicCipher.cipherBytes(SOFT_N, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCipherURLSafeString() {
        try {
            assertNotNull(BasicCipher.cipherURLSafeString(SOFT_N, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testDecipher() {
        try {
            String cipher   = BasicCipher.cipherString(SOFT_N, SOFT_N_SECRET);
            String decipher = BasicCipher.decipher(cipher, SOFT_N_SECRET);
            assertEquals(SOFT_N, decipher);
        } catch (Exception ex) {
            fail(ex);
        }
    }
}
