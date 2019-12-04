package com.ccz.modules.repository.db.friend;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EFriendStatus;
import com.ccz.modules.domain.constant.EFriendType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class FriendRec extends DbRecord {

    @JsonIgnore
    private String userId;
    @JsonIgnore
    private String friendId;
    private String friendName;
    private EFriendType friendType;
    private EFriendStatus friendStatus;
    private boolean deleted;
    private LocalDateTime addedAt;
    private LocalDateTime deletedAt;

    public FriendRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        FriendRec rec = (FriendRec)r;
        rec.userId = rd.getString("userId");
        rec.friendId = rd.getString("friendId");
        rec.friendName = rd.getString("friendName");
        rec.friendType = EFriendType.getType(rd.getString("friendType"));
        rec.friendStatus = EFriendStatus.getType(rd.getString("friendStatus"));
        rec.deleted = rd.getBoolean("deleted");
        rec.addedAt = rd.getLocalDateTime("addedAt");
        rec.deletedAt = rd.getLocalDateTime("deletedAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new FriendRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String userId, String friendId, String friendName, EFriendType friendType) {	//default normal = 0
        String sql = String.format("INSERT INTO friend (userId, friendId, friendName, friendType) "
                + "VALUES('%s', '%s', '%s', '%s')", userId, friendId, friendName, friendType);
        return super.insert(sql);
    }

    public boolean delete(String userId, String friendId) {
        String sql = String.format("DELETE FROM friend WHERE userId='%s' AND friendId='%s'", userId, friendId);
        return super.delete(sql);
    }

    public boolean updateFriendType(String userId, String friendId, EFriendType friendType) {
        String sql = String.format("UPDATE friend SET friendType='%s' WHERE userId='%s' AND friendId='%s'", friendType.getValue(), userId, friendId);
        return super.update(sql);
    }

    public List<FriendRec> getList(String userId, EFriendType friendType, int offset, int count) {
        if(EFriendType.all == friendType)
            return getListAll(userId, offset, count);
        String sql = String.format("SELECT * FROM friend WHERE userId='%s' AND friendType='%s' LIMIT %d, %d",
                userId, friendType.getValue(), offset, count);
        return super.getList(sql).stream().map(e->(FriendRec)e).collect(Collectors.toList());
    }

    public List<FriendRec> getListAll(String userId, int offset, int count) {
        String sql = String.format("SELECT * FROM friend WHERE userId='%s' LIMIT %d, %d", userId, offset, count);
        return super.getList(sql).stream().map(e->(FriendRec)e).collect(Collectors.toList());
    }

    public List<FriendRec> getList(String userId, List<String> friendIds) {
        String idStrList = friendIds.stream().map(e->"'"+e+"'").collect(Collectors.joining(","));
        String sql = String.format("SELECT * FROM friend WHERE userId='%s' AND friendId IN (%s)", userId, idStrList);
        return super.getList(sql).stream().map(e->(FriendRec)e).collect(Collectors.toList());
    }

    public int getCount(String userId, EFriendType friendType) {
        if(EFriendType.all == friendType)
            return super.count(String.format("SELECT COUNT(*) FROM friend WHERE userId='%s'", userId));
        return super.count(String.format("SELECT COUNT(*) FROM %s WHERE userId='%s' AND friendType='%s'", userId, friendType.getValue()));
    }

    public List<FriendRecInfo> getFriendMeList(String userId, EFriendType friendType, int offset, int count)  {
        return new FriendRecInfo(poolName).getFriendMeList(userId, friendType, offset, count);
    }

    public int getFriendMeCount(String userId, EFriendType friendType) {
        if(EFriendType.all == friendType)
            return super.count(String.format("SELECT COUNT(*) FROM friend WHERE friendId='%s'", userId));
        return super.count(String.format("SELECT COUNT(*) FROM %s WHERE friendId='%s' AND friendType='%s'", userId, friendType.getValue()));
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class FriendRecInfo extends DbRecord {
        @JsonIgnore
        private String userId;
        private String userName;
        private String email; //optional

        public FriendRecInfo(String poolName) {
            super(poolName);
        }

        @Override
        public boolean createTable() { 	return false; 	}

        @Override
        protected DbRecord doLoad(DbReader rd, DbRecord r) {
            FriendRecInfo rec = (FriendRecInfo)r;
            rec.userId = rd.getString("userId");
            rec.userName = rd.getString("userName");
            rec.email = rd.getString("email");
            return rec;
        }

        @Override
        protected DbRecord onLoadOne(DbReader rd) {
            return doLoad(rd, this);
        }

        @Override
        protected DbRecord onLoadList(DbReader rd) {
            return doLoad(rd, new FriendRecInfo(poolName));
        }

        public List<FriendRecInfo> getFriendMeList(String userId, EFriendType friendType, int offset, int count) {
            String sql = String.format("SELECT userId, userName, userType, email FROM userAuth WHERE userId IN (SELECT userId FROM %s WHERE friendId='%s' LIMIT %d, %d)",
                        userId, offset, count);
            if(EFriendType.all != friendType)
                sql = String.format("SELECT userId, userName, userType, email FROM userAuth WHERE userId IN (SELECT userId FROM %s WHERE friendId='%s' AND friendType='%s') LIMIT %d, %d",
                        userId, friendStatus.getValue(), offset, count);
            return super.getList(sql).stream().map(e->(FriendRecInfo)e).collect(Collectors.toList());
        }

    }

}
