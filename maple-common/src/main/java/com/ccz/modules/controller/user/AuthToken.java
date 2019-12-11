package com.ccz.modules.controller.user;

import com.ccz.modules.common.utils.AsciiSplitter.ASS;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.domain.constant.EUserAuthType;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class AuthToken {
    private String scode;
    private String userId;
    private String uuid;
    private EUserAuthType authType;
    private LocalDateTime expireDateTime;

    private boolean decrypted = false;
    private int EXPIRE_DAYS = 30;

    public String enc(String scode, String userId, String uuid, EUserAuthType authType) {
        LocalDateTime now = LocalDateTime.now();
        String expireDt = now.plusDays(EXPIRE_DAYS).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return Crypto.BlowFish.getInst().enc(scode + ASS.UNIT + userId + ASS.UNIT +
                uuid + ASS.UNIT + authType.toString() + ASS.UNIT + expireDt);
    }

    public AuthToken dec(String cipher) {
        String decrypted = Crypto.BlowFish.getInst().dec(cipher);
        String[] splitted = decrypted.split(ASS.UNIT, -1);
        if(splitted.length<5)
            return this;
        scode = splitted[0];
        userId = splitted[1];
        uuid = splitted[2];
        authType = EUserAuthType.valueOf(splitted[3]);
        expireDateTime = LocalDateTime.parse(splitted[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.decrypted = true;
        return this;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireDateTime);
    }

    public boolean isUseless() {
        return LocalDateTime.now().plusMinutes(5).isAfter(expireDateTime);
    }


}
