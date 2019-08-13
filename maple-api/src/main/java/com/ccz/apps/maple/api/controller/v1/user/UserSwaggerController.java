package com.ccz.apps.maple.api.controller.v1.user;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.user.UserForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "MissSaigon User APIs")
@RequestMapping(value = "/api/v1/misssaigon/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserSwaggerController {
    @Autowired
    MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "사용자 아이디 조회", notes = "사용자 아이디 조회", response = String.class)
    @RequestMapping(value = "/findByUserName", method = {RequestMethod.GET})
    public ResponseEntity<?> findByUserName(@ModelAttribute @Validated UserForm.FindIdForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 ID/PW 가입", notes = "사용자 ID/PW 가입", response = String.class)
    @RequestMapping(value = "/regIdPw", method = {RequestMethod.POST})
    public ResponseEntity<?> regIdPw(@ModelAttribute @Validated UserForm.IdPwForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 Email 가입", notes = "사용자 Email 가입", response = String.class)
    @RequestMapping(value = "/regEmail", method = {RequestMethod.POST})
    public ResponseEntity<?> regEmail(@ModelAttribute @Validated UserForm.EmailForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 Mobile 가입", notes = "사용자 Mobile 가입", response = String.class)
    @RequestMapping(value = "/regPhone", method = {RequestMethod.POST})
    public ResponseEntity<?> regPhone(@ModelAttribute @Validated UserForm.MobileForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 로그인", notes = "사용자 로그인", response = String.class)
    @RequestMapping(value = "/loginUser", method = {RequestMethod.POST})
    public ResponseEntity<?> loginUser(@ModelAttribute @Validated UserForm.LoginForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "익명 사용자 로그인", notes = "익명 사용자 로그인", response = String.class)
    @RequestMapping(value = "/loginAnonymousUser", method = {RequestMethod.POST})
    public ResponseEntity<?> loginAnonymousUser(@ModelAttribute @Validated UserForm.LoginForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "사용자 접속 인증", notes = "사용자 접속 인증", response = String.class)
    @RequestMapping(value = "/signInUser", method = {RequestMethod.POST})
    public ResponseEntity<?> signInUser(@ModelAttribute @Validated UserForm.SigninForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
