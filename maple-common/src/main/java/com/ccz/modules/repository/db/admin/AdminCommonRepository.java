package com.ccz.modules.repository.db.admin;

import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import com.ccz.modules.domain.constant.EAdminStatus;
import com.ccz.modules.domain.constant.EFriendType;
import com.ccz.modules.domain.constant.EUserRole;
import com.ccz.modules.repository.db.friend.FriendRec;

import java.util.List;
import java.util.stream.Collectors;

public class AdminCommonRepository extends CommonRepository {

    public static final String ADMIN_POOLNAME = "adminPool!#?";

    public String getPoolName() { return ADMIN_POOLNAME;    }

    public boolean addAdminUser(String token, String email, String password, EUserRole userRole, String userName) {
        return new AdminUserRec(getPoolName()).insert(token, email, password, userRole, userName);
    }

    public AdminUserRec getAdminUserByUid(String email) {
        return new AdminUserRec(getPoolName()).getUserByUid(email);
    }

    public AdminUserRec getAdminUserByEmail(String email) {
        return new AdminUserRec(getPoolName()).getUserByEmail(email);
    }

    public AdminUserRec getAdminUserByEmailPassword(String uid, String password) {
        return new AdminUserRec(getPoolName()).getUserByEmailPassword(uid, password);
    }

    public boolean updateLastVisit(String uid) {
        return new AdminUserRec(getPoolName()).updateLastVisit(uid);
    }

    public boolean updateLeave(String uid) {
        return new AdminUserRec(getPoolName()).updateLeave(uid);
    }

    public boolean updatePassword(String uid, String oldPass, String newPass) {
        return new AdminUserRec(getPoolName()).updatePassword(uid, oldPass, newPass);
    }

    public boolean addAdminApp(String scode, String appId, String appToken, String uid, String title, String description, EAdminAppStatus appStatus) {
        return new AdminAppRec(getPoolName()).insert(scode, appId, uid, title, appToken, description, appStatus);
    }

    public AdminAppRec getAdminApp(String applicationId) {
        return new AdminAppRec(getPoolName()).getApp(applicationId);
    }

    public AdminAppRec getAdminApp(String uid, String scode) {
        return new AdminAppRec(getPoolName()).getApp(uid, scode);
    }

    public AdminAppRec getAdminAppByScode(String scode) {
        return new AdminAppRec(getPoolName()).getAppByScode(scode);
    }

    public boolean updateAdminPush(String uid, String scode, String fcmId, String fcmKey) {
        return new AdminAppRec(getPoolName()).updatePush(uid, scode, fcmId,fcmKey);
    }

    public boolean updateAdminApp(String uid, String scode, String title, String description, EAdminAppStatus appStatus, String fcmId, String fcmKey) {
        return new AdminAppRec(getPoolName()).updateApp(uid, scode, title,description, appStatus, fcmId, fcmKey);
    }

    public boolean updateAdminAppStatus(String uid, String scode, EAdminAppStatus appStatus) {
        return new AdminAppRec(getPoolName()).updateStatus(uid, scode, appStatus);
    }

    public boolean updateExternalDbInfo(String uid, String scode, String dbHost, String dbOptions, String dbUserId, String dbPassword) {
        return new AdminAppRec(getPoolName()).updateExternalDbInfo(uid, scode, dbHost, dbOptions, dbUserId, dbPassword);
    }

    public List<AdminAppRec> getAdminAppList(EAdminAppStatus appStatus, int offset, int count) {
        return new AdminAppRec(getPoolName()).getList(appStatus, offset, count);
    }

    public List<AdminAppRec> getAdminAppList(String uid, EAdminAppStatus appStatus, int offset, int count) {
        return new AdminAppRec(getPoolName()).getList(uid, appStatus, offset, count);
    }

    public int getAdminAppCount(String uid, EAdminAppStatus appStatus) {
        return new AdminAppRec(getPoolName()).getAppCount(uid, appStatus);
    }

    public boolean hasAdminSCode(String scode) {
        return new AdminAppRec(getPoolName()).hasSCode(scode);
    }

    public boolean upsertAdminToken(String uid, String token, String remoteIp) {
        if(getAdminToken(uid)==null)
            return new AdminTokenRec(getPoolName()).insert(uid, token, remoteIp);
        return new AdminTokenRec(getPoolName()).update(uid, token, remoteIp);
    }

    public boolean addAdminToken(String uid, String token, String remoteIp) {
        return new AdminTokenRec(getPoolName()).insert(uid, token, remoteIp);
    }
    private boolean updateAdminToken(String uid, String token, String remoteIp) {
        return new AdminTokenRec(getPoolName()).update(uid, token, remoteIp);
    }

    public boolean updateAdminToken(String uid, String token) {
        return new AdminTokenRec(getPoolName()).update(uid, token);
    }

    public boolean delAdminToken(String uid) {
        return new AdminTokenRec(getPoolName()).delete(uid);
    }

    public AdminTokenRec getAdminToken(String uid) {
        return new AdminTokenRec(getPoolName()).getToken(uid);
    }

    public boolean updateAdminTokenLasttime(String uid) {
        return new AdminTokenRec(getPoolName()).updateLasttime(uid);
    }

    public boolean isAvailableToken(String uid, String token) {
        return new AdminTokenRec(getPoolName()).isAvailableToken(uid, token);
    }
}
