package do_an.traodoido.controller;

import do_an.traodoido.dto.request.LoginRequest;
import do_an.traodoido.dto.request.RegisterRequest;
import do_an.traodoido.dto.response.LoginResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.util.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "API quản lý xác thực người dùng (đăng nhập, đăng ký)")
public class AuthController {
    
    private final AuthService authService;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    @Operation(summary = "Đăng nhập", description = "Xác thực người dùng và trả về JWT token. Token được lưu trong cookie httpOnly.")
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
    
    @Operation(summary = "Đăng ký tài khoản mới", description = "Tạo tài khoản người dùng mới với email và mật khẩu")
    @PostMapping("/register")
    public ResponseEntity<RestResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RestResponse<Void> response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}
