package com.ccz.modules.controller.user;

import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.controller.common.AuthorizedForm;
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
        @ApiModelProperty(value = "앱 등록시 발급받은 앱토큰", example = "홍길동", required = true)
        private String appToken;

        @ApiModelProperty(value="사용자 아이디", example="홍길동", required=true, allowableValues = "range[6, 20]")
        private String userId;
    }

    /*for User Register Command*/
    @ApiModel(description = "사용자 등록 공통 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class RegisterForm extends CommonForm {
        @ApiModelProperty(value = "앱 등록시 발급받은 앱토큰", example = "홍길동", required = true)
        private String appToken;

        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true, allowableValues = "range[6, 20]")
        private String userId;

        @ApiModelProperty(value = "Unique Device Id", example = "UNIQUE_ID010101", required = true)
        private String uuid;

        protected EUserAuthType authType = EUserAuthType.none;

        public RegisterForm() {
        }

        public RegisterForm(String uuid, String userId, CommonForm form) {
            this.uuid = uuid;
            this.userId = userId;
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

        @ApiModelProperty(value = "비밀번호", example = "1234", required = true, allowableValues = "range[8, 20]")
        private String pw;

        public IdPwForm() {
            super.authType = EUserAuthType.idpw;
        }
        public IdPwForm(String uuid, String userName, CommonForm form) {
            super(uuid, userName, form);
            super.authType = EUserAuthType.idpw;
        }
    }

    @ApiModel(description = "사용자 등록 Email Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class EmailForm extends RegisterForm {

        @ApiModelProperty(value = "User Email", example = "test@test.com", required = true, allowableValues = "range[6, 50]")
        private String email;

        public EmailForm() {
            super.authType = EUserAuthType.email;
        }
    }

    @ApiModel(description = "사용자 등록 Mobiile Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class MobileForm extends RegisterForm {

        @ApiModelProperty(value = "User Mobile No.", example = "0123456789", required = true, allowableValues = "range[10, 12]")
        private String phone;

        public MobileForm() {
            super.authType = EUserAuthType.phone;
        }
    }

    /*for User Auth. Command*/

    @ApiModel(description = "로그인 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class LoginForm extends RegisterForm {

        @ApiModelProperty(value = "비밀번호", example = "1234", required = true, allowableValues = "range[8, 20]")
        private String pw;

        @ApiModelProperty(value = "OS Type", example = "Android or IOS or PC", required = true)
        private String osType;

        @ApiModelProperty(value = "OS Version", example = "10.0", required = true)
        private String osVersion;

        @ApiModelProperty(value = "App. Version", example = "1.0.1", required = true)
        private String appVersion;

        @ApiModelProperty(value = "End Point ID for Push", example = "EPID001", required = false)
        private String epid = "";

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

        private Token loginToken;

        public boolean decode() {
            try{
                loginToken = new Token().dec(token);
                return loginToken.isDecrypted();
            }catch(Exception e) {
                log.error(e.getMessage());
                return false;
            }
        }

        public boolean isValidLoginToken() {
            return loginToken != null && loginToken.isDecrypted() && uuid != null && uuid.equals(loginToken.getUuid());
        }
    }

    @ApiModel(description = "익명 로그인 Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChangePwForm extends AuthorizedForm{
        @ApiModelProperty(value = "기존 비밀번호", example = "1234", required = true, allowableValues = "range[8, 20]")
        private String oldPw;

        @ApiModelProperty(value = "신규 비밀번호", example = "4321", required = true, allowableValues = "range[8, 20]")
        private String newPw;
    }

}