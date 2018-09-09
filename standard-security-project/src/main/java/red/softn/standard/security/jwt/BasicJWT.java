package red.softn.standard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

import java.util.Date;
import java.util.function.Function;

public class BasicJWT {
    
    private static Function<String, Algorithm> algorithmFunction = null;
    
    public static String create(String issuer, String secretKey) {
        return create(issuer, secretKey, 60);
    }
    
    public static String create(String issuer, String secretKey, long time) {
        return create(issuer, secretKey, time, null);
    }
    
    public static String create(String issuer, String secretKey, long time, Function<JWTCreator.Builder, JWTCreator.Builder> builderFunction) {
        long currentTimeMillis = System.currentTimeMillis();
        JWTCreator.Builder builder = JWT.create()
                                        .withIssuer(issuer)
                                        .withIssuedAt(currentTimeToDate(currentTimeMillis))
                                        .withExpiresAt(currentTimeToDate(currentTimeMillis, time));
        
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
            Verification verification = JWT.require(getAlgorithmFunction().apply(secretKey));
            
            if (verificationFunction != null) {
                verification = verificationFunction.apply(verification);
            }
            
            verification.build()
                        .verify(token);
            
            return true;
        } catch (Exception ex) {
            //TODO: log
            return false;
        }
    }
    
    public static <R> R getClaim(String token, String name, Class<R> aClass) throws JWTDecodeException {
        return decodedJWT(token).getClaim(name)
                                .as(aClass);
    }
    
    public static DecodedJWT decodedJWT(String token) throws JWTDecodeException {
        return JWT.decode(token);
    }
    
    private static Date currentTimeToDate(long currentTimeMillis) {
        return currentTimeToDate(currentTimeMillis, 0);
    }
    
    private static Date currentTimeToDate(long currentTimeMillis, long addTime) {
        if (addTime < 0) {
            addTime = 0;
        }
        
        return new Date(currentTimeMillis + (addTime * 60000));
    }
}
