package videoStream.security.springSecurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

// CustomUserDetails constructor that accepts the username, userId, and authorities
public class CustomUserDetails extends User {
    private final Long userId;

    public CustomUserDetails(String username, Long userId, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
        this.userId = userId;
    }

    // Getter method for retrieving the userId
    public Long getUserId() {
        return userId;
    }
}
