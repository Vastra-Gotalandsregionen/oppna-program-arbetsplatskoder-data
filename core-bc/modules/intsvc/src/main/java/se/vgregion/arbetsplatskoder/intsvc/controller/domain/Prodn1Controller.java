package se.vgregion.arbetsplatskoder.intsvc.controller.domain;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.repository.Prodn1Repository;
import se.vgregion.arbetsplatskoder.repository.UserRepository;
import se.vgregion.arbetsplatskoder.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/prodn1")
public class Prodn1Controller {

    @Autowired
    private Prodn1Repository prodn1Repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Prodn1>> getProdn1s() {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

        DecodedJWT jwt;
        try {
            jwt = JwtUtil.verify(jwtToken);

            String userId = jwt.getSubject();

            User user = userRepository.findOne(userId);

            if (Role.ADMIN.equals(user.getRole())) {
                return ResponseEntity.ok(prodn1Repository.findAllByOrderByForetagsnamn());
            }

            return ResponseEntity.ok(new ArrayList<>(user.getProdn1s()));

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = "/{producentid}", method = RequestMethod.GET)
    @ResponseBody
    public Prodn1 getProdn1(@PathVariable(value = "producentid", required = true) String producentid) {
        return prodn1Repository.findProdn1ByProducentidEquals(producentid);
    }
}
