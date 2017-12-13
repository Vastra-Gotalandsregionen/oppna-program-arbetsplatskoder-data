package se.vgregion.arbetsplatskoder.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private static PasswordEncoder instance = new PasswordEncoder();

    private PasswordEncoder() {

    }

    public static PasswordEncoder getInstance() {
        return instance;
    }

    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    public boolean matches(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }

    public static void main(String[] args) {
        System.out.println(instance.encodePassword(args[0]));
    }
}
