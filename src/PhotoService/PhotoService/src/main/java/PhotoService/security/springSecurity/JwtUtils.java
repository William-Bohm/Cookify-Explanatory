package videoStream.security.springSecurity;


import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;


import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.public-key}")
    private String publicKeyPath;

    // Retrieves the public key used for token verification

    private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
        String key = new String(Files.readAllBytes(Paths.get(getClass().getResource(publicKeyPath).toURI())));

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedPublicKey = Base64.getMimeDecoder().decode(publicKeyPEM);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    // Validates the JWT token and returns the claims if valid
    public Claims validateToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token);
            return jws.getBody();
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            return null;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return null;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            return null;
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            return null;
        } catch (IllegalArgumentException | IOException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            return null;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    // Checks if the JWT token has expired
    public boolean isTokenExpired(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    // Extracts the expiration date from the JWT token
    private Date extractExpiration(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
        return extractClaims(token).getExpiration();
    }

    // Extracts the claims from the JWT token
    private Claims extractClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, URISyntaxException {
        return Jwts.parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
