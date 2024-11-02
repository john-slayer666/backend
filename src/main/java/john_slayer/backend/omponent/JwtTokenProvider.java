package john_slayer.backend.omponent;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

import java.util.Base64;
import java.security.SecureRandom;

@Component
public class JwtTokenProvider {
    private final String SECRET_KEY;

    public JwtTokenProvider() {
        //! Генерируем безопасный ключ длиной 64 байта и кодируем его в Base64
        byte[] keyBytes = new byte[64];
        new SecureRandom().nextBytes(keyBytes);
        this.SECRET_KEY = Base64.getEncoder().encodeToString(keyBytes);
    }

    public String generateToken(String userId) {
        //? 24 часа в миллисекундах
        long EXPIRATION_TIME = 86400000;
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

}

