package red.softn.standard.security.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class BasicJWTTest {
    
    private static final String SOFT_N = "SoftN";
    
    private static final String SOFT_N_SECRET = "SoftN_secret";
    
    private static final int DEFAULT_TIME = 60;
    
    private static final String SOFT_N_JWT_ID = "SoftN_JWT_ID";
    
    private static final String CLAIM_01 = "Claim01";
    
    private static final String VALUE_CLAIM_01 = "valueClaim01";
    
    private static final String VALUE_HEADER_01 = "value_header01";
    
    private static final String HEADER_01 = "Header01";
    
    @Test
    void testCreateIssuerSecretKey() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCreateIssuerSecretKeyTime() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET, DEFAULT_TIME));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCreateIssuerSecretKeyBuilderFunction() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET, functionCreate()));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testCreateIssuerSecretKeyTimeBuilderFunction() {
        try {
            assertNotNull(BasicJWT.create(SOFT_N, SOFT_N_SECRET, DEFAULT_TIME, functionCreate()));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    private Function<JWTCreator.Builder, JWTCreator.Builder> functionCreate() {
        Map<String, Object> map = new HashMap<>();
        map.put(HEADER_01, VALUE_HEADER_01);
        
        return builder -> builder.withJWTId(SOFT_N_JWT_ID)
                                 .withAudience(SOFT_N)
                                 .withClaim(CLAIM_01, VALUE_CLAIM_01)
                                 .withHeader(map);
        
    }
    
    @Test
    void testSetAlgorithmFunction() {
        String expected = "HS384";
        BasicJWT.setAlgorithmFunction(Algorithm::HMAC384);
        String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET);
        assertEquals(expected, BasicJWT.decoded(token, SOFT_N_SECRET)
                                       .getAlgorithm());
    }
    
    @Test
    void testVerifyTokenSecret() {
        try {
            String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET, DEFAULT_TIME);
            
            assertTrue(BasicJWT.verify(token, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void testVerifyTokenSecretFunctionVerify() {
        try {
            String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET, DEFAULT_TIME, functionCreate());
            
            assertTrue(BasicJWT.verify(token, SOFT_N_SECRET, functionVerify()));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    private Function<Verification, Verification> functionVerify() {
        return verification -> verification.withJWTId(SOFT_N_JWT_ID)
                                           .withAudience(SOFT_N)
                                           .withClaim(CLAIM_01, VALUE_CLAIM_01);
    }
    
    @Test
    void testDecodedTokenSecret() {
        try {
            String token = BasicJWT.create(SOFT_N, SOFT_N_SECRET);
            assertNotNull(BasicJWT.decoded(token, SOFT_N_SECRET));
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
    @Test
    void test() {
        try {
            String     token      = BasicJWT.create(SOFT_N, SOFT_N_SECRET, functionCreate());
            DecodedJWT decodedJWT = BasicJWT.verifyDecoded(token, SOFT_N_SECRET, functionVerify());
            assertNotNull(decodedJWT);
            assertEquals(VALUE_HEADER_01, decodedJWT.getHeaderClaim(HEADER_01)
                                                    .asString());
            assertEquals(VALUE_CLAIM_01, decodedJWT.getClaim(CLAIM_01)
                                                   .asString());
        } catch (Exception ex) {
            fail(ex);
        }
    }
    
}
