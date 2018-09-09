package red.softn.standard.security.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class BasicJWTTest {
    
    private static final String SOFT_N = "SoftN";
    
    private static final String SOFT_N_SECRET = "SoftN_secret";
    
    private static final int TIME = 60;
    
    private static final String SOFT_N_JWT_ID = "SoftN_JWT_ID";
    
    private static final String CLAIM_01 = "Claim01";
    
    private static final String VALUE_CLAIM_01 = "valueClaim01";
    
    @Test
    void testCreate() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCreate1() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET, TIME));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCreate2() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET, TIME, functionCreate()));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    private Function<JWTCreator.Builder, JWTCreator.Builder> functionCreate() {
        return builder -> builder.withJWTId(SOFT_N_JWT_ID)
                                 .withAudience(SOFT_N)
                                 .withClaim(CLAIM_01, VALUE_CLAIM_01);
        
    }
    
    @Test
    void testSetAlgorithmFunction() {
        BasicJWT.setAlgorithmFunction(Algorithm::HMAC256);
    }
    
    @Test
    void testVerify() {
        String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET, TIME, functionCreate());
        
        assertTrue(BasicJWT.verify(token, SOFT_N_SECRET));
    }
    
    @Test
    void testVerify1() {
        String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET, TIME, functionCreate());
        
        assertTrue(BasicJWT.verify(token, SOFT_N_SECRET, functionVerify()));
    }
    
    private Function<Verification, Verification> functionVerify() {
        return verification -> verification.withJWTId(SOFT_N_JWT_ID)
                                           .withAudience(SOFT_N)
                                           .withClaim(CLAIM_01, VALUE_CLAIM_01);
    }
    
    @Test
    void testDecodedJWT() {
        String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET, TIME, functionCreate());
        assertNotNull(BasicJWT.decodedJWT(token));
    }
    
    @Test
    void testGetClaim() {
        String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET, TIME, functionCreate());
        
        assertNotNull(BasicJWT.getClaim(token, CLAIM_01, String.class));
        assertEquals(VALUE_CLAIM_01, BasicJWT.getClaim(token, CLAIM_01, String.class));
    }
    
}
