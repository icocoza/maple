package com.ccz.modules.repository.db.admin;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminAppRec extends DbRecord {

    private String appId;
    private String uid;
    private String scode;
    private String title;
    private String token;
    private String description;
    private EAdminAppStatus status;
    private String fcmId;
    private String fcmKey;

    private String dbHost;
    private String dbOptions;
    private String dbUserId;
    private String dbPassword;
    private int dbInitConn;
    private int dbMaxConn;

    private LocalDateTime createdAt;
    private LocalDateTime statusAt;


    public AdminAppRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        AdminAppRec rec = (AdminAppRec)r;
        rec.appId = rd.getString("appId");
        rec.uid = rd.getString("uid");
        rec.scode = rd.getString("scode");
        rec.title = rd.getString("title");
        rec.token = rd.getString("token");
        rec.description = rd.getString("description");
        rec.status = EAdminAppStatus.getType(rd.getString("status"));
        rec.fcmId = rd.getString("fcmId");
        rec.fcmKey = rd.getString("fcmKey");

        rec.dbHost = rd.getString("dbHost");
        rec.dbOptions = rd.getString("dbOptions");
        rec.dbUserId = rd.getString("dbUserId");
        rec.dbPassword = rd.getString("dbPassword");
        rec.dbInitConn = rd.getInt("dbInitConn");
        rec.dbMaxConn = rd.getInt("dbMaxConn");

        rec.createdAt = rd.getLocalDateTime("createdAt");
        rec.statusAt = rd.getLocalDateTime("statusAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new AdminAppRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String scode, String appId, String uid, String title, String token,
                          String description, EAdminAppStatus status, String fcmId, String fcmKey) {
        String sql = String.format("INSERT INTO adminApp (appId, uid, scode, title, token, description, status, fcmId, fcmKey) "
                          + "VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                          appId, uid, scode, title, token, description, status, fcmId, fcmKey);
        return super.insert(sql);
    }

    public boolean insert(String scode, String appId, String uid, String title, String token,
                          String description, EAdminAppStatus status) {
        String sql = String.format("INSERT INTO adminApp (appId, uid, scode, title, token, description, status) "
                        + "VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                appId, uid, scode, title, token, description, status, fcmId, fcmKey);
        return super.insert(sql);
    }

    public AdminAppRec getApp(String appId) {
        String sql = String.format("SELECT * FROM adminApp WHERE appId='%s'", appId);
        return (AdminAppRec) super.getOne(sql);
    }

    public AdminAppRec getApp(String uid, String scode) {
        String sql = String.format("SELECT * FROM adminApp WHERE uid='%s' AND scode='%s'", uid, scode);
        return (AdminAppRec) super.getOne(sql);
    }

    public AdminAppRec getAppByScode(String scode) {
        String sql = String.format("SELECT * FROM adminApp WHERE scode='%s'", scode);
        return (AdminAppRec) super.getOne(sql);
    }

    public boolean updateApp(String uid, String scode, String title,
                             String description, EAdminAppStatus status, String fcmId, String fcmKey) {
        String sql = String.format("UPDATE adminApp SET title='%s', description='%s', "
                        + "status='%s', statusAt=now(), fcmId='%s', fcmKey='%s' "
                        + "WHERE uid='%s' AND scode='%s'",
                title, description, status.getValue(), fcmId, fcmKey, uid, scode);
        return super.update(sql);
    }

    public boolean updatePush(String uid, String scode, String fcmId, String fcmKey) {
        String sql = String.format("UPDATE adminApp SET fcmId='%s', fcmKey='%s', statusAt=now() WHERE uid='%s' AND scode='%s'", fcmId, fcmKey, uid, scode, appId);
        return super.update(sql);
    }

    public boolean updateStatus(String uid, String scode, EAdminAppStatus status) {
        String sql = String.format("UPDATE adminApp SET status='%s', statusAt=now() WHERE uid='%s' AND scode='%s'", status.getValue(), uid, scode, appId);
        return super.update(sql);
    }

    public boolean updateExternalDbInfo(String uid, String scode, String dbHost, String dbOptions, String dbUserId, String dbPassword) {
        String sql = String.format("UPDATE adminApp SET dbHost='%s', dbOptions='%s', dbUserId='%s', dbPassword='%s' " +
                "WHERE uid='%s' AND scode='%s'", dbHost, dbOptions, dbUserId, dbPassword, uid, scode);
        return super.update(sql);
    }

    public List<AdminAppRec> getList(EAdminAppStatus status, int offset, int count) {
        String sql = String.format("SELECT * FROM adminApp ORDER BY statusAt DESC LIMIT %d, %d",
                offset, count);
        if(EAdminAppStatus.ALL != status)
            sql = String.format("SELECT * FROM adminApp WHERE status='%s' ORDER BY statusAt DESC LIMIT %d, %d",
                    status.getValue(), offset, count);
        return super.getList(sql).stream().map(e->(AdminAppRec)e).collect(Collectors.toList());
    }

    public List<AdminAppRec> getList(String uid, EAdminAppStatus status, int offset, int count) {
        String sql = String.format("SELECT * FROM adminApp WHERE uid='%s' ORDER BY statusAt DESC LIMIT %d, %d",
                uid, offset, count);
        if(EAdminAppStatus.ALL != status)
            sql = String.format("SELECT * FROM adminApp WHERE uid='%s' AND status='%s' ORDER BY statusAt DESC LIMIT %d, %d",
                    uid, status.getValue(), offset, count);
        return super.getList(sql).stream().map(e->(AdminAppRec)e).collect(Collectors.toList());
    }

    public int getAppCount(String uid, EAdminAppStatus status) {
        if(EAdminAppStatus.ALL == status)
            return super.count(String.format("SELECT COUNT(*) FROM adminApp WHERE uid='%s'", uid));
        return super.count(String.format("SELECT COUNT(*) FROM adminApp WHERE uid='%s' AND status='%s'", uid, status.getValue()));
    }

    public boolean hasSCode(String scode) {
        String sql = String.format("SELECT * FROM adminApp WHERE scode='%s'", scode);
        return super.exist(sql);
    }

}
