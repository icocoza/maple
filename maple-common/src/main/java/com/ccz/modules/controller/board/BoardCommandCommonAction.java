package com.ccz.modules.controller.board;

import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.action.SessionManager;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.controller.file.FileForm;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.session.AuthSession;
import com.ccz.modules.repository.db.board.BoardCommonRepository;

import java.util.ArrayList;
import java.util.List;

public class BoardCommandCommonAction  extends CommandAction {
    SessionManager sessionManager;
    BoardCommonRepository boardCommonRepository;

    @Override
    public void initCommandFunctions(CommonRepository commonRepository) {
        boardCommonRepository = (BoardCommonRepository) commonRepository;

    }

    @Override
    public String makeCommandId(Enum e) {
        return null;
    }

    ICommandFunction<AuthSession, ResponseData<EAllError>, BoardForm.AddBoardForm> doAddBoard = (AuthSession authSession, ResponseData<EAllError> res, BoardForm.AddBoardForm form) -> {
//        List<FileForm.FileResult> fileIds = new ArrayList<>();
//        String fileId = uploadFile(form.getMultipartFile(), form.getUserName(), form.getUploadDir(), form.getServerIp(), form.getComment());
//        fileIds.add(new FileForm().new FileResult(form.getMultipartFile().getOriginalFilename(), fileId));
//        res.setParam("fileIds", fileIds);
        return res.setError(EAllError.ok);
    };
}
