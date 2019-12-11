package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.domain.constant.EUserAuthType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserAuthRec extends DbRecord {

    private String uid;
    private String userId;
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
        rec.uid = rd.getString("uid");
        rec.userId = rd.getString("userId");
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

    public DbRecord insertUserIdPw(String uid, String userId, String pw) {
        return super.insert(qInsertUserIdPw(uid, userId, pw)) ? this : DbRecord.Empty;
    }

    static public String qInsertUserIdPw(String uid, String userId, String pw) {
        pw = StrUtils.getSha256(pw);
        return String.format("INSERT INTO userAuth (uid, userId, pw, authType) VALUES('%s', '%s', '%s', '%s')",
                uid, userId, pw, EUserAuthType.idpw);
    }

    public DbRecord insertEmail(String uid, String email) {
        return super.insert(qInsertEmail(uid, email)) ? this : DbRecord.Empty;
    }

    static public String qInsertEmail(String uid, String email) {
        return String.format("INSERT INTO userAuth (uid, email, authType) VALUES('%s', '%s', '%s')",
                uid, email, EUserAuthType.email);
    }

    public DbRecord insertPhoneNo(String uid, String phone) {
        return super.insert(qInsertPhoneNo(uid, phone)) ? this : DbRecord.Empty;
    }

    static public String qInsertPhoneNo(String uid, String phone) {
        return String.format("INSERT INTO userAuth (uid, phone, authType) VALUES('%s', '%s', '%s')",
                uid, phone, EUserAuthType.phone);
    }

    public UserAuthRec getUser(String uid) {
        String sql = String.format("SELECT * FROM userAuth WHERE uid='%s'", uid);
        return (UserAuthRec) super.getOne(sql);
    }

    public UserAuthRec getUserByUserId(String userId) {
        String sql = String.format("SELECT * FROM userAuth WHERE userId='%s'", userId);
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

    public EUserAuthType findUserAuth(String userId, String email, String phone) {
        String sql = String.format("SELECT * FROM userAuth WHERE userId='%s' OR email='%s' OR phone='%s'", userId, email, phone);
        UserAuthRec auth = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == auth)
            return EUserAuthType.none;
        return auth.authType;
    }

    public boolean findUserId(String userId) {
        String sql = String.format("SELECT * FROM userAuth WHERE userId='%s'", userId);
        UserAuthRec user = (UserAuthRec) super.getOne(sql);
        if(DbRecord.Empty == user)
            return false;
        return true;
    }

    public String findUidByUserId(String userId) {
        String sql = String.format("SELECT * FROM userAuth WHERE userId='%s'", userId);
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

    public boolean updatePw(String uid, String pw) {
        return super.update(qUpdatePw(uid, pw));
    }

    static public String qUpdatePw(String uid, String pw) {
        pw = StrUtils.getSha256(pw);
        return String.format("UPDATE userAuth SET pw='%s' WHERE uid='%s'", pw, uid);
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

    public boolean updateUserQuit(String uid) {
        String sql = String.format("UPDATE userAuth SET authType='%s' WHERE uid='%s'", EUserAuthType.quit, uid);
        return super.update(sql);
    }

    public boolean deleteUser(String uid) {
        String sql = String.format("DELETE FROM userAuth WHERE uid='%s'", uid);
        return super.delete(sql);
    }

    public boolean isSameAuthId(EUserAuthType authType, String uid ) {
        if(EUserAuthType.idpw == authType && this.uid != null && this.userId.equals(uid))
            return true;
        if(EUserAuthType.email == authType && this.email != null && this.email.equals(uid))
            return true;
        if(EUserAuthType.phone == authType && this.phone != null && this.phone.equals(uid))
            return true;
        return false;
    }

    public boolean isSameUserName(String userId) {
        return this.userId!=null && this.userId.equals(userId);
    }

    public boolean isSamePw(String pw) {
        pw = StrUtils.getSha256(pw);
        return this.pw != null && this.pw.equals(pw);
    }

    public boolean isSameSmsCode(String smsCode) {
        return this.smsCode!=null && this.smsCode.equals(smsCode);
    }
}
