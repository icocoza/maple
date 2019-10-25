package com.ccz.modules.controller.board;

import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.action.SessionManager;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.common.utils.*;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.constant.EBoardCmd;
import com.ccz.modules.domain.constant.EBoardContentType;
import com.ccz.modules.domain.file.UploadFile;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.session.AuthSession;
import com.ccz.modules.repository.db.board.*;
import com.ccz.modules.repository.db.file.FileCommonRepository;
import com.ccz.modules.repository.db.file.UploadFileRec;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import com.ccz.modules.server.config.FileConfig;
import com.ccz.modules.controller.board.BoardForm.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardCommandCommonAction extends CommandAction {
    SessionManager sessionManager;
    BoardCommonRepository boardCommonRepository;
    UserCommonRepository userCommonRepository;
    FileCommonRepository fileCommonRepository;

    ImageResizeWorker imageResizeWorker = new ImageResizeWorker();

    @Override
    public void initCommandFunctions(CommonRepository commonRepository) {
        boardCommonRepository = (BoardCommonRepository) commonRepository;
    }

    public void initCommandFunctions(CommonRepository commonRepository, CommonRepository fileCommonRepository, CommonRepository userCommonRepository) {
        this.initCommandFunctions(commonRepository);
        this.userCommonRepository = (UserCommonRepository) userCommonRepository;
        this.fileCommonRepository = (FileCommonRepository) fileCommonRepository;

        super.setCommandFunction(makeCommandId(EBoardCmd.addboard), doAddBoard); //O
        super.setCommandFunction(makeCommandId(EBoardCmd.delboard), doDelBoard);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.updatetitle), doUpdateBoardTitle);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.updatecontent), doUpdateBoardContent);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.updatecategory), doUpdateBoardCategory);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.updateboard), doUpdateBoard);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.boardlist), doGetBoardList);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.getcontent), doGetBoardContent);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.like), incBoardLike);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.dislike), incBoardDislike);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.addreply), addReply); //O
        super.setCommandFunction(makeCommandId(EBoardCmd.delreply), delReply);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.replylist), getReplyList);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.addvote), addVote);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.voteitemlist), getVoteItemList);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.selvote), selectVoteItem);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.voteupdate), updateVote);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.changeselection), changeVoteSelection);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.voteinfolist), getVoteInfoList);//O
        super.setCommandFunction(makeCommandId(EBoardCmd.boardsearch), boardSearch);
    }

    @Override
    public String makeCommandId(Enum e) {
        return  "" + e.name();
    }

    private String getShortContent(String content) {
        if (content.length() < 54)
            return content;
        return content.substring(0, 54) + "...(더보기)";
    }

    ICommandFunction<AuthSession, ResponseData<EAllError>, AddBoardForm> doAddBoard = (AuthSession ss, ResponseData<EAllError> res, AddBoardForm form) -> {
        List<String> scrapIds = findAndAddScrap(form.getScode(), form.getContent());
        return addBoard(ss, res, form, scrapIds);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, DelBoardForm> doDelBoard = (AuthSession ss, ResponseData<EAllError> res, DelBoardForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if( userId == null || userId.length() < 1)
            return res.setError(EAllError.invalid_user);

        if( boardCommonRepository.deleteBoard(form.getScode(), form.getBoardId(), userId) == false)
            return res.setError(EAllError.FailDeleteBoard);
        res.setParam("boardId", form.getBoardId());
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, UpdateTitleForm> doUpdateBoardTitle = (AuthSession ss, ResponseData<EAllError> res, UpdateTitleForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if( userId == null || userId.length() < 1)
            return res.setError(EAllError.invalid_user);

        if(boardCommonRepository.updateBoardTitle(form.getScode(), form.getBoardId(), userId, form.getTitle()) == false)
            return res.setError(EAllError.FailUpdate);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, UpdateContentForm> doUpdateBoardContent = (AuthSession ss, ResponseData<EAllError> res, UpdateContentForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if( userId == null || userId.length() < 1)
            return res.setError(EAllError.invalid_user);

        if( boardCommonRepository.updateBoardContent(form.getScode(), form.getBoardId(), userId, getShortContent(form.getContent()), form.isHasImage(), form.isHasFile())==false)
            return res.setError(EAllError.FailUpdate);
        boardCommonRepository.updateBoardContent(form.getScode(), form.getBoardId(), userId, form.getContent());
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, UpdateCategoryForm> doUpdateBoardCategory = (AuthSession ss, ResponseData<EAllError> res, UpdateCategoryForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if( userId == null || userId.length() < 1)
            return res.setError(EAllError.invalid_user);
        
        if(boardCommonRepository.updateBoardCategory(form.getScode(), form.getBoardId(), userId, form.getCategory())==false)
            return res.setError(EAllError.FailUpdate);
        return res.setError(EAllError.ok);
    };
    
    ICommandFunction<AuthSession, ResponseData<EAllError>, UpdateBoardForm> doUpdateBoard = (AuthSession ss, ResponseData<EAllError> res, UpdateBoardForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if( userId == null || userId.length() < 1)
            return res.setError(EAllError.invalid_user);
        
        List<String> scrapIds = findAndAddScrap(form.getScode(), form.getContent());
        return updateBoard(ss, res, form, userId, scrapIds);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, BoardListForm> doGetBoardList = (AuthSession ss, ResponseData<EAllError> res, BoardListForm form) -> {
        List<BoardDetailRec> boardList = boardCommonRepository.getBoardDetailList(form.getScode(), form.getCategory(), form.getOffset(), form.getCount());	//load all list
        if(boardList.size()<1)
            return res.setError(EAllError.NoListData);

        List<String> voteBoardIds = boardList.stream().filter(x-> (x.getContentType() == EBoardContentType.vote)).map(x->x.getBoardId()).collect(Collectors.toList());
        Map<String, Integer> voteCount = boardCommonRepository.getVoteCount(form.getScode(), voteBoardIds);
        if(voteCount.size() > 0) {
            String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
            Map<String, Integer> votedIt = boardCommonRepository.getVotedBoardId(form.getScode(), userId, voteBoardIds);
            for(BoardDetailRec rec : boardList) {
                if(rec.getContentType() == EBoardContentType.vote && voteCount.containsKey(rec.getBoardId()))
                    rec.setVotes(voteCount.get(rec.getBoardId()));
                if(votedIt.containsKey(rec.getBoardId()) == true)
                    rec.setVoted(true);
            }
        }
        res.setParam("category", form.getCategory());
        res.setParam("data", boardList);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, BoardContentForm> doGetBoardContent = (AuthSession ss, ResponseData<EAllError> res, BoardContentForm form) -> {
        String content = boardCommonRepository.getBoardContent(form.getScode(), form.getBoardId());
        if(content==null || content.length() < 1)
            return res.setError(EAllError.NoData);
        List<ScrapDetailRec> scrapList = boardCommonRepository.getScrapDetailList(form.getScode(), form.getBoardId());
        boardCommonRepository.incVisit(form.getScode(), form.getBoardId());
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        BoardLikeRec rec = boardCommonRepository.getBoardVoter(form.getScode(), form.getBoardId(), userId);

        if(rec != DbRecord.Empty)
            res.setParam("like", rec);
        res.setParam("files", fileCommonRepository.getFileList(form.getScode(), form.getBoardId()));
        res.setParam("vote", boardCommonRepository.getVoteItemList(form.getScode(), form.getBoardId()));
        if(scrapList != null)
            res.setParam("scrapList", scrapList);
        return res.setError(EAllError.ok).setParam("content", content);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, BoardLikeForm> incBoardLike = (AuthSession ss, ResponseData<EAllError> res, BoardLikeForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if(form.isAdded() == true && boardCommonRepository.addBoardVoter(form.getScode(), form.getBoardId(), userId, form.getUserName(), form.getPreferences()) == false)
            return res.setError(EAllError.AlreadyLiked);
        else if(form.isAdded() == false && boardCommonRepository.deleteBoardVoter(form.getScode(), form.getBoardId(), userId, form.getPreferences()) == false)
            return res.setError(EAllError.NotExistLikedUser);

        boardCommonRepository.incLike(form.getScode(), form.getBoardId(), form.isAdded());
        BoardCountRec rec = boardCommonRepository.getBoardCounter(form.getScode(), form.getBoardId());
        res.setParam("preferences", form.getPreferences());
        res.setParam("isAdded", form.isAdded());
        if(rec!=null)
            res.setParam("count", rec);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, BoardDislikeForm> incBoardDislike = (AuthSession ss, ResponseData<EAllError> res, BoardDislikeForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if(form.isAdded() == true && boardCommonRepository.addBoardVoter(form.getScode(), form.getBoardId(), userId, form.getUserName(), form.getPreferences()) == false)
            return res.setError(EAllError.AlreadyDisliked);
        else if(form.isAdded() == false && boardCommonRepository.deleteBoardVoter(form.getScode(), form.getBoardId(), userId, form.getPreferences()) == false)
            return res.setError(EAllError.NotExistDislikeUser);

        boardCommonRepository.incDislike(form.getScode(), form.getBoardId(), form.isAdded());
        BoardCountRec rec = boardCommonRepository.getBoardCounter(form.getScode(), form.getBoardId());
        res.setParam("preferences", form.getPreferences());
        res.setParam("isAdded", form.isAdded());
        if(rec!=null)
            res.setParam("count", rec);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, AddReplyForm> addReply = (AuthSession ss, ResponseData<EAllError> res, AddReplyForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        String replyId = StrUtils.getSha1Uuid("replyId:");
        boardCommonRepository.addReply(form.getScode(), replyId, form.getBoardId(), form.getParentReplyId(), userId, form.getUserName(), form.getDepth(), form.getBody());
        List<BoardReplyRec> replyList = boardCommonRepository.getReplyList(form.getScode(), form.getBoardId(), 0, 15);
        if(replyList.size() > 0)
            res.setParam("data", replyList);
        return res.setError(EAllError.ok).setParam("replyId", replyId);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, DelReplyForm> delReply = (AuthSession ss, ResponseData<EAllError> res, DelReplyForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if(boardCommonRepository.deleteReply(form.getScode(), form.getReplyId(), userId) == false)
            return res.setError(EAllError.FailDeleteReply);
        res.setParam("replyId", form.getReplyId());
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, ReplyListForm> getReplyList = (AuthSession ss, ResponseData<EAllError> res, ReplyListForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());

        List<BoardReplyRec> replyList = boardCommonRepository.getReplyList(form.getScode(), form.getBoardId(), form.getOffset(), form.getCount());
        if(replyList.size() < 1)
            return res.setError(EAllError.NoListData);
        res.setParam("data", replyList);
        return res.setError(EAllError.ok);
    };

    //vote apis
    ICommandFunction<AuthSession, ResponseData<EAllError>, AddVoteForm> addVote = (AuthSession ss, ResponseData<EAllError> res, AddVoteForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());

        if( (res = addBoard(ss, res, form, null)).getError() != EAllError.ok)	//1. board info of vote
            return res;

        String boardId = res.getDataParam("boardId");

        if(boardCommonRepository.addVote(form.getScode(), boardId, userId, form.getUserName(), form.getExpiredAt()) == false) {
            boardCommonRepository.deleteBoard(form.getScode(), boardId, userId);
            return res.setError(EAllError.InvalidParameter);
        }
        if(form.getItemList().size() < 2) {
            boardCommonRepository.deleteBoard(form.getScode(), boardId, userId);
            boardCommonRepository.deleteVote(form.getScode(), boardId, userId);
            return res.setError(EAllError.InvalidParameter);
        }
        for(String voteItem : form.getItemList()) {
            String voteItemId = StrUtils.getSha1Uuid("voteItem:");
            boardCommonRepository.addVoteItem(form.getScode(), boardId, voteItemId, voteItem);
        }

        return res.setError(EAllError.ok).setParam("boardid", boardId);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, VoteItemListForm> getVoteItemList = (AuthSession ss, ResponseData<EAllError> res, VoteItemListForm form) -> {
        List<BoardVoteItemRec> voteItemList = boardCommonRepository.getVoteItemList(form.getScode(), form.getBoardId());
        return res.setError(EAllError.ok).setParam("data", voteItemList);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, SelectVoteItemForm> selectVoteItem = (AuthSession ss, ResponseData<EAllError> res, SelectVoteItemForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());

        BoardVoteUserRec  voteUser = boardCommonRepository.getVoteUser(form.getScode(), userId, form.getBoardId());
        if(form.isSelected() == true && voteUser != DbRecord.Empty)
            return res.setError(EAllError.AlreadyVoteUser);
        else if(form.isSelected() == false && voteUser == DbRecord.Empty)
            return res.setError(EAllError.NotExistVoteUser);
        if(form.isSelected() == true && boardCommonRepository.addVoteUser(form.getScode(), userId, form.getBoardId(), form.getVoteItemId()) == false)
            return res.setError(EAllError.FailAddVoteUser);
        else if(form.isSelected() == false && boardCommonRepository.deleteVoteUser(form.getScode(), userId, form.getBoardId()) == false)
            return res.setError(EAllError.FailDelVoteUser);

        boardCommonRepository.updateVoteSelectedItem(form.getScode(), userId, form.getBoardId(), form.getVoteItemId());
        res.setParam("vote", boardCommonRepository.getVoteItemList(form.getScode(), form.getBoardId()));
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, VoteUpdateForm> updateVote = (AuthSession ss, ResponseData<EAllError> res, VoteUpdateForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());

        BoardVoteRec rec = boardCommonRepository.getVoteInfo(form.getScode(), form.getBoardId());
        if(rec == DbRecord.Empty)
            return res.setError(EAllError.NotExistVoteInfo);
        else if(rec.getUserId().equals(ss.getUserId()) == false)
            return res.setError(EAllError.PermissionDeny);
        else if(rec.getExpiredAt().compareTo(LocalDateTime.now()) <1 || rec.isClosed() == true)
            return res.setError(EAllError.AlreadyExpired);

        if(form.getExpiredAt() != null)
            boardCommonRepository.updateVoteExpireAt(form.getScode(), form.getBoardId(), userId, form.getExpiredAt());
        if(form.getClosed() != null)
            boardCommonRepository.updateVoteClose(form.getScode(), form.getBoardId(), userId, form.getClosed());
        if(form.getVoteItems() != null) {
            for(VoteItemData voteItem : form.getVoteItems())
                boardCommonRepository.updateVoteText(form.getScode(), form.getBoardId(), voteItem.getVoteItemId(), voteItem.getVoteText());
        }
        return res.setError(EAllError.ok);
    };

    /**
     * change vote item selection
     * @param ss
     * @param res
     * @param userData, [board id][new vote item id]
     * @return
     */
    ICommandFunction<AuthSession, ResponseData<EAllError>, VoteChangeForm> changeVoteSelection = (AuthSession ss, ResponseData<EAllError> res, VoteChangeForm form) -> {
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());

        if( boardCommonRepository.updateVoteSelectedItem(form.getScode(), userId, form.getBoardId(), form.getVoteItemId()) == false )
            return res.setError(EAllError.NotExistVoteInfo);
        return res.setError(EAllError.ok).setParam(form.getScode(), form.getBoardId() +", "+ form.getVoteItemId());
    };

    /**
     * get vote info list
     * @param ss
     * @param res
     * @param userData, {[board id]}
     * @return {[board id][expire time][is closed]}, if exist
     */
    ICommandFunction<AuthSession, ResponseData<EAllError>, VoteInfoListForm> getVoteInfoList = (AuthSession ss, ResponseData<EAllError> res, VoteInfoListForm form) -> {
        List<BoardVoteRec> voteInfoList = boardCommonRepository.getVoteInfoList(form.getScode(), form.getBoardIds());
        if(voteInfoList.size() < 1)
            return res.setError(EAllError.NoListData);

        return res.setError(EAllError.ok).setParam("data", voteInfoList);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, BoardSearch> boardSearch = (AuthSession ss, ResponseData<EAllError> res, BoardSearch form) -> {
        ;
        return res.setError(EAllError.ok);
    };

    /////// private areas //////////
    private ResponseData<EAllError> addBoard(AuthSession ss, ResponseData<EAllError> res, BoardForm.AddBoardForm form, List<String> scrapIds) {
        String boardId = StrUtils.getSha1Uuid("board:");
        String userId = userCommonRepository.findUserIdByUserName(form.getScode(), form.getUserName());
        if( userId == null || userId.isEmpty() )
            return res.setError(EAllError.invalid_user);

        if( boardCommonRepository.addBoard(form.getScode(), boardId, userId, form.getUserName(), form.getTitle(),
                getShortContent(form.getContent()), form.isHasImage(), form.isHasFile(),
                form.getCategory(), form.getContentType()) == false)
            return res.setError(EAllError.FailAddBoard);

        boardCommonRepository.addBoardContent(form.getScode(), boardId, userId, form.getContent());
        boardCommonRepository.createBoardCounter(form.getScode(), boardId);

        if( form.getFileIds().size() > 0 ) {
            fileCommonRepository.updateFilesEnabled(form.getScode(), form.getFileIds(), boardId, true);
            UploadFileRec fileRec = fileCommonRepository.getFileInfo(form.getScode(), form.getFileIds().get(0));

            if(makeCrop(form.getScode(), boardId, fileRec) != null)
                fileCommonRepository.addCropFile(form.getScode(), boardId, SysUtils.getLinuxIp(), FileConfig.CROP_PATH, boardId);
        }

        if(scrapIds != null) {
            List<String> queries = new ArrayList<>();
            for(String scrapId : scrapIds)
                queries.add(boardCommonRepository.queryInsertBoardScrap(form.getScode(), boardId, scrapId));
            boardCommonRepository.multiQueries(form.getScode(), queries);
        }

        return res.setError(EAllError.ok).setParam("boardId", boardId);
    }

    private ResponseData<EAllError> updateBoard(AuthSession ss, ResponseData<EAllError> res, UpdateBoardForm form, String userId, List<String> scrapIds) {
        if(boardCommonRepository.updateBoard(form.getScode(), form.getBoardId(), userId, form.getTitle(), getShortContent(form.getContent()),
                form.isHasImage(), form.isHasFile(), form.getCategory(), form.getContentType())==false)
            return res.setError(EAllError.FailUpdate);

        boardCommonRepository.updateBoardContent(form.getScode(), form.getBoardId(), userId, form.getContent());
        fileCommonRepository.updateDeleteFile(form.getScode(), form.getBoardId());
        fileCommonRepository.updateFilesEnabled(form.getScode(), form.getFileIds(), form.getBoardId(), true);
        boardCommonRepository.deleteBoardScrap(form.getScode(), form.getBoardId());

        List<String> queries = new ArrayList<>();
        for(String scrapId : scrapIds)
            queries.add(boardCommonRepository.queryInsertBoardScrap(form.getScode(), form.getBoardId(), scrapId));
        boardCommonRepository.multiQueries(form.getScode(), queries);

        return res.setError(EAllError.ok).setParam("boardId", form.getBoardId());
    }

    private List<String> findAndAddScrap(String scode, String content) {
        List<String> list = StrUtils.extractUrls(content);
        if(list.size()<1)
            return null;
        List<HtmlNode> htmls = list.stream().map(x -> HtmlScrapper.doScrap(x)).filter(x -> x.isEmpty()==false).collect(Collectors.toList());
        List<String> urls = htmls.stream().map(x -> x.getUrl()).collect(Collectors.toList());

        List<String> dbUrls = boardCommonRepository.getScrapListByUrl(scode, urls).stream().map(x->x.getUrl()).collect(Collectors.toList());
        htmls.removeIf(x -> dbUrls.contains(x.getUrl()));

        List<String> queries = new ArrayList<>();
        for(HtmlNode html : htmls) {
            queries.add(boardCommonRepository.queryInsertScrap(scode, html.getWebScrapId(), html.getUrl(), html.getTitle(), html.getSubTitle()));
            queries.add(boardCommonRepository.queryInsertScrapBody(scode, html.getWebScrapId(), html.getShortBody()));
            boardCommonRepository.multiQueries(scode, queries);
            makeScrapImage(scode, html);
        }
        return boardCommonRepository.getScrapListByUrl(scode, urls).stream().map(x->x.getScrapId()).collect(Collectors.toList());
    }

    private final float MAX_SCRAP_SIZE = 480f;
    private void makeScrapImage(String scode, final HtmlNode htmlNode) {
        try {
            URL url = new URL(htmlNode.getImageUrl());
            String ext = FileUtils.getUrlExt(url.toString());
            String localSrc = Paths.get(FileConfig.UPLOADED_FOLDER, FileConfig.SCRAP_PATH, htmlNode.getWebScrapId(), (ext.length()>0 ? "." + ext : "")).toString();

            BufferedImage bufImg = ImageIO.read(url);
            ImageIO.write(bufImg, ext.length()>0 ? ext:"jpeg", new File(localSrc));	//ext가 blank일 경우, 일단 jpeg로 저장하고 다시 확장자 획득시도

            String resExt = (ext.length()<1) ? getFileExtFromSource(localSrc) : ext;
            String localDest = Paths.get(FileConfig.UPLOADED_FOLDER, FileConfig.CROP_PATH + htmlNode.getWebScrapId(), (resExt.length()>0 ? "." + resExt : "")).toString();

            ImageUtil.ImageSize imgSize = ImageUtil.getImageSize(localSrc);
            float rate = MAX_SCRAP_SIZE / (imgSize.getWidth() > imgSize.getHeight() ? (float)imgSize.getWidth() : (float)imgSize.getHeight());
            imageResizeWorker.doCrop(localSrc, localDest, (int)(imgSize.getWidth() * rate), (int)(imgSize.getHeight() * rate), new ImageResizeWorker.ImageResizerCallback() {
                @Override
                public void onCompleted(Object dest) {
                    boardCommonRepository.updateScrap(scode, htmlNode.getWebScrapId(), SysUtils.getLinuxIp(), FileConfig.CROP_PATH, resExt);
                    new File(localSrc).delete();
                }
                @Override
                public void onFailed(Object src) {
                    System.out.println(src);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtFromSource(String src) {
        File file = new File(src);
        try{
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            ImageReader reader = iter.next();
            String formatName = reader.getFormatName();
            System.out.println("FOUND IMAGE FORMAT :" + formatName);
            iis.close();
            return formatName.toLowerCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String makeCrop(String scode, String boardId, UploadFileRec fileRec) {
        if(fileRec==null || fileRec.getThumbName()==null)
            return null;
        String thumbPath = UploadFile.getThumbPath(scode, FileConfig.UPLOADED_FOLDER);
        String cropPath = UploadFile.getCropPath(scode, FileConfig.UPLOADED_FOLDER);
        File cropDir = new File(cropPath);
        if(cropDir.exists()==false)
            cropDir.mkdirs();

        imageResizeWorker.doCrop(thumbPath + fileRec.getThumbName(), cropPath + boardId, fileRec.getThumbWidth()/2, fileRec.getThumbHeight()/2, new ImageResizeWorker.ImageResizerCallback() {
            @Override
            public void onCompleted(Object dest) {
                System.out.println(dest);
            }
            @Override
            public void onFailed(Object src) {
                System.out.println(src);
            }
        });
        return cropPath + boardId;
    }
}
