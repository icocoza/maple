package com.ccz.modules.repository.db.user;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserTokenRec extends DbRecord {
    private String userId;
    private String uuid;
    private String tokenId;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private boolean enabled;

    public UserTokenRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        UserTokenRec rec = (UserTokenRec)r;
        rec.userId = rd.getString("userId");
        rec.uuid = rd.getString("uuid");
        rec.tokenId = rd.getString("tokenId");
        rec.token = rd.getString("token");
        rec.createdAt = rd.getLocalDateTime("createdAt");
        rec.expiredAt = rd.getLocalDateTime("expiredAt");
        rec.enabled = rd.getBoolean("enabled");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new UserTokenRec(super.poolName));
    }

    public UserTokenRec getTokenByUserId(String userId) {		//if want to support multiple device, return List<UserTokenRec>
        String sql = String.format("SELECT * FROM userToken WHERE userId='%s'", userId);
        return (UserTokenRec) super.getOne(sql);
    }

    public UserTokenRec getTokenByTokenId(String tokenId) {
        String sql = String.format("SELECT * FROM userToken WHERE tokenId='%s'", tokenId);
        return (UserTokenRec) super.getOne(sql);
    }

    public UserTokenRec getTokenByUserTokenId(String userId, String tokenId) {
        String sql = String.format("SELECT * FROM userToken WHERE userId='%s' AND tokenId='%s'", userId, tokenId);
        return (UserTokenRec) super.getOne(sql);
    }

    public boolean insertToken(String userId, String uuid, String tokenId, String token) {
        return super.insert(qInsertToken(userId, uuid, tokenId, token, false));
    }

    static public String qInsertToken(String userId, String uuid, String tokenId, String token, boolean enabled) {
        return String.format("INSERT INTO userToken (userId, uuid, tokenId, token, enabled) VALUES('%s', '%s', '%s', '%s', %b)", userId, uuid, tokenId, token, enabled);
    }

    public boolean updateToken(String userId, String tokenId, String token) {
        String sql = String.format("UPDATE userToken SET token='%s' WHERE userId='%s' AND tokenId='%s'", token, userId, tokenId);
        return super.update(sql);
    }

    public boolean updateToken(String userId, String tokenId, String token, boolean enabled) {
        String sql = String.format("UPDATE userToken SET token='%s', enabled=%b WHERE userId='%s' AND tokenId='%s'", token, enabled, userId, tokenId);
        return super.update(sql);
    }

    public boolean enableToken(String userId, String tokenId, boolean enabled) {
        String sql = String.format("UPDATE userToken SET enabled=%b WHERE userId='%s' AND tokenId='%s'", enabled, userId, tokenId);
        return super.update(sql);
    }

    static public String qDeleteTokenByUuid(String userId, String uuid) {
        return String.format("DELETE FROM userToken WHERE userId='%s' AND uuid='%s'", userId, uuid);
    }

    public boolean delete(String userId, String tokenId) {
        String sql = String.format("DELETE FROM userToken WHERE userId='%s' AND token='%s'", userId, tokenId);
        return super.delete(sql);
    }

    public boolean delete(String userId) {
        String sql = String.format("DELETE FROM userToken WHERE userId='%s'", userId);
        return super.delete(sql);
    }
}
