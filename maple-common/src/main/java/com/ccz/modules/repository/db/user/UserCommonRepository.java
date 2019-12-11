package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EUserAuthType;

public class UserCommonRepository extends CommonRepository {
    
    public UserRec addUser(String scode, String userId, String userName, boolean anonymous, String osType, String osVersion, String appVersion) {
        return (UserRec) new UserRec(scode).insert(userId, userName, anonymous, osType, osVersion, appVersion);
    }

    public UserRec getUser(String scode, String userId) {
        return (UserRec) new UserRec(scode).getUser(userId);
    }

    public boolean updateAppCode(String scode, String userId, String inAppcode) {
        return new UserRec(scode).updateAppCode(userId, inAppcode);
    }

    public boolean updateLastTime(String scode, String userId) {
        return new UserRec(scode).updateLastVisit(userId);
    }

    public boolean updateUserInfo(String scode, String userId, String osType, String osVersion, String appVersion) {
        return new UserRec(scode).updateUser(userId, osType, osVersion, appVersion);
    }

    //for user token
    public UserTokenRec getUserTokenByUserId(String scode, String userId) {
        return (UserTokenRec) new UserTokenRec(scode).getTokenByUserId(userId);
    }

    public UserTokenRec getUserToken(String scode, String userId, String uuid) {
        return (UserTokenRec) new UserTokenRec(scode).getToken(userId, uuid);
    }

    public UserTokenRec getUserTokenByUserTokenId(String scode, String userId, String tokenid) {
        return (UserTokenRec) new UserTokenRec(scode).getTokenByUserTokenId(userId, tokenid);
    }

    public boolean addUserToken(String scode, String userId, String uuid, String tokenid, String token) {
        return new UserTokenRec(scode).insertToken(userId, uuid, tokenid, token);
    }

    public boolean delUserToken(String scode, String userId) {
        return new UserTokenRec(scode).delete(userId);
    }

    public boolean enableToken(String scode, String userId, String tokenid, boolean enabled) {
        return new UserTokenRec(scode).enableToken(userId, tokenid, enabled);
    }

    public boolean updateToken(String scode, String userId, String tokenid, String token, boolean enabled) {
        return new UserTokenRec(scode).updateToken(userId, tokenid, token, enabled);
    }

    //for user authentication
    public UserAuthRec insertUID(String scode, String userId, String userName, String pw) {
        return (UserAuthRec) new UserAuthRec(scode).insertUserIdPw(userId, userName, pw);
    }

    public UserAuthRec insertEmail(String scode, String userId, String email, String pw) {
        return (UserAuthRec) new UserAuthRec(scode).insertEmail(userId, email);
    }

    public UserAuthRec insertPhoneNo(String scode, String userId, String phoneno, String pw) {
        return (UserAuthRec) new UserAuthRec(scode).insertPhoneNo(userId, phoneno);
    }

    public UserAuthRec getUserAuth(String scode, String userId) {
        return (UserAuthRec) new UserAuthRec(scode).getUser(userId);
    }

    public UserAuthRec getUserAuthByUserName(String scode, String userName) {
        return (UserAuthRec) new UserAuthRec(scode).getUserByUserName(userName);
    }

    public UserAuthRec getUserAuthByEmail(String scode, String email) {
        return (UserAuthRec) new UserAuthRec(scode).getUserByEmail(email);
    }

    public UserAuthRec getUserAuthByPhone(String scode, String phoneno) {
        return (UserAuthRec) new UserAuthRec(scode).getUserByPhone(phoneno);
    }

    public EUserAuthType findUserAuth(String scode, String userName, String email, String phoneno) {
        return new UserAuthRec(scode).findUserAuth(userName, email, phoneno);
    }

    public boolean findUserId(String scode, String userId) {
        return new UserAuthRec(scode).findUserId(userId);
    }

    public String findUserIdByUserName(String scode, String userName) {
        return new UserAuthRec(scode).findUserIdByUserName(userName);
    }

    public boolean findEmail(String scode, String email) {
        return new UserAuthRec(scode).findEmail(email);
    }

    public boolean findPhoneno(String scode, String phoneno) {
        return new UserAuthRec(scode).findPhoneno(phoneno);
    }

    public boolean updatePw(String scode, String userName, String pw) {
        return new UserAuthRec(scode).updatePw(userName, pw);
    }

    public boolean updateEmailCode(String scode, String email, String pw) {
        return new UserAuthRec(scode).updateEmailCode(email, pw);
    }

    public boolean updateSMSCode(String scode, String phoneno, String emailcode) {
        return new UserAuthRec(scode).updateSMSCode(phoneno, emailcode);
    }

    public boolean updateUserQuit(String scode, String userId) {
        return new UserAuthRec(scode).updateUserQuit(userId);
    }

    public boolean deleteUserId(String scode, String userId) {
        return new UserAuthRec(scode).deleteUserId(userId);
    }

    //for user epid for fcm push
    public DbRecord addEpid(String scode, String userId, String deviceUuid, String epid) {
        return new UserPushRec(scode).insert(deviceUuid, userId, epid);
    }
    public boolean delEpid(String scode, String deviceUuid) {        
        return new UserPushRec(scode).delete(deviceUuid);
    }
    
    public UserPushRec getEpid(String scode, String deviceUuid) {
        return new UserPushRec(scode).getEpid(deviceUuid);
    }
    
    public boolean updateEpid(String scode, String deviceUuid, String epid) {
        return new UserPushRec(scode).updateEpid(deviceUuid, epid);
    }

}
