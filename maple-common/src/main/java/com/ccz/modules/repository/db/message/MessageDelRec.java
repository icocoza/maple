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
public class MessageDelRec extends DbRecord {

    private String channelId;
    private String userId;
    private String messageId;
    private LocalDateTime deletedAt;

    public MessageDelRec(String poolName) {
        super(poolName);
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    protected DbRecord doLoad(DbReader rd, DbRecord r) {
        MessageDelRec rec = (MessageDelRec)r;
        rec.messageId = rd.getString("messageId");
        rec.channelId = rd.getString("channelId");
        rec.userId = rd.getString("userId");
        rec.deletedAt = rd.getLocalDateTime("deletedAt");
        return rec;
    }

    @Override
    protected DbRecord onLoadOne(DbReader rd) {
        return doLoad(rd, this);
    }

    @Override
    protected DbRecord onLoadList(DbReader rd) {
        return doLoad(rd, new MessageDelRec(super.poolName));
    }

    ////////////////// Queries /////////////////////
    public boolean insert(String channelId, String userId, String messageId) {
        String sql = String.format("INSERT INTO chatDelMessage (channelId, userId, messageId) VALUES('%s', '%s', '%s')",
                channelId, userId, messageId);
        return super.insert(sql);
    }

    public List<RecDelId> getDelMessageIdList(String channelId, String userId, LocalDateTime joinTime) {
        return new RecDelId(poolName).getList(channelId, userId, joinTime);
    }

    public class RecDelId extends DbRecord {

        public String messageId;

        public RecDelId(String poolName) {
            super(poolName);
        }

        @Override
        public boolean createTable() { return false;	}

        @Override
        protected DbRecord doLoad(DbReader rd, DbRecord r) {
            RecDelId rec = (RecDelId)r;
            rec.messageId = rd.getString("messageId");
            return rec;
        }

        @Override
        protected DbRecord onLoadOne(DbReader rd) {
            return doLoad(rd, this);
        }

        @Override
        protected DbRecord onLoadList(DbReader rd) {
            return doLoad(rd, new RecDelId(poolName));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        public List<RecDelId> getList(String channelId, String userId, LocalDateTime joinTime) {
            String strToDate = "STR_TO_DATE('"+formatter.format(joinTime)+"', '%Y-%m-%d %H:%i:%s')";
            String sql = String.format("SELECT messageId FROM chatDelMessage WHERE channelId='%s' AND userId='%s' AND createdAt > %s ",
                    channelId, userId, strToDate);
            return super.getList(sql).stream().map(e->(RecDelId)e).collect(Collectors.toList());
        }

    }
}
