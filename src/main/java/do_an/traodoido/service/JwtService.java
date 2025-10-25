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
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    private Date extractExpiration(String token) throws ParseException, JOSEException {
        return extractClaim(token, JWTClaimsSet::getExpirationTime);
    }
    
    public String extractUsername(String token) {
        try {
            return extractClaim(token, JWTClaimsSet::getSubject);
        } catch (Exception e) {
            return null;
        }
    }
    
    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) throws ParseException, JOSEException {
        final JWTClaimsSet claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private JWTClaimsSet extractAllClaims(String token) throws ParseException, JOSEException {
        // Parse the JWT
        SignedJWT signedJWT = SignedJWT.parse(token);
        
        // Create verifier
        MACVerifier verifier = new MACVerifier(secretKey);
        
        // Verify the signature
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid JWT signature");
        }
        
        return signedJWT.getJWTClaimsSet();
    }
}
