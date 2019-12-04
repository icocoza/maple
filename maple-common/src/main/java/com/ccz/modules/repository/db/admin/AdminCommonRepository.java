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

    public boolean addAdminUser(String email, String password, EUserRole userRole, String userName) {
        return new AdminUserRec(getPoolName()).insert(email, password, userRole, userName);
    }

    public AdminUserRec getAdminUser(String email) {
        return new AdminUserRec(getPoolName()).getUser(email);
    }

    public AdminUserRec getAdminUser(String email, String password) {
        return new AdminUserRec(getPoolName()).getUser(email, password);
    }

    public boolean updateLastVisit(String email) {
        return new AdminUserRec(getPoolName()).updateLastVisit(email);
    }

    public boolean updateLeave(String email) {
        return new AdminUserRec(getPoolName()).updateLeave(email);
    }

    public boolean updatePassword(String email, String oldPass, String newPass) {
        return new AdminUserRec(getPoolName()).updatePassword(email, oldPass, newPass);
    }

    public boolean addAdminApp(String scode, String appId, String appToken, String email, String title,
                          String description, EAdminAppStatus appStatus, String fcmId, String fcmKey) {
        return new AdminAppRec(getPoolName()).insert(scode, appId, email, title, appToken,
                description, appStatus, fcmId, fcmKey);
    }

    public AdminAppRec getAdminApp(String applicationId) {
        return new AdminAppRec(getPoolName()).getApp(applicationId);
    }

    public AdminAppRec getAdminApp(String email, String scode) {
        return new AdminAppRec(getPoolName()).getApp(email, scode);
    }

    public AdminAppRec getAdminAppByScode(String scode) {
        return new AdminAppRec(getPoolName()).getApp(scode);
    }

    public boolean updateAdminApp(String email, String scode, String title, String description, EAdminAppStatus appStatus, String fcmId, String fcmKey) {
        return new AdminAppRec(getPoolName()).updateApp(email, scode, title,description, appStatus, fcmId, fcmKey);
    }

    public boolean updateAdminAppStatus(String email, String scode, EAdminAppStatus appStatus) {
        return new AdminAppRec(getPoolName()).updateStatus(email, scode, appStatus);
    }

    public boolean updateExternalDbInfo(String email, String scode, String dbHost, String dbOptions, String dbUserId, String dbPassword) {
        return new AdminAppRec(getPoolName()).updateExternalDbInfo(email, scode, dbHost, dbOptions, dbUserId, dbPassword);
    }

    public List<AdminAppRec> getAdminAppList(EAdminAppStatus appStatus, int offset, int count) {
        return new AdminAppRec(getPoolName()).getList(appStatus, offset, count);
    }

    public List<AdminAppRec> getAdminAppList(String email, EAdminAppStatus appStatus, int offset, int count) {
        return new AdminAppRec(getPoolName()).getList(email, appStatus, offset, count);
    }

    public int getAdminAppCount(String email, EAdminAppStatus appStatus) {
        return new AdminAppRec(getPoolName()).getAppCount(email, appStatus);
    }

    public boolean hasAdminSCode(String scode) {
        return new AdminAppRec(getPoolName()).hasSCode(scode);
    }

    public boolean upsertAdminToken(String email, String token, String remoteIp) {
        if(getAdminToken(email)==null)
            return new AdminTokenRec(getPoolName()).insert(email, token, remoteIp);
        return new AdminTokenRec(getPoolName()).update(email, token, remoteIp);
    }

    private boolean addAdminToken(String email, String token, String remoteIp) {
        return new AdminTokenRec(getPoolName()).insert(email, token, remoteIp);
    }
    private boolean updateAdminToken(String email, String token, String remoteIp) {
        return new AdminTokenRec(getPoolName()).update(email, token, remoteIp);
    }

    public boolean updateAdminToken(String email, String token) {
        return new AdminTokenRec(getPoolName()).update(email, token);
    }

    public boolean delAdminToken(String email) {
        return new AdminTokenRec(getPoolName()).delete(email);
    }

    public AdminTokenRec getAdminToken(String email) {
        return new AdminTokenRec(getPoolName()).getToken(email);
    }

    public boolean updateAdminTokenLasttime(String email) {
        return new AdminTokenRec(getPoolName()).updateLasttime(email);
    }

    public boolean isAvailableToken(String email, String token, String remoteIp) {
        return new AdminTokenRec(getPoolName()).isAvailableToken(email, token, remoteIp);
    }
}
