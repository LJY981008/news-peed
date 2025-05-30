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

    /**
     * <p>비밀번호 암호화</p>
     *
     * @param rawPassword 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encode(String rawPassword){
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    /**
     * <p>평문 비밀번호와 저장된 비밀번호 비교 검증</p>
     *
     * @param rawPassword 평문 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return boolean
     */
    public boolean matches(String rawPassword, String encodedPassword){
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}