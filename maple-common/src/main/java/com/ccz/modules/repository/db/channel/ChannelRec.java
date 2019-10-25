package com.ccz.modules.repository.db.channel;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EChannelType;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChannelRec extends DbRecord {

    private String channelId;
    private String userId;
    private String attendees;
    private int attendeeCount;
    private String lastMessage;
    private EChannelType channelType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ChannelRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        ChannelRec rec = (ChannelRec)r;
        rec.channelId = rd.getString("channelId");
        rec.userId = rd.getString("userId");
        rec.attendees = rd.getString("attendees");
        rec.attendeeCount = rd.getInt("attendeeCount");
        rec.lastMessage = rd.getString("lastMessage");
        rec.channelType = EChannelType.getType(rd.getString("lastMessage"));
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
        return doLoad(rd, new ChannelRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String channelId, String userId, String attendees, int attendeeCount, EChannelType channelType) {
        String sql = String.format("INSERT INTO chatChannel (channelId, userId, attendees, attendeeCount, channelType) "
                                 + "VALUES('%s', '%s', '%s', %d, '%s')", channelId, userId, attendees, attendeeCount, channelType.getValue());
        return super.insert(sql);
    }

    public boolean delete(String channelId) {
        String sql = String.format("DELETE FROM chatChannel WHERE channelId='%s'",channelId);
        return super.delete(sql);
    }

    public boolean updateAttendee(String channelId, String attendees, int attendeeCount) {
        String sql = String.format("UPDATE chatChannel SET attendees='%s', attendeeCount=%d WHERE channelId='%s'",attendees, attendeeCount, channelId);
        return super.update(sql);
    }

    public boolean updateAttendee(String channelId, String attendees, int attendeeCount, EChannelType channelType) {
        String sql = String.format("UPDATE chatChannel SET attendees='%s', attendeeCount=%d, channelType='%s' WHERE channelId='%s'", 
                                    attendees, attendeeCount, channelType.getValue(), channelId);
        return super.update(sql);
    }

    public boolean updateLastTime(String channelId) {
        String sql = String.format("UPDATE chatChannel, chatMyChannel SET chatChannel.modifiedAt=now(), chatMyChannel.modifiedAt=now() " +
                        "WHERE chatChannel.channelId = chatMyChannel.channelId AND chatChannel.channelId='%s'", channelId);
        return super.update(sql);
    }

    public boolean updateLastMsgAndTime(String channelId, String lastMessage) {
        String sql = String.format("UPDATE chatChannel, chatMyChannel SET chatChannel.modifiedAt = now(), chatMyChannel.modifiedAt = now(), " +
                        "chatChannel.lastMessage='%s' WHERE chatChannel.channelId=chatMyChannel.channelId AND chatChannel.channelId='%s'",
                        lastMessage, channelId);
        return super.update(sql);
    }

    public ChannelRec getChannel(String channelId) {
        String sql = String.format("SELECT * FROM chatChannel WHERE channelId='%s'",channelId);
        return (ChannelRec) super.getOne(sql);
    }

    public ChannelRec findChannel(String userId, String attendees) {
        String sql = String.format("SELECT * FROM chatChannel WHERE (userId='%s' AND attendees='%s') OR (userId='%s' AND attendees='%s')",userId, attendees, attendees, userId);
        return (ChannelRec) super.getOne(sql);
    }

    public List<RecChLastMsg> getChannelLastMsg(List<String> chids) {
        return new RecChLastMsg(poolName).getChannelLastMsg(chids);
    }

    public class RecChLastMsg extends DbRecord {	//to enhance performance
        public String channelId;
        public Timestamp modifiedAt;
        public String lastMessage;

        public RecChLastMsg(String poolName) {
            super(poolName);
        }

        @Override
        public boolean createTable() {	return false; 	}

        @Override
        protected DbRecord doLoad(DbReader rd, DbRecord r) {
            RecChLastMsg rec = (RecChLastMsg)r;
            rec.channelId = rd.getString("channelId");
            rec.modifiedAt = rd.getDate("modifiedAt");
            rec.lastMessage = rd.getString("lastMessage");
            return rec;
        }

        @Override
        protected DbRecord onLoadOne(DbReader rd) {
            return doLoad(rd, this);
        }

        @Override
        protected DbRecord onLoadList(DbReader rd) {
            return doLoad(rd, new RecChLastMsg(poolName));
        }

        public List<RecChLastMsg> getChannelLastMsg(List<String> channelIds) {
            String query = channelIds.stream().filter(e-> e != null && e.length() > 0).map(e->"'"+e+"'").collect(Collectors.joining(","));
            String sql = String.format("SELECT channelId, modifiedAt, lastMessage FROM chatChannel WHERE channelId IN(%s)", query);
            return super.getList(sql).stream().map(e->(RecChLastMsg)e).collect(Collectors.toList());
        }
    }
}
