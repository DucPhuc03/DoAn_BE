package do_an.traodoido.service;

import do_an.traodoido.dto.request.LoginRequest;
import do_an.traodoido.dto.request.RegisterRequest;
import do_an.traodoido.dto.response.LoginResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.UsernameAlreadyExistsException;
import do_an.traodoido.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public LoginResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtService.generateToken(loginRequest.getUsername());
        
        return LoginResponse.builder()
                .accessToken(accessToken)
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
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role("USER")
                .build();
        
        userRepository.save(user);
        
        return RestResponse.<Void>builder()
                .code(1000)
                .message("User registered successfully")
                .data(null)
                .build();
    }
}
