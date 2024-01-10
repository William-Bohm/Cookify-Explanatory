package videoStream.security.springSecurity;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private JwtUtils jwtUtils;

    // Loads the user details based on the provided JWT token
    @Override
    public UserDetails loadUserByUsername(String jwt) throws UsernameNotFoundException {
        try {
            // Check if the token has expired
            if (jwtUtils.isTokenExpired(jwt)) {
                throw new UsernameNotFoundException("Token is expired");
            }

            // Validate the token and retrieve the claims
            Claims claims = jwtUtils.validateToken(jwt);
            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            List<String> roles = claims.get("roles", List.class);

            // Map the roles to SimpleGrantedAuthority objects
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            // Return the CustomUserDetails object containing the user details
            return new CustomUserDetails(username, userId, authorities);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
