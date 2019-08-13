package com.ccz.modules.repository.db.file;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UploadFileRec extends DbRecord {

	private String fileId;
	private String userId;
	private String boardId;
	private String fileName;
	private String thumbName;
	private String fileType;
	private String fileServer; //file server ip
	@Setter
	private int width, height;
	private int thumbWidth, thumbHeight;
	private long fileSize;
	private boolean uploaded, enabled;
	private String comment;
    private LocalDateTime uploadedAt;
	private LocalDateTime deletedAt;

	public UploadFileRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		UploadFileRec rec = (UploadFileRec)r;
		rec.fileId = rd.getString("fileId");
		rec.userId = rd.getString("userId");
		rec.boardId = rd.getString("boardId");
		rec.fileName = rd.getString("fileName");
		rec.thumbName = rd.getString("thumbName");
		rec.fileType = rd.getString("fileType");
		rec.fileServer = rd.getString("fileServer");
		rec.width = rd.getInt("width");
		rec.height = rd.getInt("height");
		rec.thumbWidth = rd.getInt("thumbWidth");
		rec.thumbHeight = rd.getInt("thumbHeight");
		rec.fileSize = rd.getLong("fileSize");
		rec.uploaded = rd.getBoolean("uploaded");
		rec.enabled = rd.getBoolean("enabled");
		rec.comment = rd.getString("comment");
		rec.uploadedAt = rd.getLocalDateTime("uploadedAt");
		rec.deletedAt = rd.getLocalDateTime("deletedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new UploadFileRec(poolName));
	}

	public DbRecord initFileInfo(String fileId, String userId, String fileServer, String fileName, String fileType, long fileSize, String comment) {
		this.fileId = fileId;
		this.userId = userId;
		this.fileServer = fileServer;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.comment = comment;
		String sql = qInitFileInfo(fileId, userId, fileServer, fileName, fileType, fileSize, comment);
		return super.insert(sql) ? this : DbRecord.Empty;
	}

	public static String qInitFileInfo(String fileId, String userId, String fileServer, String fileName, String fileType, long fileSize, String comment) {
		return String.format("INSERT INTO uploadfile (fileId, userId, fileServer, fileName, fileType, fileSize, comment) VALUES('%s', '%s', '%s', '%s', '%s', %d, '%s')",
				fileId, userId, fileServer, fileName, fileType, fileSize, comment);
	}
	
	public DbRecord updateFileInfo(String fileId, int width, int height, long fileSize) {
		this.fileId = fileId;
		this.width = width;
		this.height = height;
		this.fileSize = fileSize;
		
		String sql = qUpdateFileInfo(fileId, width, height, fileSize);
		return super.insert(sql) ? this : DbRecord.Empty;
	}

	public static String qUpdateFileInfo(String fileId, int width, int height, long fileSize) {
		return String.format("UPDATE uploadfile SET width=%d, height=%d, fileSize=%d, uploaded=true WHERE fileId='%s'",
				width, height, fileSize, fileId);
	}
	
	public boolean updateFileEnabled(String fileId, String boardId, boolean enabled) {
		String sql = String.format("UPDATE uploadfile SET boardId='%s', enabled=%b, deleted=false, deletedAt=null WHERE fileId='%s' AND uploaded=true", boardId, enabled, fileId);
		return super.update(sql);
	}
	public boolean updateFilesEnabled(List<String> fileids, String boardId, boolean enabled) {
		String filestr = fileids.stream().map(x -> "'"+x+"'").collect(Collectors.joining(","));
		String sql = String.format("UPDATE uploadfile SET boardId='%s', enabled=%b, deleted=false, deletedAt=null WHERE fileId IN(%s) AND uploaded=true", boardId, enabled, filestr);
		return super.update(sql);
	}

	public boolean updateThumbnail(String fileId, String thumbName, int thumbWidth, int thumbHeight) {
		String sql = qUpdateThumbnail(fileId, thumbName, thumbWidth, thumbHeight);
		return super.update(sql);
	}

	public static String qUpdateThumbnail(String fileId, String thumbName, int thumbWidth, int thumbHeight) {
		return String.format("UPDATE uploadfile SET thumbName='%s', thumbWidth=%d, thumbHeight=%d WHERE fileId='%s'",
				thumbName, thumbWidth, thumbHeight, fileId);
	}

	public boolean updateDeleteFile(String boardId) {
		String sql = String.format("UPDATE uploadfile SET deleted=true, deletedAt=NOW() WHERE boardId='%s'", boardId);
		return super.update(sql);
	}

	public boolean delete(String fileId) {
		String sql = String.format("DELETE FROM uploadfile WHERE fileId='%s'", fileId);
		return super.delete(sql);
	}

	public UploadFileRec getFile(String fileId) {
		String sql = String.format("SELECT * FROM uploadfile WHERE fileId='%s'", fileId);
		return (UploadFileRec) super.getOne(sql);
	}
	
	public List<UploadFileRec> getFileList(String boardId) {
		String sql = String.format("SELECT * FROM uploadfile WHERE boardId='%s' AND deleted=false", boardId);
		return super.getList(sql).stream().map(e -> (UploadFileRec)e).collect(Collectors.toList());
	}
}
