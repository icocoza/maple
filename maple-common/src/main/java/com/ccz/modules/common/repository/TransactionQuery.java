package com.ccz.modules.common.repository;

import com.ccz.modules.repository.db.user.UserAuthRec;
import com.ccz.modules.repository.db.user.UserRec;
import com.ccz.modules.repository.db.user.UserTokenRec;

public class TransactionQuery {

    public static String qInsertUserIdPw(String uid, String userId, String pw) {
        return UserAuthRec.qInsertUserIdPw(uid, userId, pw);
    }

    public static String queryInsertEmail(String uid, String email) {
        return UserAuthRec.qInsertEmail(uid, email);
    }

    public static String queryInsertPhoneNo(String uid, String phoneno) {
        return UserAuthRec.qInsertPhoneNo(uid, phoneno);
    }

    public static String queryUpdateEmailCode(String email, String emailcode) {
        return UserAuthRec.qUpdateEmailCode(email, emailcode);
    }

    public static String queryUpdateSMSCode(String phoneno, String smscode) {
        return UserAuthRec.qUpdateSMSCode(phoneno, smscode);
    }

    public static String queryInsertToken(String uid, String uuid, String tokenId, String token, boolean enabled) {
        return UserTokenRec.qInsertToken(uid, uuid, tokenId, token, enabled);
    }

    public static String queryInsertUser(String uid, String userId, boolean anonymous) {
        return UserRec.qInsert(uid, userId, anonymous);
    }

    public static String queryInsertUser(String uid, String userId, boolean anonymous, String osType, String osVersion, String appVersion) {
        return UserRec.qInsert(uid, userId, anonymous, osType, osVersion, appVersion);
    }

    public static String queryUpdateUser(String uid, String osType, String osVersion, String appVersion) {
        return UserRec.qUpdateUser(uid, osType, osVersion, appVersion);
    }

    public static String queryDeleteTokenByUuid(String uid, String uuid) {
        return UserTokenRec.qDeleteTokenByUuid(uid, uuid);
    }

    public static String queryUpdatePw(String uid, String pw) {
        return UserAuthRec.qUpdatePw(uid, pw);
    }

    /*public static String queryInsertAddress(String buildid, String zip, String sido, String sigu, String eub, String roadname, String delivery,
                                     String buildname, String dongname, String liname, String hjdongname,
                                     int buildno, int buildsubno, int jino, int jisubno, double lon, double lat) {
        return RecAddress.qInsertAddress(buildid, zip, sido, sigu, eub, roadname, delivery, buildname, dongname, liname, hjdongname, buildno, buildsubno, jino, jisubno, lon, lat);
    }

    public static String queryUpdateUserLike(String uid, boolean like, boolean cancel) {
        return UserRec.qUpdateUserLike(uid, like, cancel);
    }

    public static String queryInsertUserBuilding(String uid, String buildid, double lon, double lat) {
        return RecUserBuild.qInsertUserBuilding(uid, buildid, lon, lat);
    }

    public static String queryInsertVoterUser(String deliverid, String orderid, String senderid, int point, boolean like, String comments) {
        return RecUserVoter.qInsert(deliverid, orderid, senderid, point, like, comments);
    }

    public static String queryDeleteVoterUser(String deliverid, String orderid, String senderid) {
        return RecUserVoter.qDelete(deliverid, orderid, senderid);
    }

    public static String queryInsertOrderFile(String fileid, String orderid, String uid, EUserType usertype) {
        return RecDeliveryPhoto.qInsert(fileid, orderid, uid, usertype);
    }

    public static String queryUpdatePhotoUrl(String orderid, String photourl) {
        return RecDeliveryOrder.qUpdatePhotoUrl(orderid, photourl);
    }

    public static String queryAddUserTable(String uid, String tableid, String title, int tablepos) {
        return RecUserBoardTableList.qInsertUserTable(uid, tableid, title, tablepos);
    }

    public static String queryInsertScrap(String scrapid, String url, String title, String subtitle) {
        return RecScrap.qInsertScrap(scrapid, url, title, subtitle);
    }

    public static String queryInsertScrapId(String boardid, String scrapid) {
        return RecBoardScrap.qInsertScrap(boardid, scrapid);
    }
    public static String queryInsertScrapBody(String scrapid, String body) {
        return RecScrapBody.qInsertScrapBody(scrapid, body);
    }

    public static boolean insertTransactionalScrap(String scode, String scrapid, String url, String title, String subtitle, String body) {
        List<String> queries = new ArrayList<>();
        queries.add(this.queryInsertScrap(scrapid, url, title, subtitle));
        queries.add(this.queryInsertScrapBody(scrapid, body));
        return DbTransaction.getInst().transactionQuery(scode, queries);
    }*/
}
