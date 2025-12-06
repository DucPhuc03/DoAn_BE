package do_an.traodoido.config;

import do_an.traodoido.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {
    
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Xử lý authentication khi client connect
            String authToken = extractTokenFromHeaders(accessor);
            if (authToken != null) {
                try {
                    Authentication authentication = authenticateToken(authToken);
                    accessor.setUser(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("WebSocket authenticated user: {}", authentication.getName());
                } catch (Exception e) {
                    log.error("WebSocket authentication failed: {}", e.getMessage());
                }
            }
        } else if (accessor != null && (StompCommand.SEND.equals(accessor.getCommand()) || 
                                       StompCommand.SUBSCRIBE.equals(accessor.getCommand()))) {
            // Xử lý authentication cho mỗi message
            String authToken = extractTokenFromHeaders(accessor);
            if (authToken != null) {
                try {
                    Authentication authentication = authenticateToken(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("WebSocket message authenticated user: {}", authentication.getName());
                } catch (Exception e) {
                    log.error("WebSocket message authentication failed: {}", e.getMessage());
                }
            } else {
                // Nếu không có token trong headers, thử lấy từ user đã authenticated
                Authentication existingAuth = (Authentication) accessor.getUser();
                if (existingAuth != null) {
                    SecurityContextHolder.getContext().setAuthentication(existingAuth);
                }
            }
        }
        
        return message;
    }
    
    private String extractTokenFromHeaders(StompHeaderAccessor accessor) {
        // Tìm token trong STOMP headers
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String authHeader = authHeaders.get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
        }
        return null;
    }
    
    private Authentication authenticateToken(String token) {
        try {
            // Decode JWT token
            Jwt jwt = jwtDecoder.decode(token);
            
            // Lấy username từ JWT subject
            String username = jwt.getSubject();
            if (username == null) {
                throw new JwtException("JWT token does not contain subject");
            }
            
            // Load user từ database
            var user = userRepository.findByUsername(username);
            if (user == null) {
                throw new JwtException("User not found: " + username);
            }
            
            // Tạo Authentication object
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
            );
            
            return authentication;
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw e;
        }
    }
}















