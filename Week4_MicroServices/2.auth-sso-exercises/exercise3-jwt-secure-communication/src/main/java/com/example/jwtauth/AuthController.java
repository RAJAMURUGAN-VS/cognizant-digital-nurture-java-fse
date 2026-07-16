package com.example.jwtauth;

import com.example.jwtauth.security.JwtTokenProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Demo login endpoint. Accepts any non-blank username/password and
 * issues a signed JWT — this is not the exercise sheet's original code
 * (the sheet never showed how a token actually gets minted for a real
 * login), but it's the missing piece needed to exercise
 * JwtTokenProvider/JwtTokenFilter end-to-end.
 *
 * Replace the credential check below with a real
 * AuthenticationManager / UserDetailsService lookup before using this
 * anywhere beyond local experimentation.
 */
@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public record LoginRequest(String username, String password) {}

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        if (request.username() == null || request.username().isBlank()
                || request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("username and password are required");
        }

        String token = jwtTokenProvider.createToken(request.username());
        return Map.of("token", token);
    }
}
