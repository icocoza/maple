package com.ccz.modules.repository.db.channel;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyChannelRec extends DbRecord {

    @JsonIgnore
    private String userId;
    private String channelId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MyChannelRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        MyChannelRec rec = (MyChannelRec)r;
        rec.userId = rd.getString("userId");
        rec.channelId = rd.getString("channelId");
        rec.createdAt = rd.getLocalDateTime("createdAt");
        rec.modifiedAt = rd.getLocalDateTime("modifiedAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new MyChannelRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String userId, String channelId) {
        String sql = String.format("INSERT INTO chatMyChannel (userId, channelId) VALUES('%s', '%s', '%s')", userId, channelId);
        return super.insert(sql);
    }

    public boolean delete(String userId, String channelId) {
        String sql = String.format("DELETE FROM chatMyChannel WHERE userId='%s' AND channelId='%s'", userId, channelId);
        return super.delete(sql);
    }

    public boolean updateLastTime(String channelId) {
        String sql = String.format("UPDATE chatMyChannel SET modifiedAt=now() WHERE channelId='%s'", channelId);
        return super.update(sql);
    }

    public MyChannelRec getChannel(String userId, String channelId) {
        String sql = String.format("SELECT * FROM chatMyChannel WHERE userId='%s' AND channelId='%s'", userId, channelId);
        return (MyChannelRec) super.getOne(sql);
    }
    public List<MyChannelRec> getChannelList(String userId, int offset, int count) {
        String sql = String.format("SELECT * FROM chatMyChannel WHERE userId='%s' ORDER BY modifiedAt DESC LIMIT %d, %d", userId, offset, count);
        return super.getList(sql).stream().map(e->(MyChannelRec)e).collect(Collectors.toList());
    }

    public int getChannelCount(String userId) {
        return super.count(String.format("SELECT COUNT(*) FROM chatMyChannel WHERE userId='%s'", userId));
    }

    public List<MyChannelRecExt> getChannelInfoList(String userId, int offset, int count) {
        return new MyChannelRecExt(poolName).getChannelInfoList(userId, offset, count);
    }

    public class MyChannelRecExt extends DbRecord {
        private String channelId;
        private String userId;
        private String attendees;
        private int attendeeCount;
        private String lastMessage;
        private LocalDateTime modifiedAt;

        public MyChannelRecExt(String poolName) {
            super(poolName);
        }

        @Override
        public boolean createTable() {	return false; 	}

        @Override
        protected DbRecord doLoad(DbReader rd, DbRecord r) {
            MyChannelRecExt rec = (MyChannelRecExt)r;
            rec.channelId = rd.getString("channelId");
            rec.userId = rd.getString("userId");
            rec.attendees = rd.getString("attendees");
            rec.attendeeCount = rd.getInt("attendeeCount");
            rec.lastMessage = rd.getString("lastMessage");
            rec.modifiedAt = rd.getLocalDateTime("modifiedAt");
            return rec;
        }

        @Override
        protected DbRecord onLoadOne(DbReader rd) {
            return doLoad(rd, this);
        }

        @Override
        protected DbRecord onLoadList(DbReader rd) {
            return doLoad(rd, new MyChannelRecExt(poolName));
        }

        public List<MyChannelRecExt> getChannelInfoList(String userId, int offset, int count) {
            String sql = String.format("SELECT chatMyChannel.channelId, chatChannel.userId, chatChannel.attendees, "
                    + "chatMyChannel.modifiedAt, chatChannel.attendeeCount, channel.lastMessage "
                    + "FROM chatMyChannel JOIN chatChannel ON(chatMyChannel.channelId = chatChannel.channelId) "
                    + "WHERE chatMyChannel.userId='%s' ORDER BY chatMyChannel.modifiedAt DESC LIMIT %d, %d", userId, offset, count);
            return super.getList(sql).stream().map(e->(MyChannelRecExt)e).collect(Collectors.toList());
        }
    }
}
