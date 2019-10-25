package com.ccz.modules.repository.db.message;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EMessageType;
import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MessageRec extends DbRecord {

    private String messageId;
    private String channelId;
    private String senderId;
    private String content;
    private int readCount;
    private EMessageType messageType;
    private LocalDateTime createdAt;

    public MessageRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        MessageRec rec = (MessageRec)r;
        rec.messageId = rd.getString("messageId");
        rec.channelId = rd.getString("channelId");
        rec.senderId = rd.getString("senderId");
        rec.content = rd.getString("content");
        rec.readCount = rd.getInt("readCount");
        rec.messageType = EMessageType.getType(rd.getString("messageType"));
        rec.createdAt = rd.getLocalDateTime("createdAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new MessageRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String messageId, String channelId, String senderId, EMessageType messageType, String content) {
        String sql = String.format("INSERT INTO chatMessage (messageId, channelId, senderId, messageType, content) "
                + "VALUES('%s', '%s', '%s', %d, '%s')", messageId, channelId, senderId, messageType.getValue(), content);
        return super.insert(sql);
    }

    public boolean delete(String messageId) {
        String sql = String.format("DELETE FROM chatMessage WHERE messageId='%s'", messageId);
        return super.delete(sql);
    }

    public boolean deleteChMsg(String channelId) {
        String sql = String.format("DELETE FROM chatMessage WHERE channelId='%s'", channelId);
        return super.delete(sql);
    }

    public MessageRec getMessage(String channelId, String messageId) {
        String sql = String.format("SELECT * FROM chatMessage WHERE channelId='%s' AND messageId='%s'", channelId, messageId);
        return (MessageRec) super.getOne(sql);
    }

    public List<MessageRec> getMessageList(String channelId, List<String> messageIds) {
        String strMessageIds = messageIds.stream().map(e -> "'" + e + "'").collect(Collectors.joining(","));
        String sql = String.format("SELECT * FROM chatMessage WHERE channelId='%s' AND messageId IN(%s)", channelId, strMessageIds);
        return super.getList(sql).stream().map(e->(MessageRec)e).collect(Collectors.toList());
    }


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public List<MessageRec> getMessageList(String channelId, LocalDateTime joinTime, int offset, int count) {
        String strToDate = "STR_TO_DATE('"+formatter.format(joinTime)+"', '%Y-%m-%d %H:%i:%s')";
        String sql = String.format("SELECT * FROM chatMessage WHERE channelId='%s' AND createdAt > %s "
                + "ORDER BY createdAt DESC LIMIT %d, %d", channelId, strToDate, offset, count);
        return super.getList(sql).stream().map(e->(MessageRec)e).collect(Collectors.toList());
    }

    public List<MessageRec> getMessageListWithoutDeletion(String channelId, LocalDateTime joinTime, String deleteIdsWithComma, int offset, int count) {
        String strToDate = "STR_TO_DATE('"+formatter.format(joinTime)+"', '%Y-%m-%d %H:%i:%s')";
        String sql = String.format("SELECT * FROM chatMessage WHERE channelId='%s' AND createdAt > %s AND messageId NOT IN(%s) "
                + "ORDER BY createdAt DESC LIMIT %d, %d", channelId, strToDate, deleteIdsWithComma, offset, count);
        return super.getList(sql).stream().map(e->(MessageRec)e).collect(Collectors.toList());
    }

    public boolean incReadCount(String messageId) {
        String sql = String.format("UPDATE chatMessage SET readCount = readCount+1 WHERE messageId='%s'", messageId);
        return super.update(sql);
    }
}
