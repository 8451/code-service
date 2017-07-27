package com.e451.rest.security;

import com.e451.rest.domains.user.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by j747951 on 6/8/2017.
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";


    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private String secret;
    private Long expiration;
    private String publicKeyEncoded;
    private String privateKeyEncoded;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") Long expiration,
                        @Value("${jwt.public_key}") String publicKeyEncoded,
                        @Value("${jwt.private_key}") String privateKeyEncoded) {
        this.secret = secret;
        this.expiration = expiration;
        this.publicKeyEncoded = publicKeyEncoded;
        this.privateKeyEncoded = privateKeyEncoded;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            JwtParser parser = Jwts.parser();

            if(privateKeyEncoded == null || publicKeyEncoded == null) {
                parser.setSigningKey("secret");
            } else {
                parser.setSigningKey(getPublicKey());
            }

            claims = parser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        try {

            JwtBuilder builder = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(generateExpirationDate());

            if(privateKeyEncoded == null || publicKeyEncoded == null) {
                builder.signWith(SignatureAlgorithm.HS256, "secret");
            } else {
                builder.signWith(SignatureAlgorithm.RS256, getPrivateKey());
            }

            return builder.compact();

        } catch (Exception e) {
            logger.error("Error generating token", e);
            return null;
        }
    }

    public Boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token));
    }

    private PublicKey getPublicKey() throws Exception{
        if(publicKey == null) {
            try {
                byte[] publicKeyBytes = publicKeyEncoded.getBytes();
                byte[] publicKeyDecoded = java.util.Base64.getDecoder().decode(publicKeyBytes);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyDecoded);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                publicKey = factory.generatePublic(spec);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                logger.error("Error in public key algorithm or spec", e);
            }
        }
        return publicKey;
    }

    private Key getPrivateKey() throws Exception {
        if (privateKey == null) {
            try {
                byte[] privateKeyBytes = privateKeyEncoded.getBytes();
                byte[] privateKeyDecoded = java.util.Base64.getDecoder().decode(privateKeyBytes);
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyDecoded);
                KeyFactory factory = KeyFactory.getInstance("RSA");
                privateKey = factory.generatePrivate(spec);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                logger.error("Error in private key algorithm or spec", e);
            }
        }
        return privateKey;
    }
}