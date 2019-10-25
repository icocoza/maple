package com.ccz.apps.maple.api.controller.v1.oauth;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.oauth.OAuthForm;
import com.ccz.modules.controller.user.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "Maple User APIs")
@RequestMapping(value = "/api/v1/maple/oauth", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OAuthSwaggerController {
    @Autowired
    MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "사용자 Facebook 가입", notes = "사용자 Facebook 가입", response = String.class)
    @RequestMapping(value = "/facebook/join", method = {RequestMethod.GET})
    public ResponseEntity<?> findByUserName(@ModelAttribute @Validated OAuthForm.JoinForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 Facebook SignIn", notes = "사용자 Facebook SignIn", response = String.class)
    @RequestMapping(value = "/facebook/signin", method = {RequestMethod.POST})
    public ResponseEntity<?> regIdPw(@ModelAttribute @Validated UserForm.SigninForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
