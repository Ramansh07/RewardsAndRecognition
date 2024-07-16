package com.inorg.rewardAndRecognition.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import java.text.ParseException;
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
                    .issueTime(now)
                    .expirationTime(validity)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            throw new RuntimeException("Failed to create JWT token", e); // or handle accordingly
        }
    }

public boolean validateToken(String token) {
    try {

        SignedJWT signedJWT = SignedJWT.parse(token);

        byte[] secretKeyBytes = secretKey.getBytes();
        JWSVerifier verifier = new MACVerifier(secretKeyBytes);

        if (!signedJWT.verify(verifier)) {
            return false;
        }
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiration != null && expiration.before(new Date())) {
            return false;
        }
        return true;
    } catch (ParseException | JOSEException e) {
        return false;
    }
}

    public String getAccessToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("accessToken", String.class);
    }

public String getUsername(String token) {
    try {

        SignedJWT signedJWT = SignedJWT.parse(token);

        byte[] secretKeyBytes = secretKey.getBytes();
        JWSVerifier verifier = new MACVerifier(secretKeyBytes);

        if (!signedJWT.verify(verifier)) {
            return null;
        }
        return signedJWT.getJWTClaimsSet().getSubject();

    } catch (ParseException | JOSEException e) {
        return null;
    }
}

    public String

    resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
