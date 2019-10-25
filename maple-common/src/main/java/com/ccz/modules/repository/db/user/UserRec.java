package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class UserRec extends DbRecord {

    private String userId;
    private String userName;
    private boolean anonymous;
    private String osType;
    private String osVersion;
    private String  appVersion;
    private String inAppcode;
    private LocalDateTime createdAt;
    private LocalDateTime leftAt;
    private LocalDateTime lastAt;
    private int likes, dislikes;

    public UserRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        UserRec rec = (UserRec)r;
        rec.userId = rd.getString("userId");
        rec.userName = rd.getString("userName");
        rec.anonymous = rd.getBoolean("anonymous");
        rec.osType = rd.getString("osType");
        rec.osVersion = rd.getString("osVersion");
        rec.appVersion = rd.getString("appVersion");
        rec.inAppcode = rd.getString("inAppcode");
        rec.createdAt = rd.getLocalDateTime("createdAt");
        rec.leftAt = rd.getLocalDateTime("leftAt");
        rec.lastAt = rd.getLocalDateTime("lastAt");
        rec.likes = rd.getInt("likes");
        rec.dislikes = rd.getInt("dislikes");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new UserRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public DbRecord insert(String userId, String userName, boolean isAnonymous) {
        return super.insert(qInsert(userId, userName, isAnonymous)) ? this : DbRecord.Empty;
    }

    public DbRecord insert(String userId, String userName, boolean isAnonymous, String osType, String osVersion, String appVersion) {
        return super.insert(qInsert(userId, userName, isAnonymous, osType, osVersion, appVersion)) ? this : DbRecord.Empty;
    }

    public boolean updateUser(String userId, String osType, String osVersion, String appVersion) {
        return super.update(qUpdateUser(userId, osType, osVersion, appVersion));
    }

    public boolean delete(String userId) {
        String sql = String.format("DELETE FROM user WHERE userId='%s'", userId);
        return super.delete(sql);
    }

    public UserRec getUser(String userId) {
        String sql = String.format("SELECT * FROM user WHERE userId='%s'", userId);
        return (UserRec) super.getOne(sql);
    }

    public boolean updateAppCode(String userId, String inAppcode) {
        String sql = String.format("UPDATE user SET inAppcode='%s' WHERE userId='%s'", inAppcode, userId);
        return super.update(sql);
    }

    public boolean updateLastVisit(String userId) {
        String sql = String.format("UPDATE user SET lasttime=%d WHERE userId='%s'", System.currentTimeMillis(), userId);
        return super.update(sql);
    }

    public boolean updateLeave(String userId) {
        String sql = String.format("UPDATE user SET leavetime=%d WHERE userId='%s'", System.currentTimeMillis(), userId);
        return super.update(sql);
    }

    public boolean updateUsername(String userId, String userName) {
        String sql = String.format("UPDATE user SET userName='%s' WHERE userId='%s'", userName, userId);
        return super.update(sql);
    }

    public boolean changeAnonymousToNormal(String userId) {
        String sql = String.format("UPDATE user SET anonymous=TRUE WHERE userId='%s'", userId);
        return super.update(sql);
    }

    public boolean isSameAppCode(String inAppcode) {
        return this.inAppcode!=null && inAppcode.equals(inAppcode);
    }

    public boolean updateUserLike(String userId, boolean likes, boolean cancel) {
        return super.update(qUpdateUserLike(userId, likes, cancel));
    }

    static public String qInsert(String userId, String userName, boolean isAnonymous) {
        return String.format("INSERT INTO user (userId, userName, anonymous) VALUES('%s', '%s', %b)", userId, userName, isAnonymous);
    }

    static public String qInsert(String userId, String userName, boolean isAnonymous, String osType, String osVersion, String appVersion) {
        return String.format("INSERT INTO user (userId, userName, anonymous, osType, osVersion, appVersion, jointime, leavetime, lasttime) "
                        + "VALUES('%s', '%s', %b, '%s', '%s', '%s', %d, 0, 0)",
                userId, userName, isAnonymous, osType, osVersion, appVersion, System.currentTimeMillis());
    }

    static public String qUpdateUser(String userId, String osType, String osVersion, String appVersion) {
        return String.format("UPDATE user SET osType='%s', osVersion='%s', appVersion='%s' WHERE userId='%s'", osType, osVersion, appVersion, userId);
    }

    static public String qUpdateUserLike(String userId, boolean likes, boolean cancel) {
        int value = cancel == false ? 1 : -1;
        return likes ?
                String.format("UPDATE user SET likes=likes+%d WHERE userId='%s'", value, userId) :
                String.format("UPDATE user SET dislikes=dislikes+%d WHERE userId='%s'", value, userId);
    }

}
