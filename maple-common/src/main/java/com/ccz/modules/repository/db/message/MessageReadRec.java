package com.ccz.modules.repository.db.message;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageReadRec extends DbRecord {

    private String channelId;
    private String userId;
    private String messageId;
    private LocalDateTime readAt;

    public MessageReadRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        MessageReadRec rec = (MessageReadRec)r;
        rec.messageId = rd.getString("messageId");
        rec.channelId = rd.getString("channelId");
        rec.userId = rd.getString("userId");
        rec.readAt = rd.getLocalDateTime("readAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new MessageReadRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String channelId, String userId, String messageId) {
        String sql = String.format("INSERT INTO chatReadMessage (channelId, userId, messageId) "
                + "VALUES('%s', '%s', '%s')", channelId, userId, messageId);
        return super.insert(sql);
    }
}
