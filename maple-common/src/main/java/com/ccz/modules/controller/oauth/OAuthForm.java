package com.ccz.modules.controller.oauth;

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
public class OAuthForm {

    @ApiModel(description = "OAuth Join Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class JoinForm extends CommonForm {
        @ApiModelProperty(value="사용자 이름", example="홍길동", required=true)
        private String userName;
    }

    @ApiModel(description = "OAuth SignIn Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class SignInForm extends CommonForm {
        @ApiModelProperty(value="Facebook Code", example="Facebook Code", required=true)
        private String code;
    }


}