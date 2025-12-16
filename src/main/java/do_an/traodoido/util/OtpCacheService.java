package do_an.traodoido.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OtpCacheService {

    private final Cache<String, String> cache =
            Caffeine.newBuilder()
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .maximumSize(10_000)
                    .build();

    // Chuẩn hoá key
    private String key(String email) {
        return email.trim().toLowerCase();
    }

    public void save(String email, String otp) {
        cache.put(key(email), otp);

    }

    public boolean verify(String email, String otp) {
        String k = key(email);
        String cachedOtp = cache.getIfPresent(k);



        if (cachedOtp != null && cachedOtp.equals(otp)) {
            cache.invalidate(k); // 🔥 xoá OTP sau khi dùng
            return true;
        }
        return false;
    }

    public void clear(String email) {
        cache.invalidate(key(email));
    }
}

