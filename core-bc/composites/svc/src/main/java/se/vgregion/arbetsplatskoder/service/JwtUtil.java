package se.vgregion.arbetsplatskoder.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Patrik Björk
 */
@Service
public class JwtUtil {

    private static String secret;

    @Value("${jwt.sign.secret}")
    private String jwtSignSecret;

    private static int MINUTES_AGE = 30;

    @PostConstruct
    public void init() {
        secret = jwtSignSecret;
    }

    public static String createToken(String userId, String displayName, String[] roles, Set<Prodn1> prodn1s) {
        try {
            Date timeAhead = Date.from(Instant.now().plus(MINUTES_AGE, ChronoUnit.MINUTES));
            Date now = Date.from(Instant.now());

            Integer[] prodn1sStrings = prodn1s.stream()
                    .map(Prodn1::getId)
                    .collect(Collectors.toList()).toArray(new Integer[]{});

            return JWT.create()
                    .withSubject(userId != null ? String.valueOf(userId) : null)
                    .withArrayClaim("roles", roles)
                    .withClaim("displayName", displayName)
                    .withArrayClaim("prodn1s", prodn1sStrings)
                    .withIssuedAt(now)
                    .withExpiresAt(timeAhead)
                    .sign(Algorithm.HMAC256(secret));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DecodedJWT verify(String jwtToken) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        return verifier.verify(jwtToken);
    }
}
