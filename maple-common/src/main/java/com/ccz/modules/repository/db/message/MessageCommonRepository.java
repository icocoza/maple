package com.ccz.modules.repository.db.message;

import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EMessageType;

import java.time.LocalDateTime;
import java.util.List;

public class MessageCommonRepository extends CommonRepository {
    
    public boolean addChatMessage(String scode, String messageId, String channelId, String senderId, EMessageType messageType, String content) {
        return new MessageRec(scode).insert(messageId, channelId, senderId, messageType, content);
    }

    public boolean delChatMessage(String scode, String messageId) {
        return new MessageRec(scode).delete(messageId);
    }

    public boolean deleteChannelMsg(String scode, String channelId) {
        return new MessageRec(scode).deleteChMsg(channelId);
    }

    public MessageRec getChatMessage(String scode, String channelId, String messageId) {
        return new MessageRec(scode).getMessage(channelId, messageId);
    }

    public List<MessageRec> getChatMessageList(String scode, String channelId, List<String> messageIds) {
        return new MessageRec(scode).getMessageList(channelId, messageIds);
    }

    public List<MessageRec> getChatMessageList(String scode, String channelId, LocalDateTime joinTime, int offset, int count) {
        return new MessageRec(scode).getMessageList(channelId, joinTime, offset, count);
    }

    public List<MessageRec> getMessageListWithoutDeletion(String scode, String channelId, LocalDateTime joinTime, String deleteIdsWithComma, int offset, int count) {
        return new MessageRec(scode).getMessageListWithoutDeletion(channelId, joinTime, deleteIdsWithComma, offset, count);
    }

    public boolean incReadCount(String scode, String messageId) {
        return new MessageRec(scode).incReadCount(messageId);
    }

    public boolean addDelMessageId(String scode, String channelId, String userId, String messageId) {
        return new MessageDelRec(scode).insert(channelId, userId, messageId);
    }

    public List<MessageDelRec.RecDelId> getDelMessageIdList(String scode, String channelId, String userId, LocalDateTime joinTime) {
        return new MessageDelRec(scode).getDelMessageIdList(channelId, userId, joinTime);
    }

    public boolean addMessageReadInfo(String scode, String channelId, String userId, String messageId) {
        return new MessageReadRec(scode).insert(channelId, userId, messageId);
    }
}
