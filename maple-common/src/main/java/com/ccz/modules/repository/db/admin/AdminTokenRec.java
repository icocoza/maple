package com.ccz.modules.repository.db.admin;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EAdminStatus;
import com.ccz.modules.domain.constant.EUserRole;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminTokenRec extends DbRecord {

    private String email;
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
        rec.email = rd.getString("email");
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
    public boolean upsert(String email, String token, String remoteIp) {
        if(getToken(email)==null)
            return insert(email, token, remoteIp);
        else
            return update(email, token, remoteIp);
    }

    public boolean insert(String email, String token, String remoteIp) {
        this.email = email;
        this.token = token;
        this.remoteIp = remoteIp;
        String sql = String.format("INSERT INTO adminToken (email, token, remoteIp) "
                + "VALUES('%s', '%s', '%s')", email, token, remoteIp);
        return super.insert(sql);
    }
    public boolean update(String email, String token, String remoteIp) {
        String sql = String.format("UPDATE adminApp SET token='%s', remoteIp='%s', issuedate=now(), lastAt=now() WHERE email='%s'",
                token, remoteIp, email);
        return super.update(sql);
    }

    public boolean update(String email, String token) {
        String sql = String.format("UPDATE adminToken SET token='%s', lastAt=now() WHERE email='%s'",
                token, email);
        return super.update(sql);
    }

    public boolean delete(String email) {
        String sql = String.format("DELETE FROM adminToken WHERE email='%s'", email);
        return super.delete(sql);
    }

    public AdminTokenRec getToken(String email) {
        String sql = String.format("SELECT * FROM adminToken WHERE email='%s'", email);
        return (AdminTokenRec) super.getOne(sql);
    }

    public boolean updateLasttime(String email) {
        String sql = String.format("UPDATE adminToken SET lastAt=now() WHERE email='%s'", email);
        return super.update(sql);
    }

    public boolean isAvailableToken(String email, String token, String remoteIp) {
        String sql = String.format("SELECT * FROM adminToken WHERE email='%s' AND token='%s' AND remoteIp='%s'", email, token , remoteIp);
        return (AdminTokenRec) super.getOne(sql) != AdminTokenRec.Empty;
    }

}
