package com.ccz.apps.maple.api.controller.v1.friend;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.friend.FriendForm;
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
@Api(tags = "Maple friend APIs")
@RequestMapping(value = "/api/v1/maple/friend", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FriendSwaggerController {
    @Autowired
    private MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "친구 추가", notes = "친구 추가", response = String.class)
    @RequestMapping(value = "/addFriend", method = {RequestMethod.GET})
    public ResponseEntity<?> addFriend(@ModelAttribute @Validated FriendForm.AddFriendForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "친구 삭제", notes = "친구 삭제", response = String.class)
    @RequestMapping(value = "/delFriend", method = {RequestMethod.POST})
    public ResponseEntity<?> delFriend(@ModelAttribute @Validated FriendForm.DelFriendForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "친구 타입 변경", notes = "친구 타입 변경", response = String.class)
    @RequestMapping(value = "/changeFriendType", method = {RequestMethod.POST})
    public ResponseEntity<?> changeFriendType(@ModelAttribute @Validated FriendForm.ChangeFriendType form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "친구 ID 목록", notes = "친구 ID 목록", response = String.class)
    @RequestMapping(value = "/friendIdList", method = {RequestMethod.POST})
    public ResponseEntity<?> friendIdList(@ModelAttribute @Validated FriendForm.FriendIdListForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "친구 수", notes = "친구 수", response = String.class)
    @RequestMapping(value = "/friendCount", method = {RequestMethod.POST})
    public ResponseEntity<?> friendCount(@ModelAttribute @Validated FriendForm.FriendCountForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "친구 정보", notes = "친구 정보", response = String.class)
    @RequestMapping(value = "/friendInfo", method = {RequestMethod.POST})
    public ResponseEntity<?> friendInfo(@ModelAttribute @Validated FriendForm.FriendInfoForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "나를 친추한 친구 목록", notes = "나를 친추한 친구 목록", response = String.class)
    @RequestMapping(value = "/appendMeFriend", method = {RequestMethod.POST})
    public ResponseEntity<?> appendMeFriend(@ModelAttribute @Validated FriendForm.AppendMeForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "나를 차단한 친구 목록", notes = "나를 차단한 친구 목록", response = String.class)
    @RequestMapping(value = "/blockMeFriend", method = {RequestMethod.POST})
    public ResponseEntity<?> blockMeFriend(@ModelAttribute @Validated FriendForm.BlockMeForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "나를 친추한 친구 수", notes = "나를 친추한 친구 수", response = String.class)
    @RequestMapping(value = "/appendMeCount", method = {RequestMethod.POST})
    public ResponseEntity<?> appendMeCount(@ModelAttribute @Validated FriendForm.FriendNameForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "나를 차단한 친구 수", notes = "나를 차단한 친구 수", response = String.class)
    @RequestMapping(value = "/blockMeCount", method = {RequestMethod.POST})
    public ResponseEntity<?> blockMeCount(@ModelAttribute @Validated FriendForm.FriendNameForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
