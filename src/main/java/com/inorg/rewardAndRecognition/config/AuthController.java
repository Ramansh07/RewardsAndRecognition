package com.inorg.rewardAndRecognition.config;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public AuthController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/auth/google")
    public Map<String, String> getGoogleAuthUrl() throws URISyntaxException {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("google");
        String authorizationUri = registration.getProviderDetails().getAuthorizationUri();
        String clientId = registration.getClientId();
        String redirectUri = registration.getRedirectUri();
        String scope = "openid%20profile%20email";
        String state = UUID.randomUUID().toString();

        URI uri = new URI(String.format(
                "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s",
                authorizationUri,
                clientId,
                redirectUri,
                scope,
                state
        ));

        Map<String, String> response = new HashMap<>();
        response.put("authUrl", uri.toString());
        response.put("state", state);
        return response;
    }
}

