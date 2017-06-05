package se.vgregion.arbetsplatskoder.intsvc.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.json.LoginRequest;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.service.JwtUtil;
import se.vgregion.arbetsplatskoder.service.LdapLoginService;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Patrik Björk
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LdapLoginService ldapLoginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = null;
            if (request.getHeader("iv-user") != null) { // TODO Remove before production.
                user = ldapLoginService.loginOffline(request.getHeader("iv-user"));
            } else {
                user = ldapLoginService.login(loginRequest.getUsername(), loginRequest.getPassword());
                if (user.getInactivated() != null && user.getInactivated()) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            String token = JwtUtil.createToken(user.getId(), user.getDisplayName(), user.getRole().name(),
                    user.getProdn1s());

            return ResponseEntity.ok(token);
        } catch (FailedLoginException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/renew", method = RequestMethod.POST)
    public ResponseEntity<String> renewJwt(@RequestBody String jwt) {

        try {
            DecodedJWT decodedJWT = JwtUtil.verify(jwt);

            User user = userRepository.findOne(decodedJWT.getSubject());

            String token = JwtUtil.createToken(user.getId(), user.getDisplayName(), user.getRole().name(),
                    user.getProdn1s());

            return ResponseEntity.ok(token);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @RequestMapping(value = "/impersonate", method = RequestMethod.POST)
    public ResponseEntity<String> impersonate(@RequestBody User userToImpersonate) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

        DecodedJWT jwt;
        try {
            jwt = JwtUtil.verify(jwtToken);

            String role = jwt.getClaim("roles").asString();

            if (Role.ADMIN.name().equals(role)) {
                User impersonated = ldapLoginService.loginWithoutPassword(userToImpersonate.getId());

                String token = JwtUtil.createToken(impersonated.getId(), impersonated.getDisplayName(),
                        impersonated.getRole().name(), impersonated.getProdn1s());

                return ResponseEntity.ok(token);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (FailedLoginException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
