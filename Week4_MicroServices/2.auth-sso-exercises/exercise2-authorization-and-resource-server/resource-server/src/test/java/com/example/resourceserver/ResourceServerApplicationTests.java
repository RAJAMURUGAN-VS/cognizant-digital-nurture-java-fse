package com.example.resourceserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * NOTE: JwtDecoder is mocked out here so the Spring context can start in
 * tests without reaching out over the network to a real running
 * authorization-server to fetch its issuer metadata / JWK set.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ResourceServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void unauthenticatedRequestToSecureIsRejected() throws Exception {
        mockMvc.perform(get("/secure"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validJwtCanAccessSecureEndpoint() throws Exception {
        mockMvc.perform(get("/secure").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string("This is a secure endpoint"));
    }
}
