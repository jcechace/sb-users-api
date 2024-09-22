package net.cechacek.examples.users.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cechacek.examples.users.services.AuthTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.PATH)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    public static final String PATH = "/auth";

    private final AuthTokenService authTokenService;

     @PostMapping("/token")
     public String token(Authentication authentication) {
         log.info("Received token request for {}", authentication.getName());
         return authTokenService.generateJWTToken(authentication);
     }
}
