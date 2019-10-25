package com.ccz.apps.maple.api.controller.v1.message;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.message.MessageForm;
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
@Api(tags = "Maple Message APIs")
@RequestMapping(value = "/api/v1/maple/message", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MessageSwaggerController {
    @Autowired
    private MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "메시지", notes = "메시지", response = String.class)
    @RequestMapping(value = "/message", method = {RequestMethod.GET})
    public ResponseEntity<?> message(@ModelAttribute @Validated MessageForm.ChatMessageForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "메시지 동기화", notes = "메시지 동기화", response = String.class)
    @RequestMapping(value = "/syncMessage", method = {RequestMethod.POST})
    public ResponseEntity<?> syncMessage(@ModelAttribute @Validated MessageForm.SyncMessageForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "메시지 읽음", notes = "메시지 읽음", response = String.class)
    @RequestMapping(value = "/readMessage", method = {RequestMethod.POST})
    public ResponseEntity<?> readMessage(@ModelAttribute @Validated MessageForm.ReadMessageForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "메시지 삭제", notes = "메시지 삭제", response = String.class)
    @RequestMapping(value = "/delMessage", method = {RequestMethod.POST})
    public ResponseEntity<?> delMessage(@ModelAttribute @Validated MessageForm.DelMessageForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
