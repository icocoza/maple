package com.ccz.modules.controller.user;


import com.ccz.modules.common.action.ChAttributeKey;
import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.action.SessionManager;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.common.repository.TransactionQuery;
import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.AsciiSplitter.ASS;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.common.utils.KeyGen;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.controller.user.UserForms.*;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.constant.EUserAuthType;
import com.ccz.modules.domain.constant.EUserCmd;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.domain.session.AuthSession;
import com.ccz.modules.repository.db.user.UserAuthRec;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import com.ccz.modules.repository.db.user.UserRec;
import com.ccz.modules.repository.db.user.UserTokenRec;
import com.fasterxml.jackson.databind.JsonNode;
import com.ccz.modules.controller.user.UserForm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserCommandCommonAction extends CommandAction {

    SessionManager sessionManager;
    UserCommonRepository userCommonRepository;

    public UserCommandCommonAction() {
    }

    @Override
    public void initCommandFunctions(CommonRepository commonRepository) {
        this.userCommonRepository = (UserCommonRepository)commonRepository;

        sessionManager = SessionManager.getInst();
        super.setCommandFunction(makeCommandId(EUserCmd.findId), doFindId);
        super.setCommandFunction(makeCommandId(EUserCmd.regIdPw), doRegIdPw);
        super.setCommandFunction(makeCommandId(EUserCmd.regEmail), doRegEmail);
        super.setCommandFunction(makeCommandId(EUserCmd.regPhone), doRegPhone);
        super.setCommandFunction(makeCommandId(EUserCmd.login), doLogin);
        super.setCommandFunction(makeCommandId(EUserCmd.anonymousLogin), doAnonyLogin);
        super.setCommandFunction(makeCommandId(EUserCmd.signIn), doSignin);
        super.setCommandFunction(makeCommandId(EUserCmd.anonymousSignin), doSignin);
        super.setCommandFunction(makeCommandId(EUserCmd.changePw), doUpdatePW);
        super.setCommandFunction(makeCommandId(EUserCmd.updateEmail), doUpdateEmail);
        super.setCommandFunction(makeCommandId(EUserCmd.updatePhone), doUpdatePhoneNo);
        //super.setCommandFunction(makeCommandId(EUserCmd.verifyEmail), null);
        super.setCommandFunction(makeCommandId(EUserCmd.verifySms), doVerifyPhoneNo);
    }

    @Override
    public String makeCommandId(Enum e) {
        return "" + e.name();
    }

    ICommandFunction<AuthSession, ResponseData<EAllError>, FindIdForm> doFindId = (AuthSession authSession, ResponseData<EAllError> res, FindIdForm form) -> {
        if(form.getUserName().getBytes().length < 4)
            return res.setError(EAllError. userid_more_than_4);
        if(userCommonRepository.findUserName(form.getScode(), form.getUserName()) == true )
            return res.setError(EAllError.already_exist_username);
        return res.setError(EAllError.ok);
    };

    /*User Register Commands*/
    ICommandFunction<AuthSession, ResponseData<EAllError>, IdPwForm> doRegIdPw = (AuthSession authSession, ResponseData<EAllError> res, IdPwForm form) -> {

        if(form.getUserName().length()<4)
            return res.setError(EAllError. userid_more_than_4);
        if(StrUtils.isAlphaNumeric(form.getUserName())==false)
            return res.setError(EAllError.userid_alphabet_and_digit);
        if(form.getPw().length()<6)
            return res.setError(EAllError.pass_more_than_6);

        if( userCommonRepository.findUserName(form.getScode(), form.getUserName()) == true )
            return res.setError(EAllError.already_exist_username);

        String userId = KeyGen.makeKeyWithSeq("userId");
        String authQuery = TransactionQuery.qInsertUserIdPw(userId, form.getUserName(), form.getPw());
        return doRegisterUser(res, userId, form.getUserName(), authQuery, form, false);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, EmailForm> doRegEmail = (AuthSession authSession, ResponseData<EAllError> res, EmailForm form) -> {
        if(StrUtils.isEmail(form.getEmail()) == false)
            return res.setError(EAllError.invalid_email_format);

        if( userCommonRepository.findEmail(form.getScode(), form.getEmail()) == true )
            return res.setError(EAllError.already_exist_email);

        String userId = KeyGen.makeKeyWithSeq("userId");
        String authQuery = TransactionQuery.queryInsertEmail(userId, form.getEmail());
        return doRegisterUser(res, userId, form.getEmail(), authQuery, form, false);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, MobileForm> doRegPhone = (AuthSession authSession, ResponseData<EAllError> res, MobileForm form) -> {
        if(StrUtils.isPhone(form.getPhone()) == false)
            return res.setError(EAllError.invalid_phoneno_format);

        if( userCommonRepository.findPhoneno(form.getScode(), form.getPhone()) == true )
            return res.setError(EAllError.already_exist_phoneno);

        String userId = KeyGen.makeKeyWithSeq("userId");
        String authQuery = TransactionQuery.queryInsertEmail(userId, form.getPhone());
        return doRegisterUser(res, userId, form.getPhone(), authQuery, form, false);
    };

    private ResponseData<EAllError> doRegisterUser(ResponseData<EAllError> res, String userId, String userName, String authQuery, RegisterForm form, boolean enableToken) {
        String tokenId = StrUtils.getSha1Uuid("tokenId");
        String token = Crypto.AES256Cipher.getInst().enc(userName+ AsciiSplitter.ASS.UNIT+form.getUuid()+ AsciiSplitter.ASS.UNIT+form.getAuthType());

        List<String> queries = new ArrayList<>();
        queries.add(authQuery);
        queries.add(TransactionQuery.queryInsertUser(userId, form.getUserName(), form.isAnonymous()));
        queries.add(TransactionQuery.queryInsertToken(userId, form.getUuid(), tokenId, token, enableToken));
        if(userCommonRepository.multiQueries(form.getScode(), queries)==false)
            return res.setError(EAllError.failed_register);

        return res.setParam("tokenId", tokenId).setParam("token", token).setError(EAllError.ok);
    }

    /*User Auth Commands*/
    ICommandFunction<AuthSession, ResponseData<EAllError>, LoginForm> doLogin = (AuthSession authSession, ResponseData<EAllError> res, LoginForm form) -> {
        UserAuthRec auth = userCommonRepository.getUserAuthByUserName(form.getScode(), form.getUserName());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.not_exist_user);
        if(auth.isSamePw(form.getPw())==false)
            return res.setError(EAllError.invalid_user);

        UserTokenRec recToken = userCommonRepository.getUserTokenByUserId(form.getScode(), auth.getUserId());
        if(recToken==DbRecord.Empty)
            return res.setError(EAllError.unauthorized_userid);

        if(recToken.getUuid().equals(form.getUuid()) == true) {
            String token = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getUserName()+ASS.UNIT+form.getUuid()+ASS.UNIT+auth.getAuthType());
            userCommonRepository.updateToken(form.getScode(), auth.getUserId(), recToken.getTokenId(), token, true);
            return res.setParam("token", token).setError(EAllError.ok);
        }

        String tokenId = StrUtils.getSha1Uuid("tokenId");
        String token = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getUserName()+ASS.UNIT+tokenId+ASS.UNIT+form.getUuid()+ASS.UNIT+ EUserAuthType.idpw.getValue());
        List<String> queries = new ArrayList<>();
        queries.add(TransactionQuery.queryDeleteTokenByUuid(auth.getUserId(), recToken.getUuid()));
        queries.add(TransactionQuery.queryInsertToken(auth.getUserId(), form.getUuid(), tokenId, token, true));
        if(userCommonRepository.multiQueries(form.getScode(), queries)==false)
            return res.setError(EAllError.failed_update_token);

        userCommonRepository.addEpid(form.getScode(), auth.getUserId(), form.getUuid(), form.getEpid());	//register epid
        userCommonRepository.updateUserInfo(form.getScode(), auth.getUserId(), form.getOsType(), form.getOsVersion(), form.getAppVersion());
        return res.setParam("token", token).setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, AnonymousLoginForm> doAnonyLogin = (AuthSession authSession, ResponseData<EAllError> res, AnonymousLoginForm form) -> {
        if( userCommonRepository.findUserName(form.getScode(), form.getUserName()) == true )
            return res.setError(EAllError.already_exist_username);

        String userId = KeyGen.makeKeyWithSeq("anonyus");
        String authQuery = TransactionQuery.qInsertUserIdPw(userId, form.getUserName(), form.getUserName()); //anonymous user는 uid(client defined)와 inappcode를 pw로 사용함(즉, passcode 없음)
        RegisterForm registerForm = new UserForm().new RegisterForm(form.getUuid(), form.getUserName(), form);

        if(doRegisterUser(res, userId, form.getUserName(), authQuery, registerForm, true).getError() != EAllError.ok)
            return res;

        UserAuthRec auth = userCommonRepository.getUserAuth(form.getScode(), userId);
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.not_exist_user);
        UserTokenRec recToken = userCommonRepository.getUserTokenByUserId(form.getScode(), auth.getUserId());
        if(recToken==DbRecord.Empty)
            return res.setError(EAllError.unauthorized_userid);

        userCommonRepository.addEpid(form.getScode(), auth.getUserId(), form.getUuid(), form.getEpid());	//register epid
        userCommonRepository.updateUserInfo(form.getScode(), auth.getUserId(), form.getOsType(), form.getOsVersion(), form.getAppVersion());

        res.setUserid(userId);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, SigninForm> doSignin = (AuthSession authSession, ResponseData<EAllError> res, SigninForm form) -> {

        if(form.decode() == false)
            return res.setError(EAllError.unknown_error);
        if(form.isValidUserToken()==false )
            return res.setError(EAllError.invalid_user_token);

        UserTokenRec token = userCommonRepository.getUserTokenByTokenId(form.getScode(), form.getTokenId());
        if(token==DbRecord.Empty)
            return res.setError(EAllError.invalid_user_tokenid);
        if(form.getUuidByToken().equals(token.getUuid())==false)
            return res.setError(EAllError.invalid_or_expired_token);
        if(token.isEnabled() == false)
            return res.setError(EAllError.unauthorized_token);

        UserAuthRec auth = userCommonRepository.getUserAuth(form.getScode(), token.getUserId());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.unauthorized_userid);

        UserRec user = userCommonRepository.getUser(form.getScode(), token.getUserId());
        if(DbRecord.Empty == user)
            return res.setError(EAllError.not_exist_userinfo);

        if(authSession != null) {   //websocket일 경우 사용
            authSession.putSession(form.getScode(), user);    //consider the sessionid to find instance when close
            authSession.getCh().attr(ChAttributeKey.getAuthSessionKey()).set(authSession);
            sessionManager.put(authSession);
        }

        res.setParam("userName", user.getUserName());
        //res.setParam("userId", auth.getUserId()); // UserId는 외부에 노출하지 않도록 함
        res.setParam(""+user.getLastAt());
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> doUpdatePW = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
        FormUpdateIdUser form = new UserForms().new FormUpdateIdUser(jNode);
        if(form.getUuid() == null || form.getUuid().length() < 1)
            return res.setError(EAllError.invalid_uuid);
        if(form.getNewpw().length()<6)
            return res.setError(EAllError.pass_more_than_6);

        UserAuthRec auth = userCommonRepository.getUserAuthByUserName(form.getScode(), form.getUserName());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.not_exist_user);
        if(auth.isSamePw(form.getPw())==false)
            return res.setError(EAllError.mismatch_pw);

        String tokenId = StrUtils.getSha1Uuid("tid");
        String token = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getUserId()+ AsciiSplitter.ASS.UNIT+form.getUuid()+ AsciiSplitter.ASS.UNIT+form.getAuthtype());
        List<String> queries = new ArrayList<>();
        queries.add(TransactionQuery.queryDeleteTokenByUuid(auth.getUserId(), form.getUuid()));
        queries.add(TransactionQuery.queryInsertToken(auth.getUserId(), form.getUuid(), tokenId, token, false));
        queries.add(TransactionQuery.queryUpdatePw(form.getUserId(), form.getNewpw()));
        if(userCommonRepository.multiQueries(form.getScode(), queries)==false)
            return res.setError(EAllError.failed_change_pw);
        return res.setParam("tid", tokenId).setParam("token", token).setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> doUpdateEmail = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
        FormUpdateEmailUser form = new UserForms().new FormUpdateEmailUser(jNode);
        if(form.getUuid() == null || form.getUuid().length() < 1)
            return res.setError(EAllError.invalid_uuid);
        if(StrUtils.isEmail(form.getEmail()) == false)
            return res.setError(EAllError.invalid_email_format);

        UserAuthRec auth = userCommonRepository.getUserAuthByEmail(form.getScode(), form.getEmail());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.not_exist_user);
        String tokenId = StrUtils.getSha1Uuid("tid");
        String token = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getEmail()+ AsciiSplitter.ASS.UNIT+form.getUuid()+ASS.UNIT+form.getAuthtype());
        String emailcode = StrUtils.getSha1Uuid("ec");

        List<String> queries = new ArrayList<>();
        queries.add(TransactionQuery.queryUpdateEmailCode(form.getEmail(), emailcode));
        queries.add(TransactionQuery.queryInsertToken(auth.getUserId(), form.getUuid(), tokenId, token, false));
        if(userCommonRepository.multiQueries(form.getScode(), queries)==false)
            return res.setError(EAllError.failed_email_verify);

        //[TODO] Send Email

        return res.setParam("tid", tokenId).setParam("token", token).setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> doUpdatePhoneNo = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
        FormUpdatePhoneUser form = new UserForms().new FormUpdatePhoneUser(jNode);
        if(form.getUuid() == null || form.getUuid().length() < 1)
            return res.setError(EAllError.invalid_uuid);
        if(StrUtils.isPhone(form.getPhone()) == false)
            return res.setError(EAllError.invalid_phoneno_format);

        UserAuthRec auth = userCommonRepository.getUserAuthByPhone(form.getScode(), form.getPhone());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.not_exist_user);
        String tokenId = StrUtils.getSha1Uuid("tid");
        String token = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getPhone()+ASS.UNIT+form.getUuid()+ASS.UNIT+form.getAuthtype());
        String smscode = "" + (new Random().nextInt(9999-1000)+1000);

        List<String> queries = new ArrayList<>();
        queries.add(TransactionQuery.queryUpdateSMSCode(form.getPhone(), smscode));
        queries.add(TransactionQuery.queryInsertToken(auth.getUserId(), form.getUuid(), tokenId, token, false));
        if(userCommonRepository.multiQueries(form.getScode(), queries)==false)
            return res.setError(EAllError.failed_phone_Verify);

        //[TODO] Send SMS

        return res.setParam("tid", tokenId).setParam("token", token).setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> doVerifyPhoneNo = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
        FormVerifyPhoneUser form = new UserForms().new FormVerifyPhoneUser(jNode);
        if(form.getUuid() == null || form.getUuid().length() < 1)
            return res.setError(EAllError.invalid_uuid);
        if(StrUtils.isPhone(form.getPhone()) == false)
            return res.setError(EAllError.invalid_phoneno_format);
        if(form.getSmsCode()==null || form.getSmsCode().length() != 4)
            return res.setError(EAllError.smscode_size_4);

        UserAuthRec auth = userCommonRepository.getUserAuthByPhone(form.getScode(), form.getPhone());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.not_exist_user);
        if(auth.isSameSmsCode(form.getSmsCode())==false)
            return res.setError(EAllError.mismatch_smscode);

        if(userCommonRepository.enableToken(form.getScode(), auth.getUserId(), form.getTokenId(), true) == false)
            return res.setError(EAllError.unknown_error);

        return res.setError(EAllError.ok);
    };

    private ResponseData<EAllError> doLoginByToken(ResponseData<EAllError> res, FormLogin form) {
        if(form.isValidUuid()==false)
            return res.setError(EAllError.invalid_user_token);

        UserTokenRec token = userCommonRepository.getUserTokenByTokenId(form.getScode(), form.getTokenId());
        if(token==DbRecord.Empty)
            return res.setError(EAllError.invalid_user_tokenid);
        if(form.getTokenUuid().equals(token.getUuid())==false)
            return res.setError(EAllError.invalid_or_expired_token);

        UserAuthRec auth = userCommonRepository.getUserAuth(form.getScode(), token.getUserId());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.unauthorized_userid);
        if(auth.isSameUserName(form.getUserName())==false || auth.isSamePw(form.getPw())==false)
            return res.setError(EAllError.invalid_user);

        if(userCommonRepository.enableToken(form.getScode(), token.getUserId(), token.getTokenId(), true) == false)
            return res.setError(EAllError.unknown_error);
        userCommonRepository.updateUserInfo(form.getScode(), auth.getUserId(), form.getOsType(), form.getOsVersion(), form.getAppVersion());
        return res.setError(EAllError.ok);
    }


}
