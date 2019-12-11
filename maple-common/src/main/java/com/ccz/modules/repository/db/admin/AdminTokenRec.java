package com.ccz.modules.repository.db.admin;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EAdminStatus;
import com.ccz.modules.domain.constant.EUserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminTokenRec extends DbRecord {

    private String uid;
    private String token;
    private String remoteIp;
    private String reserved;
    private LocalDateTime createdAt;
    private LocalDateTime lastAt;

    public AdminTokenRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        AdminTokenRec rec = (AdminTokenRec)r;
        rec.uid = rd.getString("uid");
        rec.token = rd.getString("token");
        rec.remoteIp = rd.getString("remoteIp");
        rec.reserved = rd.getString("reserved");
        rec.createdAt = rd.getLocalDateTime("createdAt");
        rec.lastAt = rd.getLocalDateTime("lastAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new AdminTokenRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean upsert(String uid, String token, String remoteIp) {
        if(getToken(uid)==null)
            return insert(uid, token, remoteIp);
        else
            return update(uid, token, remoteIp);
    }

    public boolean insert(String uid, String token, String remoteIp) {
        this.uid = uid;
        this.token = token;
        this.remoteIp = remoteIp;
        String sql = String.format("INSERT INTO adminToken (uid, token, remoteIp) "
                + "VALUES('%s', '%s', '%s')", uid, token, remoteIp);
        return super.insert(sql);
    }
    public boolean update(String uid, String token, String remoteIp) {
        String sql = String.format("UPDATE adminApp SET token='%s', remoteIp='%s', issuedate=now(), lastAt=now() WHERE uid='%s'",
                token, remoteIp, uid);
        return super.update(sql);
    }

    public boolean update(String uid, String token) {
        String sql = String.format("UPDATE adminToken SET token='%s', lastAt=now() WHERE uid='%s'",
                token, uid);
        return super.update(sql);
    }

    public boolean delete(String uid) {
        String sql = String.format("DELETE FROM adminToken WHERE uid='%s'", uid);
        return super.delete(sql);
    }

    public AdminTokenRec getToken(String uid) {
        String sql = String.format("SELECT * FROM adminToken WHERE uid='%s'", uid);
        return (AdminTokenRec) super.getOne(sql);
    }

    public boolean updateLasttime(String uid) {
        String sql = String.format("UPDATE adminToken SET lastAt=now() WHERE uid='%s'", uid);
        return super.update(sql);
    }

    public boolean isAvailableToken(String uid, String token) {
        String sql = String.format("SELECT * FROM adminToken WHERE uid='%s' AND token='%s' ORDER BY issuedAt DESC LIMIT 1", uid, token);
        return (AdminTokenRec) super.getOne(sql) != AdminTokenRec.Empty;
    }

}
