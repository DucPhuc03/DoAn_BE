package do_an.traodoido.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import do_an.traodoido.dto.request.TokenDTO;
import do_an.traodoido.entity.User;
import do_an.traodoido.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    private UserRepository userRepository;
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    public String generateToken(User user) {

        TokenDTO tokenDTO= TokenDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        Date issueAt = new Date();
        Date expiration = new Date(issueAt.getTime() + jwtExpiration);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(issueAt)
                .claim("user", tokenDTO)
                .expirationTime(expiration)
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException("Error signing JWT token", e);
        }
        return jwsObject.serialize();
    }
    
    public Long extractUserId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(secretKey);
            
            if (!signedJWT.verify(verifier)) {
                return null;
            }
            
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            Map<String, Object> userClaim = (Map<String, Object>) claims.getClaim("user");
            if (userClaim != null && userClaim.containsKey("id")) {
                return Long.valueOf(userClaim.get("id").toString());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
