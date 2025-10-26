package do_an.traodoido.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import do_an.traodoido.dto.request.TokenDTO;
import do_an.traodoido.entity.User;
import do_an.traodoido.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    private UserRepository userRepository;
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    public String generateToken(String username) {
        User user = userRepository.findByUsername(username);
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
                .subject(username)
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

}
