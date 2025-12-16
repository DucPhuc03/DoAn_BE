package do_an.traodoido.util;

public class OtpUtil {
    public static String generate() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
