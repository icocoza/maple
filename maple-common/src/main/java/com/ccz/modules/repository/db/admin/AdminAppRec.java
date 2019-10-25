package com.ccz.modules.repository.db.admin;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AdminAppRec extends DbRecord {

    private String applicationId;
    private String email;
    private String scode;
    private String title;
    private String token;
    private String version;
    private String description;
    private EAdminAppStatus appStatus;
    private String fcmId;
    private String fcmKey;
    private String storeUrl;
    private boolean updateForce;

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
        rec.applicationId = rd.getString("applicationId");
        rec.email = rd.getString("email");
        rec.scode = rd.getString("scode");
        rec.title = rd.getString("title");
        rec.token = rd.getString("token");
        rec.version = rd.getString("version");
        rec.description = rd.getString("description");
        rec.appStatus = EAdminAppStatus.getType(rd.getString("appStatus"));
        rec.fcmId = rd.getString("fcmId");
        rec.fcmKey = rd.getString("fcmKey");
        rec.storeUrl = rd.getString("storeUrl");
        rec.updateForce = rd.getBoolean("updateForce");

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
    public boolean insert(String scode, String applicationId, String email, String title, String token,
                          String version, String description, EAdminAppStatus appStatus, String fcmId, String fcmKey,
                          String storeUrl, boolean updateForce) {
        String sql = String.format("INSERT INTO adminApp (applicationId, email, scode, title, token, version, description, appStatus, fcmId, fcmKey, storeUrl, updateForce) "
                          + "VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %b)",
                          applicationId, email, scode, title, token, version, description, appStatus, fcmId, fcmKey, storeUrl, updateForce);
        return super.insert(sql);
    }

    public AdminAppRec getApp(String applicationId) {
        String sql = String.format("SELECT * FROM adminApp WHERE applicationId='%s'", applicationId);
        return (AdminAppRec) super.getOne(sql);
    }

    public AdminAppRec getApp(String email, String scode) {
        String sql = String.format("SELECT * FROM adminApp WHERE email='%s' AND scode='%s'", email, scode);
        return (AdminAppRec) super.getOne(sql);
    }

    public AdminAppRec getAppByScode(String scode) {
        String sql = String.format("SELECT * FROM adminApp WHERE scode='%s'", scode);
        return (AdminAppRec) super.getOne(sql);
    }

    public boolean updateApp(String email, String applicationId, String title, String version, boolean updateForce, String storeUrl,
                             String description, EAdminAppStatus appStatus, String fcmId, String fcmKey) {
        String sql = String.format("UPDATE adminApp SET title='%s', version='%s', updateForce=%b, storeUrl='%s', description='%s', "
                        + "appStatus='%s', statusAt=now(), fcmId='%s', fcmKey='%s' "
                        + "WHERE email='%s' AND applicationId='%s'", 
                title, version, updateForce, storeUrl, description, appStatus.getValue(), fcmId, fcmKey, email, applicationId);
        return super.update(sql);
    }

    public boolean updateStatus(String email, String applicationId, EAdminAppStatus appStatus) {
        String sql = String.format("UPDATE adminApp SET appStatus='%s', statusAt=now() WHERE email='%s' AND applicationId='%s'", appStatus.getValue(), email, applicationId);
        return super.update(sql);
    }

    public boolean updateExternalDbInfo(String email, String scode, String dbHost, String dbOptions, String dbUserId, String dbPassword) {
        String sql = String.format("UPDATE adminApp SET dbHost='%s', dbOptions='%s', dbUserId='%s', dbPassword='%s' " +
                "WHERE email='%s' AND scode='%s'", dbHost, dbOptions, dbUserId, dbPassword, email, scode);
        return super.update(sql);
    }

    public List<AdminAppRec> getList(EAdminAppStatus appStatus, int offset, int count) {
        String sql = String.format("SELECT * FROM adminApp ORDER BY statusAt DESC LIMIT %d, %d",
                offset, count);
        if(EAdminAppStatus.all != appStatus)
            sql = String.format("SELECT * FROM adminApp WHERE appStatus='%s' ORDER BY statusAt DESC LIMIT %d, %d",
                    appStatus.getValue(), offset, count);
        return super.getList(sql).stream().map(e->(AdminAppRec)e).collect(Collectors.toList());
    }

    public List<AdminAppRec> getList(String email, EAdminAppStatus appStatus, int offset, int count) {
        String sql = String.format("SELECT * FROM adminApp WHERE email='%s' ORDER BY statusAt DESC LIMIT %d, %d",
                email, offset, count);
        if(EAdminAppStatus.all != appStatus)
            sql = String.format("SELECT * FROM adminApp WHERE email='%s' AND appStatus='%s' ORDER BY statusAt DESC LIMIT %d, %d",
                    email, appStatus.getValue(), offset, count);
        return super.getList(sql).stream().map(e->(AdminAppRec)e).collect(Collectors.toList());
    }

    public int getAppCount(String email, EAdminAppStatus appStatus) {
        if(EAdminAppStatus.all == appStatus)
            return super.count(String.format("SELECT COUNT(*) FROM adminApp WHERE email='%s'", email));
        return super.count(String.format("SELECT COUNT(*) FROM adminApp WHERE email='%s' AND appStatus='%s'", email, appStatus.getValue()));
    }

    public boolean hasSCode(String scode) {
        String sql = String.format("SELECT * FROM adminApp WHERE scode='%s'", scode);
        return super.exist(sql);

    }

}
