package com.ccz.modules.controller.file;

import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.common.utils.FileUtils;
import com.ccz.modules.common.utils.ImageResizeWorker;
import com.ccz.modules.common.utils.ImageUtil;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.constant.EFileCmd;
import com.ccz.modules.domain.constant.EUploadFileType;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.session.AuthSession;
import com.ccz.modules.repository.db.file.FileCommonRepository;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import com.ccz.modules.server.config.FileConfig;
import com.ccz.modules.controller.file.FileForm.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileCommandCommonAction extends CommandAction {
    FileCommonRepository fileCommonRepository;
    UserCommonRepository userCommonRepository;

    ImageResizeWorker imageResizeWorker = new ImageResizeWorker();

    @Override
    public void initCommandFunctions(CommonRepository fileCommonRepository) {
        this.fileCommonRepository = (FileCommonRepository) fileCommonRepository;
        super.setCommandFunction(makeCommandId(EFileCmd.uploadFile), doUploadFile);
    }

    public void initCommandFunctions(CommonRepository fileCommonRepository, CommonRepository userCommonRepository) {
        this.initCommandFunctions(fileCommonRepository);
        this.userCommonRepository = (UserCommonRepository) userCommonRepository;
        super.setCommandFunction(makeCommandId(EFileCmd.uploadFile), doUploadFile);
        super.setCommandFunction(makeCommandId(EFileCmd.multiUploadFile), doMultiUploadFile);
    }

    @Override
    public String makeCommandId(Enum e) {
        return fileCommonRepository.getPoolName() + ":" + e.name();
    }

    ICommandFunction<AuthSession, ResponseData<EAllError>, FileForm.UploadForm> doUploadFile = (AuthSession authSession, ResponseData<EAllError> res, FileForm.UploadForm form) -> {
        List<FileResult> fileIds = new ArrayList<>();
        String fileId = uploadFile(form.getMultipartFile(), form.getUserName(), form.getUploadDir(), form.getServerIp(), form.getComment());
        fileIds.add(new FileForm().new FileResult(form.getMultipartFile().getOriginalFilename(), fileId));
        res.setParam("fileIds", fileIds);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, FileForm.MultiUploadForm> doMultiUploadFile = (AuthSession authSession, ResponseData<EAllError> res, FileForm.MultiUploadForm form) -> {
        List<MultipartFile> files = form.getMultipartFiles();
        List<FileResult> fileIds = new ArrayList<>();
        for(MultipartFile file : files) {
            String fileId = uploadFile(file, form.getUserName(), form.getUploadDir(), form.getServerIp(), form.getComment());
            fileIds.add(new FileForm().new FileResult(file.getOriginalFilename(), fileId));
        }
        res.setParam("fileIds", fileIds);
        return res.setError(EAllError.ok);
    };


    private String uploadFile(MultipartFile file, String userName, String uploadDir, String serverIp, String comment) {
        String fileId = StrUtils.getUuid("file");
        Path savePath = Paths.get(uploadDir, fileCommonRepository.getPoolName(), fileId);

        try {
            file.transferTo(savePath);
        } catch (IOException e) {
            return null;
        }

        List<String> queries = new ArrayList<>();

        String userId = userCommonRepository.findUserIdByUserName(userName);
        Long fileSize = file.getSize();
        EUploadFileType eType = EUploadFileType.getType(FileUtils.getFileExt(file.getOriginalFilename()));;
        queries.add(fileCommonRepository.queryInitFileInfo(fileId, userId, serverIp, userName, eType.getValue(), fileSize, comment));

        ImageUtil.ImageSize imageSize = null;
        try {
            imageSize = ImageUtil.getImageSize(new File(savePath.getFileName().toString()));
        } catch (IOException e) {
            return null;
        }
        queries.add(fileCommonRepository.queryUpdateFileInfo(fileId, imageSize.getWidth(), imageSize.getWidth(), fileSize));


        float rate = imageSize.getWidth() > imageSize.getHeight()? FileConfig.THUMB_SIZE / (float)imageSize.getWidth() : FileConfig.THUMB_SIZE / (float)imageSize.getHeight();
        String thumbName = newThumbFilename();
        int thumbWidth = (int)(imageSize.getWidth() * rate);
        int thumbHeight = (int)(imageSize.getHeight() * rate);
        String thumbFilePath = getThumbPath(thumbName);

        imageResizeWorker.doResize(savePath.toString(), thumbFilePath, thumbWidth, thumbHeight, new ImageResizeWorker.ImageResizerCallback() {
            @Override
            public void onCompleted(Object dest) {
                log.error("Completed - " + dest);
            }

            @Override
            public void onFailed(Object src) {
                log.error("Failed - " + src);
            }
        });
        queries.add(fileCommonRepository.queryUpdateThumbnail(fileId, thumbName, thumbWidth, thumbHeight));
        fileCommonRepository.multiQueries(queries);
        return fileId;
    }

    private static int seq = 0;
    private String newThumbFilename() {
        return String.format("%s%d_%03d", FileConfig.THUMB_PATH, System.currentTimeMillis(), ++seq % 1000);
    }

    private String getThumbPath(String fileName) {
        return Paths.get(FileConfig.UPLOADED_FOLDER, fileCommonRepository.getPoolName(), FileConfig.THUMB_PATH, fileName).toString();
    }

}
