package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EUserAuthType;

public abstract class UserCommonRepository extends CommonRepository {

    public abstract String getPoolName();

    public UserRec addUser(String userId, String userName, boolean anonymous, String osType, String osVersion, String appVersion) {
        return (UserRec) new UserRec(getPoolName()).insert(userId, userName, anonymous, osType, osVersion, appVersion);
    }

    public UserRec getUser(String userId) {
        return (UserRec) new UserRec(getPoolName()).getUser(userId);
    }

    public boolean updateAppCode(String userId, String inAppcode) {
        return new UserRec(getPoolName()).updateAppCode(userId, inAppcode);
    }

    public boolean updateLastTime(String userId) {
        return new UserRec(getPoolName()).updateLastVisit(userId);
    }

    public boolean updateUserInfo(String userId, String osType, String osVersion, String appVersion) {
        return new UserRec(getPoolName()).updateUser(userId, osType, osVersion, appVersion);
    }

    //for user token
    public UserTokenRec getUserTokenByUserId(String userId) {
        return (UserTokenRec) new UserTokenRec(getPoolName()).getTokenByUserId(userId);
    }

    public UserTokenRec getUserTokenByTokenId(String tokenid) {
        return (UserTokenRec) new UserTokenRec(getPoolName()).getTokenByTokenId(tokenid);
    }

    public UserTokenRec getUserTokenByUserTokenId(String userId, String tokenid) {
        return (UserTokenRec) new UserTokenRec(getPoolName()).getTokenByUserTokenId(userId, tokenid);
    }

    public boolean addUserToken(String userId, String uuid, String tokenid, String token) {
        return new UserTokenRec(getPoolName()).insertToken(userId, uuid, tokenid, token);
    }

    public boolean delUserToken(String userId) {
        return new UserTokenRec(getPoolName()).delete(userId);
    }

    public boolean enableToken(String userId, String tokenid, boolean enabled) {
        return new UserTokenRec(getPoolName()).enableToken(userId, tokenid, enabled);
    }

    public boolean updateToken(String userId, String tokenid, String token, boolean enabled) {
        return new UserTokenRec(getPoolName()).updateToken(userId, tokenid, token, enabled);
    }

    //for user authentication
    public UserAuthRec insertUID(String userId, String userName, String pw) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).insertUserIdPw(userId, userName, pw);
    }

    public UserAuthRec insertEmail(String userId, String email, String pw) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).insertEmail(userId, email);
    }

    public UserAuthRec insertPhoneNo(String userId, String phoneno, String pw) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).insertPhoneNo(userId, phoneno);
    }

    public UserAuthRec getUserAuth(String userId) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).getUser(userId);
    }

    public UserAuthRec getUserAuthByUserName(String userName) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).getUserByUserName(userName);
    }

    public UserAuthRec getUserAuthByEmail(String email) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).getUserByEmail(email);
    }

    public UserAuthRec getUserAuthByPhone(String phoneno) {
        return (UserAuthRec) new UserAuthRec(getPoolName()).getUserByPhone(phoneno);
    }

    public EUserAuthType findUserAuth(String userName, String email, String phoneno) {
        return new UserAuthRec(getPoolName()).findUserAuth(userName, email, phoneno);
    }

    public boolean findUserName(String userName) {
        return new UserAuthRec(getPoolName()).findUserName(userName);
    }

    public String findUserIdByUserName(String userName) {
        return new UserAuthRec(getPoolName()).findUserIdByUserName(userName);
    }

    public boolean findEmail(String email) {
        return new UserAuthRec(getPoolName()).findEmail(email);
    }

    public boolean findPhoneno(String phoneno) {
        return new UserAuthRec(getPoolName()).findPhoneno(phoneno);
    }

    public boolean updatePw(String userName, String pw) {
        return new UserAuthRec(getPoolName()).updatePw(userName, pw);
    }

    public boolean updateEmailCode(String email, String pw) {
        return new UserAuthRec(getPoolName()).updateEmailCode(email, pw);
    }

    public boolean updateSMSCode(String phoneno, String emailcode) {
        return new UserAuthRec(getPoolName()).updateSMSCode(phoneno, emailcode);
    }

    public boolean updateUserQuit(String userId) {
        return new UserAuthRec(getPoolName()).updateUserQuit(userId);
    }

    public boolean deleteUserId(String userId) {
        return new UserAuthRec(getPoolName()).deleteUserId(userId);
    }

    //for user epid for fcm push
    public DbRecord addEpid(String userId, String deviceUuid, String epid) {
        return new UserPushRec(getPoolName()).insert(deviceUuid, userId, epid);
    }
    public boolean delEpid(String deviceUuid) {        
        return new UserPushRec(getPoolName()).delete(deviceUuid);
    }
    
    public UserPushRec getEpid(String deviceUuid) {
        return new UserPushRec(getPoolName()).getEpid(deviceUuid);
    }
    
    public boolean updateEpid(String deviceUuid, String epid) {
        return new UserPushRec(getPoolName()).updateEpid(deviceUuid, epid);
    }

}
