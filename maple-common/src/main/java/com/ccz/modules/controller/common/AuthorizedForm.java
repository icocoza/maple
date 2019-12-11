package com.ccz.modules.controller.common;

import com.ccz.modules.controller.user.AuthToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "인증 이후의 Common Form")
@Data
public class AuthorizedForm extends CommonForm{

    @ApiModelProperty(value="Authorized Token after Signin", example="authorizedToken")
    private String signinToken;

    private AuthToken authToken;

    public boolean decode() {
        authToken = new AuthToken().dec(signinToken);
        return authToken.isDecrypted();
    }
}
