package com.ccz.modules.controller.admin;


import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.action.SessionManager;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.common.utils.AsciiSplitter.ASS;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.common.utils.KeyGen;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.controller.user.AdminToken;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import com.ccz.modules.domain.constant.EAdminCmd;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.repository.db.admin.AdminAppRec;
import com.ccz.modules.repository.db.admin.AdminCommonRepository;
import com.ccz.modules.repository.db.admin.AdminUserRec;
import com.ccz.modules.controller.admin.AdminForm.*;
import com.ccz.modules.server.service.DbAdminManager;
import com.ccz.modules.server.service.DbAppManager;
import com.ccz.modules.server.service.DbScodeManager;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.List;

public class AdminCommandCommonAction extends CommandAction {

    SessionManager sessionManager;
    AdminCommonRepository adminCommonRepository;

    public AdminCommandCommonAction() {
    }

    @Override
    public void initCommandFunctions(CommonRepository adminCommonRepository) {
        this.adminCommonRepository = (AdminCommonRepository) adminCommonRepository;

        super.setCommandFunction(makeCommandId(EAdminCmd.adminRegister), adminRegister);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminLogin), adminLogin);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminLogout), adminLogout);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminAddApp), adminAddApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminDelApp), adminDelApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminAppList), adminAppList);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminModifyApp), adminModifyApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminAppCount), adminAppCount);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminStopApp), updateApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminRunApp), updateApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminReadyApp), updateApp);
    }

    private String getRemoteIp(Channel ch) {
        try {
            return ((InetSocketAddress) ch.remoteAddress()).getAddress().getHostAddress();
        }catch(Exception e) {
            return "0.0.0.0";
        }
    }

    @Override
    public String makeCommandId(Enum e) {
        return e.name();
    }

    ICommandFunction<Channel, ResponseData<EAllError>, AdminUserForm> adminRegister = (Channel ch, ResponseData<EAllError> res, AdminUserForm form) -> {
        if(StrUtils.isEmail(form.getEmail())==false)
            return res.setError(EAllError.invalid_email_format);

        if( adminCommonRepository.getAdminUserByEmail(form.getEmail()) != AdminUserRec.Empty)
            return res.setError(EAllError.ExistEmail);

        if(form.getPassword().length() < 8)
            return res.setError(EAllError.short_password_length_than_8);
        String token = StrUtils.getSha256Uuid("aid:");
        if( adminCommonRepository.addAdminUser(token, form.getEmail(), form.getPassword(), form.getUserRole(), form.getUserName()) == false)
            return res.setError(EAllError.register_failed);
        return res.setError(EAllError.ok).setParam(form.getEmail());
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AdminLoginForm> adminLogin = (Channel ch, ResponseData<EAllError> res, AdminLoginForm form) -> {
        AdminUserRec rec = adminCommonRepository.getAdminUserByEmail(form.getEmail());
        if(rec== DbRecord.Empty)
            return res.setError(EAllError.eNotExistUser);

        if(rec.getPassword().equals(form.getPassword()) == false)
            return res.setError(EAllError.eWrongAccountInfo);

        String token = new AdminToken().enc(rec.getUid(), rec.getUserRole(), rec.getUserName());
        adminCommonRepository.addAdminToken(rec.getUid(), token, "0.0.0.0");   //가장 마지막 토큰이 유효한 토큰 임(히스토리 관리 위해 예전 토큰 보관)
        return res.setError(EAllError.ok).setParam("email", form.getEmail()).setParam("token", token);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AdminLogoutForm> adminLogout = (Channel ch, ResponseData<EAllError> res, AdminLogoutForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        adminCommonRepository.updateLeave(form.getUid());
        adminCommonRepository.updateAdminToken(form.getUid(), "");
        return res.setError(EAllError.ok).setParam("adminId", form.getUid());
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AddAppForm> adminAddApp = (Channel ch, ResponseData<EAllError> res, AddAppForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        if(StrUtils.isAlphaNumeric(form.getScode()) == false)
            return res.setError(EAllError.ScodeAllowedOnlyAlphabet);
        if( adminCommonRepository.hasAdminSCode(form.getScode()) == true )
            return res.setError(EAllError.AlreadyExistScode);

        String appId = KeyGen.makeKeyWithSeq("appId:");
        String appToken = Crypto.AES256Cipher.getInst().enc(appId+ ASS.CHUNK+form.getScode());

        if( (res = this.createAppDatabase(res, form)).getError() != EAllError.ok)
            return res;

        if(adminCommonRepository.addAdminApp(form.getScode(), appId, appToken, token.getUid(), form.getTitle(),form.getDescription(), form.getAppStatus()) == false)
            return res.setError(EAllError.failed_to_add_app);
        if(form.getFcmId() != null && form.getFcmKey() != null) {
            adminCommonRepository.updateAdminPush(token.getUid(), form.getScode(), form.getFcmId(), form.getFcmKey());
        }
        return res.setError(EAllError.ok).setParam("appId", appId).setParam("appToken", appToken);
    };

    private ResponseData<EAllError> createAppDatabase(ResponseData<EAllError> res, AddAppForm form) {
        if( form.isEmptyExternalDb() == true) {
            if( DbAppManager.getInst().createDatabaseOnInternal(form.getScode()) == false )
                return res.setError(EAllError.failed_to_create_app_database);
            DbAppManager.getInst().createConnectionPool(form.getScode());
        }else {
            if( DbAppManager.getInst().createDatabaseOnExternal(form.getScode(), form.getDbHost(), form.getDbOptions(), form.getDbUserId(), form.getDbPassword()) == false )
                return res.setError(EAllError.invalid_db_parameter);
            DbAppManager.getInst().createConnectionPool(form.getScode(), form.getDbHost(), form.getDbOptions(), form.getDbUserId(), form.getDbPassword());
            adminCommonRepository.updateExternalDbInfo(form.getUid(), form.getScode(), form.getDbHost(), form.getDbOptions(), form.getDbUserId(), form.getDbPassword());
        }

        DbAppManager.getInst().createTables(form.getScode());
        return res.setError(EAllError.ok);
    }


    ICommandFunction<Channel, ResponseData<EAllError>, DelAppForm> adminDelApp = (Channel ch, ResponseData<EAllError> res, DelAppForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        if( adminCommonRepository.getAdminUserByEmailPassword(token.getUid(), form.getPassword()) == AdminUserRec.Empty )
            return res.setError(EAllError.unauthorized_user);

        if( adminCommonRepository.getAdminApp(token.getUid(), form.getScode()) == null) {
            return res.setError(EAllError.NotExistScode);
        }
        adminCommonRepository.updateAdminAppStatus(token.getUid(), form.getScode(), EAdminAppStatus.DELETED);
        //TODO
        //TODO
        //TODO scode 접근 못하도록 막아야 함
        //TODO
        //TODO

        //DbAdminManager.getInst().removeDatabase(form.getScode());
        return res.setError(EAllError.ok);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AppListForm> adminAppList = (Channel ch, ResponseData<EAllError> res, AppListForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        List<AdminAppRec> adminAppList = adminCommonRepository.getAdminAppList(token.getUid(), form.getAppStatus(), form.getOffset(), form.getCount());
        if(adminAppList.size()<1)
            return res.setError(EAllError.eNoListData);

        return res.setError(EAllError.ok).setParam("adminAppList", adminAppList);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, ModifyAppForm> adminModifyApp = (Channel ch, ResponseData<EAllError> res, ModifyAppForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        if( adminCommonRepository.getAdminUserByEmailPassword(token.getUid(), form.getPassword()) == AdminUserRec.Empty )
            return res.setError(EAllError.unauthorized_user);

        if( adminCommonRepository.updateAdminApp(form.getUid(), form.getScode(), form.getTitle(), form.getDescription(), form.getAppStatus(),
                form.getFcmId(), form.getFcmKey()) == false )
            return res.setError(EAllError.eFailedToUpdateApp);
        return res.setError(EAllError.ok).setParam("appId", form.getScode());
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AppCountForm> adminAppCount = (Channel ch, ResponseData<EAllError> res, AppCountForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        int count = adminCommonRepository.getAdminAppCount(token.getUid(), form.getAppStatus());
        return res.setError(EAllError.ok).setParam("count", count);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, UpdateAppForm> updateApp = (Channel ch, ResponseData<EAllError> res, UpdateAppForm form) -> {
        if(form.getToken()==null || form.getToken().length()<1)
            return res.setError(EAllError.InvalidAdminToken);
        AdminToken token = new AdminToken().dec(form.getToken());
        if(token.isDecrypted()==false)
            return res.setError(EAllError.InvalidAdminToken);
        if(token.isExpired()==true)
            return res.setError(EAllError.ExpiredAdminToken);
        if( adminCommonRepository.isAvailableToken(token.getUid(), form.getToken()) == false )
            return res.setError(EAllError.UnauthorizedOrExpiredUser);

        if( adminCommonRepository.getAdminUserByEmailPassword(token.getUid(), form.getPassword()) == AdminUserRec.Empty )
            return res.setError(EAllError.unauthorized_user);

        if( adminCommonRepository.updateAdminAppStatus(token.getUid(), form.getScode(), form.getAppStatus()) == false )
            return res.setError(EAllError.eFailedToUpdateApp);
        return res.setError(EAllError.ok).setParam("appId", form.getScode());
    };

}
