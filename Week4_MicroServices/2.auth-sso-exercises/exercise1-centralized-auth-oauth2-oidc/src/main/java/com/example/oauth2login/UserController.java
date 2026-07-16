package com.example.oauth2login;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * Exposes the authenticated user's info once OAuth2/OIDC login succeeds.
 *
 * NOTE: the original exercise sheet returns the raw java.security.Principal
 * from GET /user. That still works here (kept for fidelity to the sheet),
 * but /user/claims below is the more useful version: it pulls the actual
 * OIDC ID token claims (name, email, sub, etc.) via OidcUser, which a
 * bare Principal does not expose.
 */
@RestController
public class UserController {

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("/user/claims")
    public Map<String, Object> userClaims(OAuth2AuthenticationToken authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        return oidcUser.getClaims();
    }
}
