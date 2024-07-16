package com.inorg.rewardAndRecognition.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2Controller {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/oauth2/token")
    public Map<String, String> getAccessToken(@RequestParam String code) {
        var clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        Map<String, String> params = new HashMap<>();
        params.put(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        params.put(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
        params.put(OAuth2ParameterNames.CODE, code);
        params.put(OAuth2ParameterNames.REDIRECT_URI, clientRegistration.getRedirectUri());
        params.put(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
        RestTemplate restTemplate = new RestTemplate();
        var tokenResponse = restTemplate.postForObject(clientRegistration.getProviderDetails().getTokenUri(), params, Map.class);
        String jwtToken = jwtTokenProvider.createToken((String) tokenResponse.get("access_token"), (String) tokenResponse.get("id_token"));
        Map<String, String> response = new HashMap<>();
        response.put("jwtToken", jwtToken);
        return ResponseEntity.ok(response).getBody();
    }
}
