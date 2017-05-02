package se.vgregion.arbetsplatskoder.intsvc.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.json.LoginRequest;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.service.LdapLoginService;
import se.vgregion.arbetsplatskoder.util.JwtUtil;

import javax.security.auth.login.FailedLoginException;

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

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = ldapLoginService.login(loginRequest.getUsername(), loginRequest.getPassword());

            String token = JwtUtil.createToken(user.getId(), user.getDisplayName(), user.getRole().name(),
                    user.getProdn1s());

            return ResponseEntity.ok(token);
        } catch (FailedLoginException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/renew", method = RequestMethod.POST)
    public ResponseEntity<String> renewJwt(@RequestBody String jwt) {

            DecodedJWT decodedJWT = JwtUtil.verify(jwt);

            User user = userRepository.findOne(decodedJWT.getSubject());

            String token = JwtUtil.createToken(user.getId(), user.getDisplayName(), user.getRole().name(),
                    user.getProdn1s());

            return ResponseEntity.ok(token);
    }
}
