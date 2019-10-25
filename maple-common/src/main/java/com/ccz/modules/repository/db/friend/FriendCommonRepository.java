package com.ccz.modules.repository.db.friend;

import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.domain.constant.EFriendType;

import java.util.List;

public class FriendCommonRepository extends CommonRepository {

    public boolean addFriend(String scode, String userId, String friendId, String friendName, EFriendType friendType) {	//default normal = 0
        return new FriendRec(scode).insert(userId, friendId, friendName, friendType);
    }

    public boolean deleteFriend(String scode, String userId, String friendId) {
        return new FriendRec(scode).delete(userId, friendId);
    }

    public boolean updateFriendType(String scode, String userId, String friendId, EFriendType friendType) {
        return new FriendRec(scode).updateFriendType(userId, friendId, friendType);
    }

    public List<FriendRec> getFriendList(String scode, String userId, EFriendType friendType, int offset, int count) {
        return new FriendRec(scode).getList(userId, friendType, offset, count);
    }

    private List<FriendRec> getFriendListAll(String scode, String userId, int offset, int count) {
        return new FriendRec(scode).getListAll(userId, offset, count);
    }

    public List<FriendRec> getFriendListByIds(String scode, String userId, List<String> friendIds) {
        return new FriendRec(scode).getList(userId, friendIds);
    }

    public int getFriendCount(String scode, String userId, EFriendType friendType) {
        return new FriendRec(scode).getCount(userId, friendType);
    }

    public List<FriendRec.FriendRecInfo> getFriendMeList(String scode, String userId, EFriendType friendType, int offset, int count)  {
        return new FriendRec(scode).new FriendRecInfo(scode).getFriendMeList(userId, friendType, offset, count);
    }

    public int getFriendMeCount(String scode, String userId, EFriendType friendType) {
        return new FriendRec(scode).getFriendMeCount(userId, friendType);
    }

}
