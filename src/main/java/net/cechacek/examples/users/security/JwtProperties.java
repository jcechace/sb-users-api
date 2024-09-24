package net.cechacek.examples.users.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.time.Duration;

/**
 * Configuration properties for JWT.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotNull
    private SecretKey secret;

    @NotNull
    private JWSAlgorithm algorithm;

    @NotEmpty
    private String issuer;

    @DurationMin(seconds = 1)
    private Duration expiration;

    public void setAlgorithm(@NotNull String algorithm) {
        this.algorithm = JWSAlgorithm.parse(algorithm);
    }

    public void setSecret(@NotNull String secret) {
        var jwk = new OctetSequenceKey.Builder(secret.getBytes())
                .algorithm(algorithm)
                .build();
        this.secret = jwk.toSecretKey();
    }
}

