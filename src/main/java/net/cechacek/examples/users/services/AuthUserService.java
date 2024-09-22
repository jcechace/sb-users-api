package net.cechacek.examples.users.services;

import lombok.RequiredArgsConstructor;
import net.cechacek.examples.users.persistence.access.UserRepository;
import net.cechacek.examples.users.util.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for user authentication and JWT token generation
 *
 * <p>
 * In practice a real authentication protocol (e.g. OAuth2) should be used.
 * Perhaps the only real advantage of such simple use of JWT is that real
 * credentials are not sent with every request.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(userMapper::toAuthModel)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
