package com.ccz.modules.controller.user;

import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EUserAuthType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserForm {

    @ApiModel(description = "아이디 검색 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class FindIdForm extends CommonForm {
        @ApiModelProperty(value="사용자 이름", example="홍길동", required=true)
        private String userName;
    }

    /*for User Register Command*/
    @ApiModel(description = "사용자 등록 공통 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class RegisterForm extends CommonForm {
        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "unique device Id", example = "UNIQUE_ID010101", required = true)
        private String uuid;

        protected EUserAuthType authType = EUserAuthType.none;

        public RegisterForm() {
        }

        public RegisterForm(String uuid, String userName, CommonForm form) {
            this.uuid = uuid;
            this.userName = userName;
            super.setScode(form.getScode());
            super.setCmd(form.getCmd());
        }

        public boolean isAnonymous() {
            return authType == EUserAuthType.none;
        }
    }

    @ApiModel(description = "사용자 등록 Id/Pw Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class IdPwForm extends RegisterForm {
        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "비밀번호", example = "1234", required = true)
        private String pw;

        public IdPwForm() {
            super.authType = EUserAuthType.idpw;
        }
    }

    @ApiModel(description = "사용자 등록 Email Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class EmailForm extends RegisterForm {
        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "User Email", example = "test@test.com", required = true)
        private String email;

        public EmailForm() {
            super.authType = EUserAuthType.email;
        }
    }

    @ApiModel(description = "사용자 등록 Mobiile Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class MobileForm extends RegisterForm {
        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "User Mobile No.", example = "0123456789", required = true)
        private String phone;

        public MobileForm() {
            super.authType = EUserAuthType.phone;
        }
    }

    /*for User Auth. Command*/

    @ApiModel(description = "로그인 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class LoginForm extends CommonForm {
        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "비밀번호", example = "1234", required = true)
        private String pw;

        @ApiModelProperty(value = "OS Type", example = "Android or IOS or PC", required = true)
        private String osType;

        @ApiModelProperty(value = "OS Version", example = "10.0", required = true)
        private String osVersion;

        @ApiModelProperty(value = "App. Version", example = "1.0.1", required = true)
        private String appVersion;

        @ApiModelProperty(value = "End Point ID for Push", example = "EPID001", required = false)
        private String epid;

        @ApiModelProperty(value = "unique device Id", example = "UNIQUE_ID010101", required = true)
        private String uuid;
    }

    @ApiModel(description = "익명 로그인 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AnonymousLoginForm extends LoginForm {
    }

    @ApiModel(description = "User Signin Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class SigninForm extends CommonForm {
        @ApiModelProperty(value = "로그인 토큰", example = "token", required = true)
        private String token;

        @ApiModelProperty(value = "unique device Id", example = "UNIQUE_ID010101", required = true)
        private String uuid;

        @ApiModelProperty(hidden = true)
        private String tokenId;
        @ApiModelProperty(hidden = true)
        private String userNameByToken;
        @ApiModelProperty(hidden = true)
        private String uuidByToken;
        @ApiModelProperty(hidden = true)
        private EUserAuthType authTypeByToken;

        public boolean decode() {
            try{
                if(this.token==null)
                    return false;
                String dec = Crypto.AES256Cipher.getInst().dec(token);
                String[] chunk = dec.split(AsciiSplitter.ASS.UNIT);
                userNameByToken = chunk[0];
                tokenId = chunk[1];
                uuidByToken = chunk[2];
                authTypeByToken = EUserAuthType.getType(chunk[3]);
                return true;
            }catch(Exception e) {
                log.error(e.getMessage());
                return false;
            }
        }

        public boolean isValidUserToken() {
            if(userNameByToken==null || userNameByToken.length()<1 || uuidByToken == null || uuidByToken.length()<1)
                return false;
            return true;
        }
    }


}