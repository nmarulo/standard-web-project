package red.softn.standard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

import java.util.Date;
import java.util.function.Function;

public class BasicJWT {
    
    /**
     * 1 Hora en milisegundos.
     */
    public static final long DEFAULT_EXPIRES_AT = 60 * 60 * 1000;
    
    private static Function<String, Algorithm> algorithmFunction = null;
    
    public static String create(String issuer, String secretKey) {
        return create(issuer, secretKey, DEFAULT_EXPIRES_AT);
    }
    
    public static String create(String issuer, String secretKey, long expiresAt) {
        return create(issuer, secretKey, expiresAt, null);
    }
    
    public static String create(String issuer, String secretKey, Function<JWTCreator.Builder, JWTCreator.Builder> builderFunction) {
        return create(issuer, secretKey, DEFAULT_EXPIRES_AT, builderFunction);
    }
    
    public static String create(String issuer, String secretKey, long expiresAt, Function<JWTCreator.Builder, JWTCreator.Builder> builderFunction) {
        long currentTimeMillis = System.currentTimeMillis();
        JWTCreator.Builder builder = JWT.create()
                                        .withIssuer(issuer)
                                        .withIssuedAt(currentTimeToDate(currentTimeMillis))
                                        .withExpiresAt(currentTimeToDate(currentTimeMillis, expiresAt));
        
        if (builderFunction != null) {
            builder = builderFunction.apply(builder);
        }
        
        return builder.sign(getAlgorithmFunction().apply(secretKey));
    }
    
    public static void setAlgorithmFunction(Function<String, Algorithm> algorithmFunction) {
        BasicJWT.algorithmFunction = algorithmFunction;
    }
    
    private static Function<String, Algorithm> getAlgorithmFunction() {
        if (algorithmFunction == null) {
            algorithmFunction = Algorithm::HMAC256;
        }
        
        return algorithmFunction;
    }
    
    public static boolean verify(String token, String secretKey) {
        return verify(token, secretKey, null);
    }
    
    public static boolean verify(String token, String secretKey, Function<Verification, Verification> verificationFunction) {
        try {
            verifyDecoded(token, secretKey, verificationFunction);
            
            return true;
        } catch (Exception ex) {
            //TODO: log.
            return false;
        }
    }
    
    public static DecodedJWT decoded(String token, String secretKey) throws JWTVerificationException {
        return verifyDecoded(token, secretKey, null);
    }
    
    public static DecodedJWT verifyDecoded(String token, String secretKey, Function<Verification, Verification> verificationFunction) throws JWTVerificationException {
        Verification verification = JWT.require(getAlgorithmFunction().apply(secretKey));
        
        if (verificationFunction != null) {
            verification = verificationFunction.apply(verification);
        }
        
        /*
         * Al verificar un token, la validación de tiempo se produce automáticamente,
         * lo que da como resultado la excepción JWTVerificationException
         * cuando los valores son inválidos.
         */
        return verification.build()
                           .verify(token);
    }
    
    private static Date currentTimeToDate(long currentTimeMillis) {
        return currentTimeToDate(currentTimeMillis, 0);
    }
    
    private static Date currentTimeToDate(long currentTimeMillis, long addTime) {
        if (addTime < 0) {
            addTime = 0;
        }
        
        return new Date(currentTimeMillis + addTime);
    }
}
