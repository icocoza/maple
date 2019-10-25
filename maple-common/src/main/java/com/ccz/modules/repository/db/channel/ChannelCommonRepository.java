package com.ccz.modules.repository.db.channel;

import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EChannelType;
import com.ccz.modules.domain.constant.EFriendType;
import com.ccz.modules.repository.db.friend.FriendRec;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelCommonRepository extends CommonRepository {

    public boolean addChatChannel(String scode, String channelId, String userId, String attendees, int attendeeCount, EChannelType channelType) {
        return new ChannelRec(scode).insert(channelId, userId, attendees, attendeeCount, channelType);
    }

    public boolean delChatChannel(String scode, String channelId) {
        return new ChannelRec(scode).delete(channelId);
    }

    public boolean updateChatChannelAttendee(String scode, String channelId, String attendees, int attendeeCount) {
        return new ChannelRec(scode).updateAttendee(channelId, attendees, attendeeCount);
    }

    public boolean updateChatChannelAttendee(String scode, String channelId, String attendees, int attendeeCount, EChannelType channelType) {
        return new ChannelRec(scode).updateAttendee(channelId, attendees, attendeeCount, channelType);
    }

    public boolean updateChatChannelLastTime(String scode, String channelId) {
        return new ChannelRec(scode).updateLastTime(channelId);
    }

    public boolean updateChatChannelLastMsgAndTime(String scode, String channelId, String lastMessage) {
        return new ChannelRec(scode).updateLastMsgAndTime(channelId, lastMessage);
    }

    public ChannelRec getChatChannel(String scode, String channelId) {
        return new ChannelRec(scode).getChannel(channelId);
    }

    public ChannelRec findChatChannel(String scode, String userId, String attendees) {
        return new ChannelRec(scode).findChannel(userId, attendees);
    }

    public List<ChannelRec.RecChLastMsg> getChannelLastMsg(String scode, List<String> channelIds) {
        return new ChannelRec(scode).getChannelLastMsg(channelIds);
    }

    public boolean addMyChannel(String scode, String userId, String channelId) {
        return new MyChannelRec(scode).insert(userId, channelId);
    }

    public boolean delMyChannel(String scode, String userId, String channelId) {
        return new MyChannelRec(scode).delete(userId, channelId);
    }

    public boolean updateMyChannelLastTime(String scode, String channelId) {
        return new MyChannelRec(scode).updateLastTime(channelId);
    }

    public MyChannelRec getMyChannel(String scode, String userId, String channelId) {
        return new MyChannelRec(scode).getChannel(userId, channelId);
    }

    public List<MyChannelRec> getMyChannelList(String scode, String userId, int offset, int count) {
        return new MyChannelRec(scode).getChannelList(userId, offset, count);
    }

    public int getMyChannelCount(String scode, String userId) {
        return new MyChannelRec(scode).getChannelCount(userId);
    }

    public List<MyChannelRec.MyChannelRecExt> getMyChannelInfoList(String scode, String userId, int offset, int count) {
        return new MyChannelRec(scode).getChannelInfoList(userId, offset, count);
    }
}
