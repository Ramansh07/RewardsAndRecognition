package com.inorg.rewardAndRecognition.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser.*;

import javax.crypto.spec.SecretKeySpec;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public String createToken(String accessToken, String idToken) {
        try {
            Date now = new Date();
            Date validity = new Date(now.getTime() + validityInMilliseconds);
            SignedJWT signedJWTT = SignedJWT.parse(idToken);
            JWTClaimsSet claimsSett = signedJWTT.getJWTClaimsSet();

            JWSSigner signer = new MACSigner(secretKey.getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(claimsSett.getStringClaim("email"))
                    .claim("accessToken", accessToken)
                    .claim("idToken", idToken)
                    .issueTime(now)
                    .expirationTime(validity)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);

            signedJWT.sign(signer);

            String jwtToken = signedJWT.serialize();
            System.out.println("Generated JWT Token: " + jwtToken + "\n\n");

            return jwtToken;
        } catch (Exception e) {
            System.out.println("Exception occurred during token creation: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            throw new RuntimeException("Failed to create JWT token", e); // or handle accordingly
        }
    }


    public boolean validateToken(String token) {
        try {
            System.out.println("\n\n"+token+"\n\n");
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("\n\nclaims"+claims+"\n\n");

            // Check token expiration
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAccessToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("accessToken", String.class);
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String

    resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println("token:" + bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }
}
