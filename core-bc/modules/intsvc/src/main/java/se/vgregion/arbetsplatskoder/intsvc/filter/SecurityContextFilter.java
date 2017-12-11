package se.vgregion.arbetsplatskoder.intsvc.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import se.vgregion.arbetsplatskoder.service.JwtUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class SecurityContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            SecurityContextHolder.getContext().setAuthentication(
                    new AnonymousAuthenticationToken("anonymous", "anonymous",
                            Collections.singletonList(new SimpleGrantedAuthority("anonymous"))));
        } else {

            String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

            DecodedJWT jwt;
            try {
                jwt = JwtUtil.verify(jwtToken);
            } catch (Exception e) {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            final String subject = jwt.getSubject();
            Claim roles = jwt.getClaim("roles");
            String role = roles.asString();

            GrantedAuthority authority = new SimpleGrantedAuthority(role);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    subject, "", Collections.singletonList(authority)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
