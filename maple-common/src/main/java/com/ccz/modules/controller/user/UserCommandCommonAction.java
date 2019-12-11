package com.ccz.modules.controller.user;


import com.ccz.modules.common.action.*;
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
        super.setCommandFunction(makeCommandId(EUserCmd.registerIdPw), registerIdPw);
        super.setCommandFunction(makeCommandId(EUserCmd.registerEmail), registerEmail);
        super.setCommandFunction(makeCommandId(EUserCmd.registerPhone), registerPhone);

        super.setCommandFunction(makeCommandId(EUserCmd.userLogin), userLogin);
        super.setCommandFunction(makeCommandId(EUserCmd.userSignIn), userSignIn);

        super.setCommandFunction(makeCommandId(EUserCmd.anonymousLogin), anonymousLogin);
        super.setCommandFunction(makeCommandId(EUserCmd.anonymousSignIn), userSignIn);

        super.setCommandFunction(makeCommandId(EUserCmd.userFindId), userFindId);
        super.setCommandFunction(makeCommandId(EUserCmd.userChangePW), userChangePW);
        super.setCommandFunction(makeCommandId(EUserCmd.userChangeEmail), userChangeEmail);
        super.setCommandFunction(makeCommandId(EUserCmd.userChangePhone), userChangePhone);

        super.setCommandFunction(makeCommandId(EUserCmd.userVerifyEmail), userVerifyEmail);
        super.setCommandFunction(makeCommandId(EUserCmd.userVerifySms), userVerifySms);
    }

    @Override
    public String makeCommandId(Enum e) {
        return "" + e.name();
    }

    /*User Register Commands*/
    ICommandFunction<AuthSession, ResponseData<EAllError>, IdPwForm> registerIdPw = (AuthSession authSession, ResponseData<EAllError> res, IdPwForm form) -> {
        if(form.getUserId().length()<6)
            return res.setError(EAllError. userIdMoreThan6Characters);
//        if(StrUtils.isAlphaNumeric(form.getUserName())==false)
//            return res.setError(EAllError.userid_alphabet_and_digit);
        if(form.getPw().length()<8)
            return res.setError(EAllError.passwordMoreThan8Characters);
        if(form.getAppToken() == null || form.getAppToken().length() < 1)
            return res.setError(EAllError.appTokenNotExist);

        AppTokenHelper app = new AppTokenHelper(form.getAppToken());  //check scode and init db pooling
        if(app.getError() != EAllError.ok)
            return res.setError(app.getError());

        if( userCommonRepository.findUserId(app.getScode(), form.getUserId()) == true )
            return res.setError(EAllError.ExistUserId);

        return doRegisterUser(res, app.getScode(), form, false);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, EmailForm> registerEmail = (AuthSession authSession, ResponseData<EAllError> res, EmailForm form) -> {
        if(StrUtils.isEmail(form.getEmail()) == false)
            return res.setError(EAllError.invalid_email_format);

        AppTokenHelper app = new AppTokenHelper(form.getAppToken());  //check scode and init db pooling
        if(app.getError() != EAllError.ok)
            return res.setError(app.getError());

        if( userCommonRepository.findEmail(app.getScode(), form.getEmail()) == true )
            return res.setError(EAllError.ExistEmail);

        return doRegisterUser(res, app.getScode(), form, false);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, MobileForm> registerPhone = (AuthSession authSession, ResponseData<EAllError> res, MobileForm form) -> {
        if(StrUtils.isPhone(form.getPhone()) == false)
            return res.setError(EAllError.invalid_phoneno_format);

        AppTokenHelper app = new AppTokenHelper(form.getAppToken());  //check scode and init db pooling
        if(app.getError() != EAllError.ok)
            return res.setError(app.getError());

        if( userCommonRepository.findPhoneno(app.getScode(), form.getPhone()) == true )
            return res.setError(EAllError.ExistPhoneNo);

        return doRegisterUser(res, app.getScode(), form, false);
    };

    private ResponseData<EAllError> doRegisterUser(ResponseData<EAllError> res, String scode, RegisterForm form, boolean enableToken) {
        String uid = KeyGen.makeKeyWithSeq("uid:"); //user id
        String authQuery = "";
        String userId = form.getUserId();
        switch(form.getAuthType()) {
            case idpw:
                authQuery = TransactionQuery.qInsertUserIdPw(uid, userId, ((IdPwForm)form).getPw());
                break;
            case email:
                userId = ((EmailForm)form).getEmail();
                authQuery = TransactionQuery.queryInsertEmail(uid, userId);
                break;
            case phone:
                userId = ((MobileForm)form).getPhone();
                authQuery = TransactionQuery.queryInsertEmail(uid, userId);
                break;
        }
        //String userTokenId = StrUtils.getSha1Uuid("utid:"); //user token id
        //String userToken = Crypto.AES256Cipher.getInst().enc(scode+AsciiSplitter.ASS.UNIT+userName+ AsciiSplitter.ASS.UNIT+form.getUuid()+ AsciiSplitter.ASS.UNIT+form.getAuthType());
        Token token = new Token().enc(scode, uid, form.getUuid(), form.getAuthType());

        List<String> queries = new ArrayList<>();
        queries.add(authQuery);
        queries.add(TransactionQuery.queryInsertUser(uid, form.getUserId(), form.isAnonymous()));
        queries.add(TransactionQuery.queryInsertToken(uid, form.getUuid(), token.getTokenId(), token.getToken(), enableToken));
        if(userCommonRepository.multiQueries(scode, queries)==false)
            return res.setError(EAllError.failed_register);
        return res.setParam("loginToken", token.getToken()).setParam("scode", SCodeManager.getBase64(scode)).setError(EAllError.ok);
    }

    /*User Auth Commands*/
    ICommandFunction<AuthSession, ResponseData<EAllError>, LoginForm> userLogin = (AuthSession authSession, ResponseData<EAllError> res, LoginForm form) -> {
        AppTokenHelper app = new AppTokenHelper(form.getAppToken());  //check scode and init db pooling
        if(app.getError() != EAllError.ok)
            return res.setError(app.getError());

        UserAuthRec auth = userCommonRepository.getUserAuthByUserName(app.getScode(), form.getUserId());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.NotExistUserAuth);

        if(auth.getAuthType() == EUserAuthType.idpw && auth.isSamePw(form.getPw())==false)  //ID/PW 검사
            return res.setError(EAllError.InvalidUser);

        UserTokenRec recToken = userCommonRepository.getUserTokenByUserId(app.getScode(), auth.getUserId());
        if(recToken==DbRecord.Empty)
            return res.setError(EAllError.UnauthorizedUserId);

        if(recToken.getUuid().equals(form.getUuid()) == true) { //device uuid가 동일하면 기존 토큰 사용
            Token token = new Token().enc(app.getScode(), auth.getUserId(), form.getUuid(), form.getAuthType());
            userCommonRepository.updateToken(app.getScode(), auth.getUserId(), recToken.getTokenId(), token.getToken(), true);
            return res.setParam("loginToken", token.getToken()).setError(EAllError.ok);
        }

        //if different uuid,, make new token
        Token token = new Token().enc(app.getScode(), auth.getUserId(), form.getUuid(), form.getAuthType());
        //String tokenId = StrUtils.getSha1Uuid("tokenId");
        //String userToken = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getUserName()+ASS.UNIT+tokenId+ASS.UNIT+form.getUuid()+ASS.UNIT+ EUserAuthType.idpw.getValue());
        List<String> queries = new ArrayList<>();
        queries.add(TransactionQuery.queryDeleteTokenByUuid(auth.getUserId(), form.getUuid()));
        queries.add(TransactionQuery.queryInsertToken(auth.getUserId(), form.getUuid(), token.getTokenId(), token.getToken(), true));
        if(userCommonRepository.multiQueries(app.getScode(), queries)==false)
            return res.setError(EAllError.failed_update_token);

        userCommonRepository.addEpid(app.getScode(), auth.getUserId(), form.getUuid(), form.getEpid());	//register epid
        userCommonRepository.updateUserInfo(app.getScode(), auth.getUserId(), form.getOsType(), form.getOsVersion(), form.getAppVersion());
        return res.setParam("loginToken", token.getToken()).setParam("scode", SCodeManager.getBase64(app.getScode())).setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, AnonymousLoginForm> anonymousLogin = (AuthSession authSession, ResponseData<EAllError> res, AnonymousLoginForm form) -> {
        AppTokenHelper app = new AppTokenHelper(form.getAppToken());  //check scode and init db pooling
        if(app.getError() != EAllError.ok)
            return res.setError(app.getError());

        if( userCommonRepository.findUserId(app.getScode(), form.getUserId()) == true )
            return res.setError(EAllError.ExistUserId);

        String userId = KeyGen.makeKeyWithSeq("anonymous:");
        IdPwForm registerForm = new UserForm().new IdPwForm(form.getUuid(), form.getUserId(), form);
        registerForm.setPw(form.getUserId());
        if(doRegisterUser(res, app.getScode(), registerForm, true).getError() != EAllError.ok)
            return res;

        UserAuthRec auth = userCommonRepository.getUserAuth(app.getScode(), userId);
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.FailToCreateAnonymousAccount);
        UserTokenRec recToken = userCommonRepository.getUserTokenByUserId(app.getScode(), auth.getUserId());
        if(recToken==DbRecord.Empty)
            return res.setError(EAllError.UnauthorizedAnonymousUserId);

        Token token = new Token().enc(app.getScode(), auth.getUserId(), form.getUuid(), form.getAuthType());
        userCommonRepository.addEpid(app.getScode(), auth.getUserId(), form.getUuid(), form.getEpid());	//register epid
        userCommonRepository.updateUserInfo(app.getScode(), auth.getUserId(), form.getOsType(), form.getOsVersion(), form.getAppVersion());
        res.setUserid(userId);
        return res.setParam("loginToken", token.getToken()).setParam("scode", SCodeManager.getBase64(app.getScode())).setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, SigninForm> userSignIn = (AuthSession authSession, ResponseData<EAllError> res, SigninForm form) -> {
        if(form.decode() == false)
            return res.setError(EAllError.InvalidLoginToken);
        if(form.isValidLoginToken()==false )    //uuid check at inside
            return res.setError(EAllError.InvalidUUID);

        UserTokenRec token = userCommonRepository.getUserToken(form.getLoginToken().getScode(), form.getLoginToken().getUserId(), form.getLoginToken().getUuid());
        if(token==DbRecord.Empty || token.getToken().equals(form.getToken())== false)
            return res.setError(EAllError.ExpiredOrDifferentLoginToken);
        if(token.isEnabled() == false)
            return res.setError(EAllError.ExpiredLoginToken);

        UserAuthRec auth = userCommonRepository.getUserAuth(form.getLoginToken().getScode(), token.getUserId());
        if(DbRecord.Empty == auth || auth.getLeftAt() != null)
            return res.setError(EAllError.MightBeLeftUser);

        UserRec user = userCommonRepository.getUser(form.getLoginToken().getScode(), token.getUserId());
        if(DbRecord.Empty == user)
            return res.setError(EAllError.MightBeLeftUser);

        if(authSession != null) {   //websocket일 경우 사용
            authSession.putSession(form.getLoginToken().getScode(), user);    //consider the sessionid to find instance when close
            authSession.getCh().attr(ChAttributeKey.getAuthSessionKey()).set(authSession);
            sessionManager.put(authSession);
        }

        res.setParam("userName", user.getUserName());
        res.setParam(""+user.getLastAt());
        res.setParam("signinToken", new AuthToken().enc(form.getLoginToken().getScode(), auth.getUserId(), form.getUuid(), auth.getAuthType()));
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, ChangePwForm> userChangePW = (AuthSession authSession, ResponseData<EAllError> res, ChangePwForm form) -> {
        if(form.decode() == false)
            return res.setError(EAllError.InvalidSigninToken);
        if(form.getAuthToken().isExpired() == true)
            return res.setError(EAllError.ExpiredSigninToken);
        if(form.getNewPw().length()<8)
            return res.setError(EAllError.passwordMoreThan8Characters);

        UserAuthRec auth = userCommonRepository.getUserAuth(form.getAuthToken().getScode(), form.getAuthToken().getUserId());
        if(DbRecord.Empty == auth)
            return res.setError(EAllError.MightBeLeftUser);
        if(auth.isSamePw(form.getNewPw())==false)
            return res.setError(EAllError.WrongPassword);

        if( userCommonRepository.updatePw(form.getAuthToken().getScode(), form.getAuthToken().getUserId(), form.getNewPw())== false)
            return res.setError(EAllError.FailToChangePW);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> userChangeEmail = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
//        FormUpdateEmailUser form = new UserForms().new FormUpdateEmailUser(jNode);
//        if(form.getUuid() == null || form.getUuid().length() < 1)
//            return res.setError(EAllError.invalid_uuid);
//        if(StrUtils.isEmail(form.getEmail()) == false)
//            return res.setError(EAllError.invalid_email_format);
//
//        UserAuthRec auth = userCommonRepository.getUserAuthByEmail(form.getScode(), form.getEmail());
//        if(DbRecord.Empty == auth)
//            return res.setError(EAllError.not_exist_user);
//        String tokenId = StrUtils.getSha1Uuid("tid");
//        String token = Crypto.AES256Cipher.getInst().enc(form.getScode(), form.getEmail()+ AsciiSplitter.ASS.UNIT+form.getUuid()+ASS.UNIT+form.getAuthtype());
//        String emailcode = StrUtils.getSha1Uuid("ec");
//
//        List<String> queries = new ArrayList<>();
//        queries.add(TransactionQuery.queryUpdateEmailCode(form.getEmail(), emailcode));
//        queries.add(TransactionQuery.queryInsertToken(auth.getUserId(), form.getUuid(), tokenId, token, false));
//        if(userCommonRepository.multiQueries(form.getScode(), queries)==false)
//            return res.setError(EAllError.failed_email_verify);
//
//        //[TODO] Send Email
//
//        return res.setParam("tid", tokenId).setParam("token", token).setError(EAllError.ok);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> userChangePhone = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
        //return res.setParam("tid", tokenId).setParam("token", token).setError(EAllError.ok);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, FindIdForm> userFindId = (AuthSession authSession, ResponseData<EAllError> res, FindIdForm form) -> {
        if(form.getUserId().getBytes().length < 6)
            return res.setError(EAllError.userIdMoreThan6Characters);
        AppTokenHelper app = new AppTokenHelper(form.getAppToken());  //check scode and init db pooling
        if(app.getError() != EAllError.ok)
            return res.setError(app.getError());

        if(userCommonRepository.findUserId(app.getScode(), form.getUserId()) == false )
            return res.setError(EAllError.NotExistUserId);
        return res.setError(EAllError.ok);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> userVerifyEmail = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
        return res.setError(EAllError.unknown_error);
    };

    ICommandFunction<AuthSession, ResponseData<EAllError>, JsonNode> userVerifySms = (AuthSession authSession, ResponseData<EAllError> res, JsonNode jNode) -> {
//        FormVerifyPhoneUser form = new UserForms().new FormVerifyPhoneUser(jNode);
//        if(form.getUuid() == null || form.getUuid().length() < 1)
//            return res.setError(EAllError.invalid_uuid);
//        if(StrUtils.isPhone(form.getPhone()) == false)
//            return res.setError(EAllError.invalid_phoneno_format);
//        if(form.getSmsCode()==null || form.getSmsCode().length() != 4)
//            return res.setError(EAllError.smscode_size_4);
//
//        UserAuthRec auth = userCommonRepository.getUserAuthByPhone(form.getScode(), form.getPhone());
//        if(DbRecord.Empty == auth)
//            return res.setError(EAllError.not_exist_user);
//        if(auth.isSameSmsCode(form.getSmsCode())==false)
//            return res.setError(EAllError.mismatch_smscode);
//
//        if(userCommonRepository.enableToken(form.getScode(), auth.getUserId(), form.getTokenId(), true) == false)
//            return res.setError(EAllError.unknown_error);

        return res.setError(EAllError.ok);
    };


}
