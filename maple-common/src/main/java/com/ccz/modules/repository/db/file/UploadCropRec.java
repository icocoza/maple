package com.ccz.modules.repository.db.file;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;

public class UploadCropRec extends DbRecord {

	public String boardId;
	public String serverIp;
	public String subPath;
	public String fileName;

	public UploadCropRec(String poolName) {
		super(poolName);
	}
	
	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		UploadCropRec rec = (UploadCropRec)r;
		rec.boardId = rd.getString("boardId");
		rec.fileName = rd.getString("fileName");
		rec.serverIp = rd.getString("serverIp");
		rec.subPath = rd.getString("subPath");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new UploadCropRec(poolName));
	}
	
	public DbRecord insertCropFile(String boardId, String serverIp, String subPath, String fileName) {
		this.boardId = boardId;
		this.serverIp = serverIp;
		this.subPath = subPath;
		this.fileName = fileName;
		String sql = String.format("INSERT INTO uploadCrop (boardId, serverIp, subPath, fileName) VALUES('%s', '%s', '%s', '%s')", 
									boardId, serverIp, subPath, fileName);
		return super.insert(sql) ? this : DbRecord.Empty;
	}
	
	public UploadCropRec getFile(String boardId) {
		String sql = String.format("SELECT * FROM uploadCrop WHERE boardId='%s'", boardId);
		return (UploadCropRec) super.getOne(sql);
	}
}
