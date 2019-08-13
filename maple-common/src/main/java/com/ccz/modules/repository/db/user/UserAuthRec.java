package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.domain.constant.EUserAuthType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserAuthRec extends DbRecord {

    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String pw;
    private String emailCode;
    private String smsCode;
    private LocalDateTime registeredAt;
    private LocalDateTime leftAt;
    private EUserAuthType authType;

    public UserAuthRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        UserAuthRec rec = (UserAuthRec)r;
        rec.userId = rd.getString("userId");
        rec.userName = rd.getString("userName");
        rec.email = rd.getString("email");
        rec.phone = rd.getString("phone");
        rec.pw = rd.getString("pw");
        rec.emailCode = rd.getString("emailCode");
        rec.smsCode = rd.getString("smsCode");
        rec.registeredAt = rd.getLocalDateTime("registeredAt");
        rec.leftAt = rd.getLocalDateTime("leftAt");
        rec.authType = EUserAuthType.getType(rd.getString("authType"));
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new UserAuthRec(super.poolName));
    }

    public DbRecord insertUserIdPw(String userId, String userName, String pw) {
        return super.insert(qInsertUserIdPw(userId, userName, pw)) ? this : DbRecord.Empty;
    }

    static public String qInsertUserIdPw(String userId, String userName, String pw) {
        pw = StrUtils.getSha256(pw);
        return String.format("INSERT INTO userAuth (userId, userName, pw, authType) VALUES('%s', '%s', '%s', '%s')",
                userId, userName, pw, EUserAuthType.idpw);
    }

    public DbRecord insertEmail(String userId, String email) {
        return super.insert(qInsertEmail(userId, email)) ? this : DbRecord.Empty;
    }

    static public String qInsertEmail(String userId, String email) {
        return String.format("INSERT INTO userAuth (userId, email, authType) VALUES('%s', '%s', '%s')",
                userId, email, EUserAuthType.email);
    }

    public DbRecord insertPhoneNo(String userId, String phone) {
        return super.insert(qInsertPhoneNo(userId, phone)) ? this : DbRecord.Empty;
    }

    static public String qInsertPhoneNo(String userId, String phone) {
        return String.format("INSERT INTO userAuth (userId, phone, authType) VALUES('%s', '%s', '%s')",
                userId, phone, EUserAuthType.phone);
    }

    public UserAuthRec getUser(String userId) {
        String sql = String.format("SELECT * FROM userAuth WHERE userId='%s'", userId);
        return (UserAuthRec) super.getOne(sql);
    }

    public UserAuthRec getUserByUserName(String userName) {
        String sql = String.format("SELECT * FROM userAuth WHERE userName='%s'", userName);
        return (UserAuthRec) super.getOne(sql);
    }

    public UserAuthRec getUserByEmail(String email) {
        String sql = String.format("SELECT * FROM userAuth WHERE email='%s'", email);
        return (UserAuthRec) super.getOne(sql);
    }

    public UserAuthRec getUserByPhone(String phone) {
        String sql = String.format("SELECT * FROM userAuth WHERE phone='%s'", phone);
        return (UserAuthRec) super.getOne(sql);
    }

    public EUserAuthType findUserAuth(String userName, String email, String phone) {
        String sql = String.format("SELECT * FROM userAuth WHERE userName='%s' OR email='%s' OR phone='%s'", userName, email, phone);
        UserAuthRec auth = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == auth)
            return EUserAuthType.none;
        return auth.authType;
    }

    public boolean findUserName(String userName) {
        String sql = String.format("SELECT * FROM userAuth WHERE userName='%s'", userName);
        UserAuthRec user = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == user)
            return false;
        return true;
    }

    public String findUserIdByUserName(String userName) {
        String sql = String.format("SELECT * FROM userAuth WHERE userName='%s'", userName);
        UserAuthRec user = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == user)
            return null;
        return user.getUserId();
    }

    public boolean findEmail(String email) {
        String sql = String.format("SELECT * FROM userAuth WHERE email='%s'", email);
        UserAuthRec user = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == user)
            return false;
        return true;
    }
    public boolean findPhoneno(String phone) {
        String sql = String.format("SELECT * FROM userAuth WHERE phone='%s'", phone);
        UserAuthRec user = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == user)
            return false;
        return true;
    }

    public boolean updatePw(String userName, String pw) {
        return super.update(qUpdatePw(userName, pw));
    }

    static public String qUpdatePw(String userName, String pw) {
        pw = StrUtils.getSha256(pw);
        return String.format("UPDATE userAuth SET pw='%s' WHERE userName='%s'", pw, userName);
    }

    public boolean updateEmailCode(String email, String emailCode) {
        return super.update(qUpdateEmailCode(email, emailCode));
    }

    static public String qUpdateEmailCode(String email, String emailCode) {
        return String.format("UPDATE userAuth SET emailCode='%s' WHERE email='%s'", emailCode, email);
    }

    public boolean updateSMSCode(String phone, String smsCode) {
        return super.update(qUpdateSMSCode(phone, smsCode));
    }

    static public String qUpdateSMSCode(String phone, String smsCode) {
        return String.format("UPDATE userAuth SET smsCode='%s' WHERE phone='%s'", smsCode, phone);
    }

    public boolean updateUserQuit(String userId) {
        String sql = String.format("UPDATE userAuth SET authType='%s' WHERE userId='%s'", EUserAuthType.quit, userId);
        return super.update(sql);
    }

    public boolean deleteUserId(String userId) {
        String sql = String.format("DELETE FROM userAuth WHERE userId='%s'", userId);
        return super.delete(sql);
    }

    public boolean isSameAuthId(EUserAuthType authType, String userId ) {
        if(EUserAuthType.idpw == authType && this.userId != null && this.userName.equals(userId))
            return true;
        if(EUserAuthType.email == authType && this.email != null && this.email.equals(userId))
            return true;
        if(EUserAuthType.phone == authType && this.phone != null && this.phone.equals(userId))
            return true;
        return false;
    }

    public boolean isSameUserName(String userName) {
        return this.userName!=null && this.userName.equals(userName);
    }

    public boolean isSamePw(String pw) {
        pw = StrUtils.getSha256(pw);
        return this.pw != null && this.pw.equals(pw);
    }

    public boolean isSameSmsCode(String smsCode) {
        return this.smsCode!=null && this.smsCode.equals(smsCode);
    }
}
