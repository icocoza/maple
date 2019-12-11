package com.ccz.modules.controller.user;

import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.domain.constant.EUserAuthType;
import lombok.Data;

@Data
public class Token {
    private String tokenId;
    private String token;

    private String scode;
    private String userId;
    private String uuid;
    private EUserAuthType authType;

    private boolean decrypted = false;

    public Token enc(String scode, String userId, String uuid, EUserAuthType authType) {
        tokenId = StrUtils.getSha1Uuid("tid:");
        token = Crypto.AES256Cipher.getInst().enc(scode+ AsciiSplitter.ASS.UNIT+userId+ AsciiSplitter.ASS.UNIT+uuid+ AsciiSplitter.ASS.UNIT+authType.toString());
        return this;
    }

    public Token dec(String cipher) {
        String decrypted = Crypto.AES256Cipher.getInst().dec(cipher);
        String[] splitted = decrypted.split(AsciiSplitter.ASS.UNIT, -1);
        if(splitted.length<4)
            return this;
        scode = splitted[0];
        userId = splitted[1];
        uuid = splitted[2];
        authType = EUserAuthType.valueOf(splitted[3]);
        this.decrypted = true;
        return this;
    }


}
