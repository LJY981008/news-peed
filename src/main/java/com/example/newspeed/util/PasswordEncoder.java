package com.example.newspeed.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * <p>유저 비밀번호 인코더</p>
 *
 * @author 이현하
 */
@Component
public class PasswordEncoder {

    public String encode(String rawPassword){
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword){
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}