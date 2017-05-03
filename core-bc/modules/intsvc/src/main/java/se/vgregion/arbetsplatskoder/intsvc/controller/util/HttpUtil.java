package se.vgregion.arbetsplatskoder.intsvc.controller.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import se.vgregion.arbetsplatskoder.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Patrik Björk
 */
public class HttpUtil {

    public static String getUserIdFromRequest(HttpServletRequest request) {
        String userId;

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            userId = null;
        } else {

            String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

            DecodedJWT jwt;
            jwt = JwtUtil.verify(jwtToken);

            userId = jwt.getSubject();
        }
        return userId;
    }
}
