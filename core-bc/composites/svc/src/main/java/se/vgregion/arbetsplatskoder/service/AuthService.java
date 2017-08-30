package se.vgregion.arbetsplatskoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import se.vgregion.arbetsplatskoder.domain.jpa.Role;
import se.vgregion.arbetsplatskoder.domain.jpa.User;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Prodn1;
import se.vgregion.arbetsplatskoder.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasRole(Authentication authentication, String role) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(role);
    }

    public boolean hasProdn1Access(Authentication authentication, Prodn1 prodn1) {
        User user = userRepository.findOne((String) authentication.getPrincipal());

        return Role.ADMIN.equals(user.getRole()) || user.getProdn1s().contains(prodn1);
    }
}
