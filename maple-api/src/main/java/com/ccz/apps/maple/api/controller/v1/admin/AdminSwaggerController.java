package com.ccz.apps.maple.api.controller.v1.admin;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.admin.AdminForm;
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
@Api(tags = "Maple Admin APIs")
@RequestMapping(value = "/api/v1/maple/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdminSwaggerController {
    @Autowired
    private MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "서비스 사용자 등록", notes = "서비스 사용자 등록", response = String.class)
    @RequestMapping(value = "/registerAdminUser", method = {RequestMethod.GET})
    public ResponseEntity<?> registerAdminUser(@ModelAttribute @Validated AdminForm.AdminUserForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 어드민 로그인", notes = "서비스 어드민 로그인", response = String.class)
    @RequestMapping(value = "/loginAdminUser", method = {RequestMethod.POST})
    public ResponseEntity<?> loginAdminUser(@ModelAttribute @Validated AdminForm.AdminLoginForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 어드민 로그아웃", notes = "서비스 어드민 로그아웃", response = String.class)
    @RequestMapping(value = "/logoutAdminUser", method = {RequestMethod.POST})
    public ResponseEntity<?> logoutAdminUser(@ModelAttribute @Validated AdminForm.AdminLogoutForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 앱 등록", notes = "서비스 앱 등록", response = String.class)
    @RequestMapping(value = "/addApp", method = {RequestMethod.POST})
    public ResponseEntity<?> addApp(@ModelAttribute @Validated AdminForm.AddAppForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 앱 삭제", notes = "서비스 앱 삭제", response = String.class)
    @RequestMapping(value = "/delApp", method = {RequestMethod.POST})
    public ResponseEntity<?> delApp(@ModelAttribute @Validated AdminForm.DelAppForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 앱 수정", notes = "서비스 앱 수정", response = String.class)
    @RequestMapping(value = "/modifyApp", method = {RequestMethod.POST})
    public ResponseEntity<?> modifyApp(@ModelAttribute @Validated AdminForm.ModifyAppForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 앱 갯수", notes = "서비스 앱 갯수", response = String.class)
    @RequestMapping(value = "/appCount", method = {RequestMethod.POST})
    public ResponseEntity<?> appCount(@ModelAttribute @Validated AdminForm.AppCountForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "서비스 앱 업데이트", notes = "서비스 앱 업데이트", response = String.class)
    @RequestMapping(value = "/updateApp", method = {RequestMethod.POST})
    public ResponseEntity<?> updateApp(@ModelAttribute @Validated AdminForm.UpdateAppForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
