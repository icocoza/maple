package com.ccz.modules.controller.user;

import com.ccz.modules.common.utils.AsciiSplitter.ASS;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.domain.constant.EUserAuthType;
import com.ccz.modules.domain.constant.EUserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class AdminToken {
    private String uid;
    private EUserRole userRole;
    private String userName;
    private LocalDateTime expireDateTime;

    private boolean decrypted = false;
    private int EXPIRE_DAYS = 30;

    public String enc(String uid, EUserRole userRole, String userName) {
        LocalDateTime now = LocalDateTime.now();
        String expireDt = now.plusDays(EXPIRE_DAYS).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return Crypto.BlowFish.getInst().enc(uid + ASS.UNIT + userRole + ASS.UNIT +
                userName + ASS.UNIT + expireDt);
    }

    public AdminToken dec(String cipher) {
        String decrypted = Crypto.BlowFish.getInst().dec(cipher);
        String[] splitted = decrypted.split(ASS.UNIT, -1);
        if(splitted.length<5)
            return this;
        uid = splitted[0];
        userRole = EUserRole.getType(splitted[1]);
        userName = splitted[2];
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
