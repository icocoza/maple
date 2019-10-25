package com.ccz.modules.repository.db.file;

import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public class FileCommonRepository  extends CommonRepository {

    public boolean initFileInfo(String scode, String fileId, String userId, String fileServer, String fileName, String fileType, long size, String comment) {
        return new UploadFileRec(scode).initFileInfo(fileId, userId, fileServer, fileName, fileType, size, comment) != DbRecord.Empty;
    }

    public String queryInitFileInfo(String scode, String fileId, String userId, String fileServer, String fileName, String fileType, long size, String comment) {
        return UploadFileRec.qInitFileInfo(fileId, userId, fileServer, fileName, fileType, size, comment);
    }

    public boolean updateFileInfo(String scode, String fileId, int width, int height, long size) {
        return new UploadFileRec(scode).updateFileInfo(fileId, width, height, size) != DbRecord.Empty;
    }

    public String queryUpdateFileInfo(String scode, String fileId, int width, int height, long size) {
        return UploadFileRec.qUpdateFileInfo(fileId, width, height, size);
    }

    public boolean updateThumbnail(String scode, String fileId, String thumbName, int thumbWidth, int thumbHeight) {
        return new UploadFileRec(scode).updateThumbnail(fileId, thumbName, thumbWidth, thumbHeight);
    }

    public String queryUpdateThumbnail(String scode, String fileId, String thumbName, int thumbWidth, int thumbHeight) {
        return UploadFileRec.qUpdateThumbnail(fileId, thumbName, thumbWidth, thumbHeight);
    }

    public boolean delFileInfo(String scode, String fileId) {
        return new UploadFileRec(scode).delete(fileId);
    }

    public UploadFileRec getFileInfo(String scode, String fileId) {
        return new UploadFileRec(scode).getFile(fileId);
    }

    public List<UploadFileRec> getFileList(String scode, String boardId) {
        return new UploadFileRec(scode).getFileList(boardId);
    }

    public boolean updateFileEnabled(String scode, String fileId, String boardId, boolean enabled) {
        return new UploadFileRec(scode).updateFileEnabled(fileId, boardId, enabled);
    }

    public boolean updateFilesEnabled(String scode, List<String> fileIds, String boardId, boolean enabled) {
        return new UploadFileRec(scode).updateFilesEnabled(fileIds, boardId, enabled);
    }

    public boolean updateDeleteFile(String scode, String boardId) {
        return new UploadFileRec(scode).updateDeleteFile(boardId);
    }

    //for file crop
    public DbRecord addCropFile(String scode, String boardId, String serverIp, String subPath, String fileName) {
        return new UploadCropRec(scode).insertCropFile(boardId, serverIp, subPath, fileName);
    }

}
