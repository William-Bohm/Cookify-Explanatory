package videoStream.security.springSecurity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    // Performs the JWT authentication and authorization for each request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Skip JWT authentication for specific endpoints
        if ("/api/public/test".equals(requestURI) || "/cookify/user-service/register".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extracts the JWT token from the request
        String jwt = getJwtFromRequest(request);
        try {
            // Validates the JWT token
            if (jwt != null && jwtUtils.validateToken(jwt) != null) {
                UserDetails userDetails;
                try {
                    // Load the user details based on the token
                    userDetails = jwtUserDetailsService.loadUserByUsername(jwt);
                } catch (UsernameNotFoundException e) {
                    log.error("Invalid token", e);
                    filterChain.doFilter(request, response);
                    return;
                }

                // Creates an authentication object using the user details
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Sets the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }

    // Retrieves the JWT token from the request's Authorization header
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
