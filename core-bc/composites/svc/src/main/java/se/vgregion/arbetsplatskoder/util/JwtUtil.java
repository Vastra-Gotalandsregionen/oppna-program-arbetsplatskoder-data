package se.vgregion.arbetsplatskoder.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Patrik Björk
 */
public class JwtUtil {

    private static final String secret = UUID.randomUUID().toString();
    private static int MINUTES_AGE = 30;

    public static String createToken(String userId, String displayName, String role, Set<Prodn1> prodn1s) {
        try {
            Date timeAhead = Date.from(Instant.now().plus(MINUTES_AGE, ChronoUnit.MINUTES));
            Date now = Date.from(Instant.now());

            String[] prodn1sStrings = prodn1s.stream()
                    .map(prodn1 -> prodn1.getProducentid())
                    .collect(Collectors.toList()).toArray(new String[]{});

            return JWT.create()
                    .withSubject(userId != null ? String.valueOf(userId) : null)
                    .withClaim("roles", role)
                    .withClaim("displayName", displayName)
                    .withArrayClaim("prodn1s", prodn1sStrings)
                    .withIssuedAt(now)
                    .withExpiresAt(timeAhead)
                    .sign(Algorithm.HMAC256(secret));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static DecodedJWT verify(String jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            return verifier.verify(jwtToken);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
