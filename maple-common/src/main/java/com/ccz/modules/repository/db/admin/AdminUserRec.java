package com.ccz.modules.repository.db.admin;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EAdminStatus;
import com.ccz.modules.domain.constant.EFriendStatus;
import com.ccz.modules.domain.constant.EFriendType;
import com.ccz.modules.domain.constant.EUserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminUserRec extends DbRecord {

    private String uid;
    private String email;
    private String password;
    private EAdminStatus adminStatus;
    private EUserRole userRole;
    private String userName;
    private String nationality;
    private String lastPassword;
    private LocalDateTime createdAt;
    private LocalDateTime lastAt;
    private LocalDateTime leftAt;
    private LocalDateTime passwordAt;


    public AdminUserRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        AdminUserRec rec = (AdminUserRec)r;
        rec.uid = rd.getString("uid");
        rec.email = rd.getString("email");
        rec.password = rd.getString("password");
        rec.adminStatus = EAdminStatus.getType(rd.getString("adminStatus"));
        rec.userRole = EUserRole.getType(rd.getString("userRole"));
        rec.userName = rd.getString("userName");
        rec.nationality = rd.getString("nationality");
        rec.lastPassword = rd.getString("lastPassword");
        rec.createdAt = rd.getLocalDateTime("createdAt");
        rec.lastAt = rd.getLocalDateTime("lastAt");
        rec.leftAt = rd.getLocalDateTime("leftAt");
        rec.passwordAt = rd.getLocalDateTime("passwordAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new AdminUserRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String uid, String email, String password, EUserRole userRole, String userName) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.userName = userName;
        String sql = String.format("INSERT INTO adminUser (uid, email, password, userRole, userName) "
                        + "VALUES('%s', '%s', '%s', '%s', '%s')",  uid, email, password, userRole.getValue(), userName);
        return super.insert(sql);
    }

    public AdminUserRec getUserByUid(String uid) {
        String sql = String.format("SELECT * FROM adminUser WHERE uid='%s'",  uid);
        return (AdminUserRec) super.getOne(sql);
    }

    public AdminUserRec getUserByEmail(String email) {
        String sql = String.format("SELECT * FROM adminUser WHERE email='%s'",  email);
        return (AdminUserRec) super.getOne(sql);
    }

    public AdminUserRec getUserByEmailPassword(String uid, String password) {
        String sql = String.format("SELECT * FROM adminUser WHERE uid='%s' AND password='%s'",  uid, password);
        return (AdminUserRec) super.getOne(sql);
    }

    public boolean updateLastVisit(String uid) {
        String sql = String.format("UPDATE adminUser SET lastAt=now() WHERE uid='%s'",  uid);
        return super.update(sql);
    }

    public boolean updateLeave(String uid) {
        String sql = String.format("UPDATE adminUser SET password='', leftAt=now(), lastAt=now() WHERE uid='%s'",  uid);
        return super.update(sql);
    }

    public boolean updatePassword(String uid, String oldPass, String newPass) {
        String sql = String.format("UPDATE adminUser SET password='%s', lastPassword='%s', passwordAt=now(), lastAt=now() " +
                "WHERE uid='%s' AND password='%s",  newPass, oldPass, uid, oldPass);
        return super.update(sql);
    }

}
