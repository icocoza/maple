package com.ccz.apps.maple.api.controller.v1.board;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.controller.admin.AdminForm;
import com.ccz.modules.controller.board.BoardForm;
import com.ccz.modules.domain.constant.EBoardCmd;
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
@Api(tags = "Maple Board APIs")
@RequestMapping(value = "/api/v1/maple/board", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BoardSwaggerController {

    @Autowired
    private MapleServerHandler mapleServerHandler;

    @ApiOperation(value = "게시글 등록", notes = "게시글 등록", response = String.class)
    @RequestMapping(value = "/addboard", method = {RequestMethod.GET})
    public ResponseEntity<?> addBoard(@ModelAttribute @Validated BoardForm.AddBoardForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제", response = String.class)
    @RequestMapping(value = "/delboard", method = {RequestMethod.GET})
    public ResponseEntity<?> delBoard(@ModelAttribute @Validated BoardForm.DelBoardForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "제목 업데이트", notes = "제목 업데이트", response = String.class)
    @RequestMapping(value = "/updateTitle", method = {RequestMethod.GET})
    public ResponseEntity<?> updateTitle(@ModelAttribute @Validated BoardForm.UpdateTitleForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "내용 업데이트", notes = "내용 업데이트", response = String.class)
    @RequestMapping(value = "/updateContent", method = {RequestMethod.GET})
    public ResponseEntity<?> updateContent(@ModelAttribute @Validated BoardForm.UpdateContentForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "카테고리 업데이트", notes = "카테고리 업데이트", response = String.class)
    @RequestMapping(value = "/updateCategory", method = {RequestMethod.GET})
    public ResponseEntity<?> updateCategory(@ModelAttribute @Validated BoardForm.UpdateCategoryForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 업데이트", notes = "게시글 업데이트", response = String.class)
    @RequestMapping(value = "/updateBoard", method = {RequestMethod.GET})
    public ResponseEntity<?> updateBoard(@ModelAttribute @Validated BoardForm.UpdateBoardForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 목록", notes = "게시글 목록", response = String.class)
    @RequestMapping(value = "/boardList", method = {RequestMethod.GET})
    public ResponseEntity<?> boardList(@ModelAttribute @Validated BoardForm.BoardLikeForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 내용", notes = "게시글 내용", response = String.class)
    @RequestMapping(value = "/getContent", method = {RequestMethod.GET})
    public ResponseEntity<?> getContent(@ModelAttribute @Validated BoardForm.BoardContentForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 좋아요", notes = "게시글 좋아요", response = String.class)
    @RequestMapping(value = "/boardLike", method = {RequestMethod.GET})
    public ResponseEntity<?> boardLike(@ModelAttribute @Validated BoardForm.BoardLikeForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 싫어요", notes = "게시글 싫어요", response = String.class)
    @RequestMapping(value = "/boardDislike", method = {RequestMethod.GET})
    public ResponseEntity<?> boardDislike(@ModelAttribute @Validated BoardForm.BoardDislikeForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "댓글 추가", notes = "댓글 추가", response = String.class)
    @RequestMapping(value = "/addReply", method = {RequestMethod.GET})
    public ResponseEntity<?> addReply(@ModelAttribute @Validated BoardForm.AddReplyForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제", response = String.class)
    @RequestMapping(value = "/delReply", method = {RequestMethod.GET})
    public ResponseEntity<?> delReply(@ModelAttribute @Validated BoardForm.DelReplyForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "댓글 목록", notes = "댓글 목록", response = String.class)
    @RequestMapping(value = "/replyList", method = {RequestMethod.GET})
    public ResponseEntity<?> replyList(@ModelAttribute @Validated BoardForm.ReplyListForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "투표글 추가", notes = "투표글 추가", response = String.class)
    @RequestMapping(value = "/addVote", method = {RequestMethod.GET})
    public ResponseEntity<?> addVote(@ModelAttribute @Validated BoardForm.AddVoteForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "투표 항목 목록", notes = "투표 항목 목록", response = String.class)
    @RequestMapping(value = "/voteItemList", method = {RequestMethod.GET})
    public ResponseEntity<?> voteItemList(@ModelAttribute @Validated BoardForm.VoteItemListForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "투표 항목 선택", notes = "투표 항목 선택", response = String.class)
    @RequestMapping(value = "/selectVoteItem", method = {RequestMethod.GET})
    public ResponseEntity<?> selectVoteItem(@ModelAttribute @Validated BoardForm.SelectVoteItemForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "투표글 업데이트", notes = "투표글 업데이트", response = String.class)
    @RequestMapping(value = "/updateVote", method = {RequestMethod.GET})
    public ResponseEntity<?> updateVote(@ModelAttribute @Validated BoardForm.VoteUpdateForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "투표 선택 변경", notes = "투표 선택 변경", response = String.class)
    @RequestMapping(value = "/changeSelection", method = {RequestMethod.GET})
    public ResponseEntity<?> changeSelection(@ModelAttribute @Validated BoardForm.VoteChangeForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "투표글 정보 목록", notes = "투표글 정보 목록", response = String.class)
    @RequestMapping(value = "/getVoteInfoList", method = {RequestMethod.GET})
    public ResponseEntity<?> getVoteInfoList(@ModelAttribute @Validated BoardForm.VoteInfoListForm form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "게시글 검색", notes = "게시글 검색", response = String.class)
    @RequestMapping(value = "/boardSearch", method = {RequestMethod.GET})
    public ResponseEntity<?> boardSearch(@ModelAttribute @Validated BoardForm.BoardSearch form) {
        String result = mapleServerHandler.process(null, form);
        return ResponseEntity.ok(result);
    }

}
