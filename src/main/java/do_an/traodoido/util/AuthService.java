package do_an.traodoido.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import do_an.traodoido.dto.request.LoginRequest;
import do_an.traodoido.dto.request.RegisterRequest;
import do_an.traodoido.dto.response.LoginResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.UserLogin;
import do_an.traodoido.entity.User;
import do_an.traodoido.enums.UserStatus;
import do_an.traodoido.exception.UsernameAlreadyExistsException;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${google.client-id}")
    private String clientId;

   @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.client-redirect-uri}")
    private String redirectUri;

    public LoginResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);
        User user = userRepository.findByUsername(loginRequest.getUsername());
        String accessToken = jwtService.generateToken(user);

        UserLogin userLogin = UserLogin.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();
        return LoginResponse.builder()
                .accessToken(accessToken)
                .user(userLogin)
                .build();
    }
    
    public RestResponse<Void> register(RegisterRequest registerRequest) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username '" + registerRequest.getUsername() + "' đã tồn tại. Vui lòng chọn username khác.");
        }
        
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(LocalDate.now())
                .role("USER")
                .status(UserStatus.ACTIVE)
                .build();
        
        userRepository.save(user);
        
        return RestResponse.<Void>builder()
                .code(1000)
                .message("User registered successfully")
                .data(null)
                .build();
    }
    public LoginResponse oauthLogin(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        // 1. Đổi code lấy access_token
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> tokenRequest =
                new HttpEntity<>(body, tokenHeaders);

        String TOKEN_URL = "https://oauth2.googleapis.com/token";

        ResponseEntity<String> tokenResponse =
                restTemplate.postForEntity(TOKEN_URL, tokenRequest, String.class);

        JsonNode tokenJson = mapper.readTree(tokenResponse.getBody());
        String accessTokenGoogle = tokenJson.get("access_token").asText();

        // 2. Dùng access_token gọi Google UserInfo
        String USERINFO_URL = "https://openidconnect.googleapis.com/v1/userinfo";

        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessTokenGoogle); // Authorization: Bearer <access_token>

        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                USERINFO_URL,
                HttpMethod.GET,
                userInfoRequest,
                String.class
        );
        JsonNode userInfoJson = mapper.readTree(userInfoResponse.getBody());

        String email = userInfoJson.get("email").asText();
        String name = userInfoJson.path("name").asText("");
        String picture = userInfoJson.path("picture").asText("");

        User user = userRepository.findByEmail(email);
        if(user == null){
            User userGoogle = User.builder()
                    .email(email)
                    .fullName(name)
                    .avatarUrl(picture)
                    .username(email.split("@")[0])
                    .password(passwordEncoder.encode("oauth2user"))
                    .createdAt(LocalDate.now())
                    .role("USER")
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepository.save(userGoogle);
            String accessToken = jwtService.generateToken(userGoogle);
            UserLogin userLogin = UserLogin.builder()
                    .id(userGoogle.getId())
                    .name(userGoogle.getFullName())
                    .email(userGoogle.getEmail())
                    .avatarUrl(userGoogle.getAvatarUrl())
                    .role(userGoogle.getRole())
                    .build();
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .user(userLogin)
                    .build();
        }
        else {
            String accessToken = jwtService.generateToken(user);
            UserLogin userLogin = UserLogin.builder()
                    .id(user.getId())
                    .name(user.getFullName())
                    .email(user.getEmail())
                    .avatarUrl(user.getAvatarUrl())
                    .role(user.getRole())
                    .build();
            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .user(userLogin)
                    .build();
        }


    }

    public String resetPassword(String email,String password){
        User user=userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "Đặt lại mật khẩu thành công";
    }

}
