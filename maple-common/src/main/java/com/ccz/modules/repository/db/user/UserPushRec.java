package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserPushRec extends DbRecord {

	private String deviceUuid;
	private String userId;
	private String epid;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	
	public UserPushRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		UserPushRec rec = (UserPushRec)r;
		rec.deviceUuid = rd.getString("deviceUuid");
		rec.userId = rd.getString("userId");
		rec.epid = rd.getString("epid");
		rec.createdAt = rd.getLocalDateTime("createdAt");
		rec.modifiedAt = rd.getLocalDateTime("modifiedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new UserPushRec(poolName));
	}

	public DbRecord insert(String deviceUuid, String userId, String epid) {
		this.deviceUuid = deviceUuid;
		this.userId = userId;
		this.epid = epid;
		
		String sql = String.format("INSERT INTO pushEpid (deviceUuid, userId, epid) VALUES('%s', '%s', '%s') "
				+ "ON DUPLICATE KEY UPDATE userId='%s', epid='%s'", deviceUuid, userId, epid, userId, epid);
		return super.insert(sql) ? this : DbRecord.Empty;
	}
	
	public boolean delete(String deviceUuid) {
		String sql = String.format("DELETE FROM pushEpid WHERE deviceUuid='%s'", deviceUuid);
		return super.delete(sql);
	}

	public UserPushRec getEpid(String deviceUuid) {
		String sql = String.format("SELECT * FROM pushEpid WHERE deviceUuid='%s'", deviceUuid);
		return (UserPushRec) super.getOne(sql);
	}
	
	public boolean updateEpid(String deviceUuid, String epid) {
		String sql = String.format("UPDATE pushEpid SET epid='%s' WHERE deviceUuid='%s'", epid, deviceUuid);
		return super.update(sql);
	}

}
