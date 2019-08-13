package com.ccz.modules.controller.user;

import com.ccz.modules.common.form.FormCommon;
import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.domain.constant.EUserAuthType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Deprecated
public class UserForms {

    public String getString(JsonNode jNode, String key) {
        if( jNode.has(key) )
            return jNode.get(key).asText();
        return null;
    }

    public double getDouble(JsonNode jNode, String key) {
        if( jNode.has(key) )
            return jNode.get(key).asDouble();
        return -1;
    }

    @Getter
    public class FormUserName extends FormCommon {
        public String userName;

        public FormUserName(JsonNode jNode) {
            super(jNode);
            this.userName = getString(jNode, "userName");
        }
    }

    @Getter
    public class FormRegisterUser extends FormCommon {
        private String uuid;
        private String userName;
        protected EUserAuthType authtype = EUserAuthType.none;

        public FormRegisterUser(JsonNode jNode) {
            super(jNode);
            this.uuid = getString(jNode, "uuid");
            this.userName = getString(jNode, "userName");
        }

        public FormRegisterUser(String uuid, String userName, FormCommon data) {	//for anonymmous, created manually
            super(data);
            this.uuid = uuid;
            this.userName = userName;
        }

        public boolean isAnonymous() {
            return authtype == EUserAuthType.none;
        }
    }

    @Getter
    public class FormRegisterIdPw extends FormRegisterUser {
        public String userId;
        public String pw;

        public FormRegisterIdPw(JsonNode jNode) {
            super(jNode);
            super.authtype = EUserAuthType.idpw;
            this.userId = getString(jNode, "userId");
            this.pw = getString(jNode, "pw");
        }
    }

    @Getter
    public class FormRegisterEmail extends FormRegisterUser {
        public String email;

        public FormRegisterEmail(JsonNode jNode) {
            super(jNode);
            super.authtype = EUserAuthType.email;
            this.email = getString(jNode, "email");
        }
    }

    @Getter
    public class FormRegisterPhone extends FormRegisterUser {
        public String phone;	//for user authentication

        public FormRegisterPhone(JsonNode jNode) {
            super(jNode);
            this.authtype = EUserAuthType.phone;
            this.phone = getString(jNode, "phone");
        }
    }

    @Getter
    public class FormLogin extends FormSignIn {
        private String userName;
        private String pw;
        private String userType;
        private String osType;
        private String osVersion;
        private String appVersion;
        private String epid;

        public FormLogin(JsonNode jNode) {
            super(jNode);
            this.userName = getString(jNode, "userName");
            this.pw = getString(jNode, "pw");
            this.userType = getString(jNode, "userType");
            this.osType = getString(jNode, "osType");
            this.osVersion = getString(jNode, "osVersion");
            this.appVersion = getString(jNode, "appVersion");
            this.epid = getString(jNode, "epid");
        }

        public boolean isValidIdPw() {
            if(userName==null || userName.length()<8 || pw==null || pw.length()<6 )
                return false;
            return true;
        }
    }

    @Getter
    public class FormAnonyLogin extends FormLogin {
        private String uuid;

        public FormAnonyLogin(JsonNode jNode) {
            super(jNode);
            this.uuid = getString(jNode, "uuid");
        }
    }

    @Getter
    public class FormAnonyLoginGps extends FormAnonyLogin {
        private String buildId;
        private double lon;
        private double lat;

        public FormAnonyLoginGps(JsonNode jNode) {
            super(jNode);
            this.buildId = getString(jNode, "buildId");
            this.lon = getDouble(jNode, "lon");
            this.lat = getDouble(jNode, "lat");
        }
    }

    @Getter
    public class FormSignIn extends FormCommon {
        private String regToken;
        private String tokenId, uuid;

        private String tokenUserid, tokenUuid;//by regToken
        private  EUserAuthType tokenAuthType = EUserAuthType.none;//by regToken

        public FormSignIn(JsonNode jNode) {
            super(jNode);
            this.regToken = getString(jNode, "regToken");
            this.tokenId = getString(jNode, "tokenId");
            this.uuid = getString(jNode, "uuid");
            decodeRegToken();
        }

        private void decodeRegToken() {
            if(this.regToken==null)
                return;
            String dec = Crypto.AES256Cipher.getInst().dec(regToken);
            String[] chunk = dec.split(AsciiSplitter.ASS.UNIT);
            tokenUserid = chunk[0];
            tokenUuid = chunk[1];
            tokenAuthType = EUserAuthType.getType(chunk[2]);
        }

        public boolean isValidUserToken() {
            if(tokenUserid==null || tokenUserid.length()<1 || tokenUuid == null || tokenUuid.length()<1 || tokenAuthType == null)
                return false;
            return true;
        }

        public boolean isValidUuid() {
            return true; //this.uuid.equals(this.tokenUuid);
        }
    }

    public class FormAnonySignIn extends FormCommon {

        public FormAnonySignIn(JsonNode jNode) {
            super(jNode);
        }

    }

    @Getter
    private class FormUpdateUser extends FormCommon {
        protected String uuid, osType, osVersion, appVersion;
        protected EUserAuthType authtype = EUserAuthType.none;

        public FormUpdateUser(JsonNode jNode) {
            super(jNode);
            this.uuid = getString(jNode, "uuid");
            this.osType = getString(jNode, "osType");
            this.osVersion = getString(jNode, "osVersion");
            this.appVersion = getString(jNode, "appVersion");
//			this.inappcode = getString(jNode, "inappcode");
        }
    }

    public class FormUpdateIdUser extends FormRegisterIdPw {
        @Getter
        private String newpw;

        public FormUpdateIdUser(JsonNode jNode) {
            super(jNode);
            newpw = getString(jNode, "newpw");
        }

    }

    public class FormUpdateEmailUser extends FormRegisterEmail {
        public FormUpdateEmailUser(JsonNode jNode) {
            super(jNode);
        }
    }

    public class FormUpdatePhoneUser extends FormRegisterPhone {
        public FormUpdatePhoneUser(JsonNode jNode) {
            super(jNode);
        }
    }

    public class FormVerifyPhoneUser extends FormRegisterPhone {
        @Getter
        private String smsCode, tokenId;

        public FormVerifyPhoneUser(JsonNode jNode) {
            super(jNode);
            smsCode = getString(jNode, "smsCode");
            tokenId = getString(jNode, "tokenId");
        }
    }

}
