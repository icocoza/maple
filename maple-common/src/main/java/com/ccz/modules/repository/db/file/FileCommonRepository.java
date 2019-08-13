package com.ccz.modules.repository.db.file;

import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;

import java.util.List;

public abstract class FileCommonRepository  extends CommonRepository {

    public boolean initFileInfo(String fileId, String userId, String fileServer, String fileName, String fileType, long size, String comment) {
        return new UploadFileRec(getPoolName()).initFileInfo(fileId, userId, fileServer, fileName, fileType, size, comment) != DbRecord.Empty;
    }

    public String queryInitFileInfo(String fileId, String userId, String fileServer, String fileName, String fileType, long size, String comment) {
        return UploadFileRec.qInitFileInfo(fileId, userId, fileServer, fileName, fileType, size, comment);
    }

    public boolean updateFileInfo(String fileId, int width, int height, long size) {
        return new UploadFileRec(getPoolName()).updateFileInfo(fileId, width, height, size) != DbRecord.Empty;
    }

    public String queryUpdateFileInfo(String fileId, int width, int height, long size) {
        return UploadFileRec.qUpdateFileInfo(fileId, width, height, size);
    }

    public boolean updateThumbnail(String fileId, String thumbName, int thumbWidth, int thumbHeight) {
        return new UploadFileRec(getPoolName()).updateThumbnail(fileId, thumbName, thumbWidth, thumbHeight);
    }
    public String queryUpdateThumbnail(String fileId, String thumbName, int thumbWidth, int thumbHeight) {
        return UploadFileRec.qUpdateThumbnail(fileId, thumbName, thumbWidth, thumbHeight);
    }

    public boolean delFileInfo(String fileId) {
        return new UploadFileRec(getPoolName()).delete(fileId);
    }
    public UploadFileRec getFileInfo(String fileId) {
        return new UploadFileRec(getPoolName()).getFile(fileId);
    }
    public List<UploadFileRec> getFileList(String boardId) {
        return new UploadFileRec(getPoolName()).getFileList(boardId);
    }
    public boolean updateFileEnabled(String fileId, String boardId, boolean enabled) {
        return new UploadFileRec(getPoolName()).updateFileEnabled(fileId, boardId, enabled);
    }
    public boolean updateFilesEnabled(List<String> fileIds, String boardId, boolean enabled) {
        return new UploadFileRec(getPoolName()).updateFilesEnabled(fileIds, boardId, enabled);
    }
    public boolean updateDeleteFile(String boardId) {
        return new UploadFileRec(getPoolName()).updateDeleteFile(boardId);
    }
    //for file crop
    public DbRecord addCropFile(String boardId, String serverIp, String subPath, String fileName) {
        return new UploadCropRec(getPoolName()).insertCropFile(boardId, serverIp, subPath, fileName);
    }

}
