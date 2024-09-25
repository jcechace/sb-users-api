package net.cechacek.examples.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cechacek.examples.users.security.JwtProperties;
import net.cechacek.examples.users.services.domain.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;


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
@Slf4j
public class AuthTokenService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;

    /**
     * Generate JWT token for given authentication.
     * @param authUser authenticated user instance
     * @return encoded JWT token as string
     */
    public String generateJWTToken(AuthUser authUser) {
        log.info("Generating JWT token for {}", authUser.getUsername());
        var now = Instant.now();
        var header = JwsHeader.with(MacAlgorithm.from(jwtProperties.getAlgorithm().getName())).build();

        var claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.getExpiration()))
                .subject(authUser.getUsername())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)). getTokenValue();
    }
}
