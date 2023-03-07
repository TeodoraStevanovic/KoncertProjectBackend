package com.java.koncert.jwt;

import com.java.koncert.exception.MyOwnException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 120*24*60*60;
    Logger logger =  LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (MalformedJwtException e){
            logger.warn("ID token in wrong format!-MalformedJwtException");
            throw new MyOwnException(e.getMessage());
        }catch (SignatureException e){
            logger.warn("ID token signature is not correct!");
            throw new MyOwnException(e.getMessage());
        }
        catch (ExpiredJwtException e){
            logger.warn("Expired jwt token!");
            throw new MyOwnException(e.getMessage());
        }
        catch (MissingClaimException e){
            logger.warn("Missing claim exception!");
            throw new MyOwnException(e.getMessage());
        }
        catch (IncorrectClaimException e){
            logger.warn("Incorrected claim!");
            throw new MyOwnException(e.getMessage());
        }
        catch (UnsupportedJwtException e){
            logger.warn("Unsupported jwt!");
            throw new MyOwnException(e.getMessage());
        }
        catch (IllegalArgumentException e){
            logger.warn("Illegal argument!");
            throw new MyOwnException(e.getMessage());
        }
        catch (Exception e){
            logger.warn("Unknown error!");
            throw new MyOwnException(e.getMessage());
        }
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        //token pravi od email-a
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS256 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
//moram da smislim kako da sacuvam secret key
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));}



}
