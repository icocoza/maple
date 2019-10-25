package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EBoardContentType;
import com.ccz.modules.domain.constant.EBoardPreferences;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardCommonRepository extends CommonRepository {

    public boolean addBoard(String scode, String boardId, String userId, String userName, String title,
                          String shortContent, boolean hasImage, boolean hasFile, String category,
                          EBoardContentType contentType) {
        return new BoardRec(scode).insert(boardId, userId, userName, title, shortContent, hasImage, hasFile, category, contentType);
    }

    public boolean deleteBoard(String scode, String boardId, String userId) {
        return new BoardRec(scode).updateDelete(boardId, userId);
    }

    public boolean updateBoard(String scode, String boardId, String userId, String title,
                               String shortContent, boolean hasImage, boolean hasFile, String category,
                               EBoardContentType contentType) {
        return new BoardRec(scode).updateBoard(boardId, userId, title, shortContent, hasImage, hasFile, category, contentType);
    }

    public boolean updateBoardTitle(String scode, String boardId, String userId, String title) {
        return new BoardRec(scode).updateTitle(boardId, userId, title);
    }

    public boolean updateBoardContent(String scode, String boardId, String userId, String content, boolean hasImage, boolean hasFile) {
        return new BoardRec(scode).updateContent(boardId, userId, content, hasImage, hasFile);
    }

    public boolean updateBoardCategory(String scode, String boardId, String userId, String category) {
        return new BoardRec(scode).updateCategory(boardId, userId, category);
    }

    public List<BoardRec> getBoardList(String scode, String category, int offset, int count) {
        return new BoardRec(scode).getList(category, offset, count);
    }

    public List<BoardRec> getBoardListByUser(String scode, String userId, String category, int offset, int count) {
        return new BoardRec(scode).getList(userId, category, offset, count);
    }

    ///
    public boolean addBoardContent(String scode, String boardId, String userId, String content) {
        return new BoardContentRec(scode).insert(boardId, userId, content);
    }

    public boolean deleteBoardContent(String scode, String boardId, String userId) {
        return new BoardContentRec(scode).updateDelete(boardId, userId);
    }

    public boolean updateBoardContent(String scode, String boardId, String userId, String content) {
        return new BoardContentRec(scode).updateContent(boardId, userId, content);
    }

    public String getBoardContent(String scode, String boardId) {
        return new BoardContentRec(scode).getContent(boardId);
    }

    ///
    public boolean createBoardCounter(String scode, String boardId) {
        return new BoardCountRec(scode).insert(boardId);
    }

    public boolean incLike(String scode, String  boardId, boolean bInc) {
        return new BoardCountRec(scode).incLike(boardId, bInc);
    }

    public boolean incDislike(String scode, String  boardId, boolean bInc) {
        return new BoardCountRec(scode).incDislike(boardId, bInc);
    }

    public boolean incVisit(String scode, String  boardId) {
        return new BoardCountRec(scode).incVisit(boardId);
    }

    public boolean incReply(String scode, String  boardId, boolean bInc) {
        return new BoardCountRec(scode).incReply(boardId, bInc);
    }

    public BoardCountRec getBoardCounter(String scode, String  boardId) {
        return new BoardCountRec(scode).getCount(boardId);
    }

    public List<BoardDetailRec> getBoardDetailList(String scode, String category, int offset,  int count) {
        return new BoardDetailRec(scode).getBoardList(category, offset, count);
    }

    public List<BoardDetailRec> getBoardDetailList(String scode, List<String> boardIds) {
        return new BoardDetailRec(scode).getBoardList(boardIds);
    }

    public String getCropfilePath(String scode, BoardDetailRec rec) {
        return new BoardDetailRec(scode).getCropfilePath(rec);
    }

    ///
    public boolean addBoardVoter(String scode, String boardId, String userId, String userName, EBoardPreferences preferences) {
        return new BoardLikeRec(scode).insert(boardId, userId, userName, preferences);
    }

    public boolean deleteBoardVoter(String scode, String boardId) {
        return new BoardLikeRec(scode).delete(boardId);
    }

    public boolean deleteBoardVoter(String scode, String boardId, String userId, EBoardPreferences preferences ) {
        return new BoardLikeRec(scode).delete(boardId, userId, preferences);
    }

    public BoardLikeRec getBoardVoter(String scode, String boardId, String userId) {
        return new BoardLikeRec(scode).getPreference(boardId, userId);
    }

    ///
    public boolean addReply(String scode, String replyId, String boardId, String parentId, String userId, String userName, int depth, String body) {
        return new BoardReplyRec(scode).insert(replyId, boardId, parentId, userId, userName, depth, body);
    }

    public boolean deleteReply(String scode, String replyId, String userId) {
        return new BoardReplyRec(scode).delete(replyId, userId);
    }

    public boolean deleteReplyIfNoChild(String scode, String replyId, String boardId, String userId) {
        return new BoardReplyRec(scode).deleteIfNoChild(replyId, boardId, userId);
    }

    public boolean updateReplyBody(String scode, String replyId, String userId, String body) {
        return new BoardReplyRec(scode).updateMsg(replyId, userId, body);
    }

    public List<BoardReplyRec> getReplyList(String scode, String boardId, int offset, int count) {
        return new BoardReplyRec(scode).getList(boardId, offset, count);
    }

    //for vote
    public boolean addVote(String scode, String boardId, String userId, String userName, LocalDateTime expiredAt) {
        return new BoardVoteRec(scode).insert(boardId, userId, userName, expiredAt);
    }

    public boolean updateVoteExpireAt(String scode, String boardId, String userId, LocalDateTime expiredAt) {
        return new BoardVoteRec(scode).updateExpireTime(boardId, userId, expiredAt);
    }

    public boolean updateVoteClose(String scode, String boardId, String userId, boolean closed) {
        return new BoardVoteRec(scode).updateClose(boardId, userId, closed);
    }

    public boolean deleteVote(String scode, String boardId, String userId) {
        return new BoardVoteRec(scode).delete(boardId, userId);
    }

    public BoardVoteRec getVoteInfo(String scode, String boardId) {
        return new BoardVoteRec(scode).getVoteInfo(boardId);
    }

    public List<BoardVoteRec> getVoteInfoList(String scode, List<String> boardIds) {
        return new BoardVoteRec(scode).getVoteInfoList(boardIds);
    }

    //for vote item
    public boolean addVoteItem(String scode, String boardId, String voteItemId, String itemText) {
        return new BoardVoteItemRec(scode).insert(boardId, voteItemId, itemText);
    }

    public boolean incVoteItem(String scode, String boardId, String voteItemId) {
        return new BoardVoteItemRec(scode).incVoteItem(boardId, voteItemId);
    }

    public boolean decVoteItem(String scode, String boardId, String voteItemId) {
        return new BoardVoteItemRec(scode).decVoteItem(boardId, voteItemId);
    }

    public boolean updateVoteText(String scode, String boardId, String voteItemId, String itemText) {
        return new BoardVoteItemRec(scode).updateVoteText(boardId, voteItemId, itemText);
    }

    public boolean deleteVote(String scode, String boardId) {
        return new BoardVoteItemRec(scode).deleteVote(boardId);
    }
    public boolean deleteVoteItem(String scode, String boardId, String voteItemId) {
        return new BoardVoteItemRec(scode).deleteVoteItem(boardId, voteItemId);
    }

    public List<BoardVoteItemRec> getVoteItemList(String scode, String boardId) {
        return new BoardVoteItemRec(scode).getVoteItemList(boardId);
    }

    //for vote user
    public boolean addVoteUser(String scode, String userId, String boardId, String voteItemId) {
        return new BoardVoteUserRec(scode).insert(userId, boardId, voteItemId);
    }

    public boolean updateVoteSelectedItem(String scode, String userId, String boardId, String voteItemId) {
        return new BoardVoteUserRec(scode).updateSelectItem(userId, boardId, voteItemId);
    }

    public boolean deleteVoteUser(String scode, String userId, String boardId) {
        return new BoardVoteUserRec(scode).delete(userId, boardId);
    }

    public BoardVoteUserRec getVoteUser(String scode, String userId, String boardId) {
        return new BoardVoteUserRec(scode).getVoteUser(userId, boardId);
    }

    public List<BoardVoteUserRec> getVoteUserList(String scode, String boardId) {
        return new BoardVoteUserRec(scode).getVoteUserList(boardId);
    }

    public Map<String, Integer> getVoteCount(String scode, List<String> boardIds) {
        return new BoardVoteUserCountRec(scode).getVoteCount(boardIds);
    }

    public Map<String, Integer>  getVotedBoardId(String scode, String userId, List<String> boardIds) {
        return new BoardVoteUserCountRec(scode).getVotedBoardId(userId, boardIds);
    }

    //for scrap
    public boolean addScrap(String scode, String scrapId, String url, String title, String subTitle) {
        return new ScrapRec(scode).insert(scrapId, url, title, subTitle);
    }

    public String queryInsertScrap(String scode, String scrapId, String url, String title, String subTitle) {
        return ScrapRec.qInsertScrap(scrapId, url, title, subTitle);
    }

    public ScrapRec getScrap(String scode, String scrapId) {
        return new ScrapRec(scode).getScrap(scrapId);
    }

    public List<ScrapRec> getScrapList(String scode, List<String> scrapIds) {
        return new ScrapRec(scode).getScrapList(scrapIds);
    }

    public List<ScrapRec> getScrapListByUrl(String scode, List<String> urls) {
        return new ScrapRec(scode).getScrapListByUrl(urls);
    }

    public boolean updateScrap(String scode, String scrapId, String scrapIp, String scrapPath, String scrapExt) {
        return new ScrapRec(scode).updateScrap(scrapId, scrapIp, scrapPath, scrapExt);
    }

    public boolean addScrapBody(String scode, String scrapId,  String body) {
        return new ScrapBodyRec(scode).insert(scrapId, body);
    }

    public String queryInsertScrapBody(String scode, String scrapId, String body) {
        return ScrapBodyRec.qInsertScrapBody(scrapId, body);
    }

    public ScrapBodyRec getScrapBody(String scode, String scrapId) {
        return new ScrapBodyRec(scode).getScrapBody(scrapId);
    }

    public boolean updateScrapBody(String scode, String scrapId, String body) {
        return new ScrapBodyRec(scode).updateScrapBody(scrapId, body);
    }

    public List<ScrapDetailRec> getScrapDetailList(String scode, String boardId) {
        return new ScrapDetailRec(scode).getScrapDetailList(boardId);
    }

    public boolean addBoardScrapInfo(String scode, String boardId, String scrapId) {
        return new BoardScrapRec(scode).insert(boardId, scrapId);
    }

    public String queryInsertBoardScrap(String scode, String boardId, String scrapId) {
        return BoardScrapRec.qInsertScrap(boardId, scrapId);
    }

    public BoardScrapRec getBoardScrapId(String scode, String boardId) {
        return new BoardScrapRec(scode).getScrapId(boardId);
    }

    public List<BoardScrapRec> getBoardScrapIdList(String scode, String boardId) {
        return new BoardScrapRec(scode).getScrapIdList(boardId);
    }

    public boolean deleteBoardScrap(String scode, String boardId) {
        return new BoardScrapRec(scode).delete(boardId);
    }
}
