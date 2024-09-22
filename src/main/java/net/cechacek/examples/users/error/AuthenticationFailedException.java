package net.cechacek.examples.users.error;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {
    public AuthenticationFailedException(String msg) {
        super(msg);
    }

    public AuthenticationFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
