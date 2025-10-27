package do_an.traodoido.controller;

import do_an.traodoido.dto.request.LoginRequest;
import do_an.traodoido.dto.request.RegisterRequest;
import do_an.traodoido.dto.response.LoginResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);

        ResponseCookie responseCookie=ResponseCookie.from("access_token",response.getAccessToken())
                .httpOnly(true)
                .path("/")
                .secure(true)
                .maxAge(jwtExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<RestResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RestResponse<Void> response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}
