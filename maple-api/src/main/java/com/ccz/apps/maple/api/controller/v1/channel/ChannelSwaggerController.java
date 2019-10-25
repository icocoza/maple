package com.ccz.apps.maple.api.controller.v1.channel;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.channel.ChannelForm;
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
@Api(tags = "Maple Channel APIs")
@RequestMapping(value = "/api/v1/maple/channel", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ChannelSwaggerController {
    @Autowired
    private MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "대화 채널 생성", notes = "대화 채널 생성", response = String.class)
    @RequestMapping(value = "/createChannel", method = {RequestMethod.GET})
    public ResponseEntity<?> createChannel(@ModelAttribute @Validated ChannelForm.ChannelCreateForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "대화 채널 종료", notes = "대화 채널 종료", response = String.class)
    @RequestMapping(value = "/exitChannel", method = {RequestMethod.POST})
    public ResponseEntity<?> exitChannel(@ModelAttribute @Validated ChannelForm.ChannelExitForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "대화 채널 입장", notes = "대화 채널 입장", response = String.class)
    @RequestMapping(value = "/enterChannel", method = {RequestMethod.POST})
    public ResponseEntity<?> enterChannel(@ModelAttribute @Validated ChannelForm.ChannelEnterForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "대화 채널 초대", notes = "대화 채널 초대", response = String.class)
    @RequestMapping(value = "/inviteChannel", method = {RequestMethod.POST})
    public ResponseEntity<?> inviteChannel(@ModelAttribute @Validated ChannelForm.ChannelInviteForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "내 대화 채널", notes = "내 대화 채널", response = String.class)
    @RequestMapping(value = "/myChannel", method = {RequestMethod.POST})
    public ResponseEntity<?> myChannel(@ModelAttribute @Validated ChannelForm.MyChannelForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "내 대화 채널 갯수", notes = "내 대화 채널 갯수", response = String.class)
    @RequestMapping(value = "/myChannelCount", method = {RequestMethod.POST})
    public ResponseEntity<?> myChannelCount(@ModelAttribute @Validated ChannelForm.ChannelIdForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "대화 채널 마지막 메시지", notes = "대화 채널 마지막 메시지", response = String.class)
    @RequestMapping(value = "/channelLastMessage", method = {RequestMethod.POST})
    public ResponseEntity<?> channelLastMessage(@ModelAttribute @Validated ChannelForm.ChannelLastMessageForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "대화 채널 정보", notes = "대화 채널 정보", response = String.class)
    @RequestMapping(value = "/channelInfo", method = {RequestMethod.POST})
    public ResponseEntity<?> channelInfo(@ModelAttribute @Validated ChannelForm.ChannelInfoForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
