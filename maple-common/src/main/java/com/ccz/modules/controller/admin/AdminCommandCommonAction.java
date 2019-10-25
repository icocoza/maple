package com.ccz.modules.controller.admin;


import com.ccz.modules.common.action.CommandAction;
import com.ccz.modules.common.action.ResponseData;
import com.ccz.modules.common.action.SessionManager;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.common.utils.KeyGen;
import com.ccz.modules.common.utils.StrUtils;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import com.ccz.modules.domain.constant.EAdminCmd;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.repository.db.admin.AdminAppRec;
import com.ccz.modules.repository.db.admin.AdminCommonRepository;
import com.ccz.modules.repository.db.admin.AdminUserRec;
import com.ccz.modules.controller.admin.AdminForm.*;
import com.ccz.modules.server.service.DbAdminManager;
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

        super.setCommandFunction(makeCommandId(EAdminCmd.adminregister), doRegister);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminlogin), doLogin);
        super.setCommandFunction(makeCommandId(EAdminCmd.adminlogout), adminLogout);
        super.setCommandFunction(makeCommandId(EAdminCmd.addapp), addApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.delapp), delApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.applist), appList);
        super.setCommandFunction(makeCommandId(EAdminCmd.modifyapp), modifyApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.appcount), appCount);
        super.setCommandFunction(makeCommandId(EAdminCmd.stopapp), updateApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.runapp), updateApp);
        super.setCommandFunction(makeCommandId(EAdminCmd.readyapp), updateApp);
    }

    private String getRemoteIp(Channel ch) {
        return ((InetSocketAddress)ch.remoteAddress()).getAddress().getHostAddress();
    }

    @Override
    public String makeCommandId(Enum e) {
        return adminCommonRepository.getPoolName() + ":" + e.name();
    }

    ICommandFunction<Channel, ResponseData<EAllError>, AdminUserForm> doRegister = (Channel ch, ResponseData<EAllError> res, AdminUserForm form) -> {
        if(StrUtils.isEmail(form.getEmail())==false)
            return res.setError(EAllError.invalid_email_format);

        if( adminCommonRepository.getAdminUser(form.getEmail()) != AdminUserRec.Empty)
            return res.setError(EAllError.already_exist_email);

        if(form.getPassword().length() < 8)
            return res.setError(EAllError.short_password_length_than_8);

        if( adminCommonRepository.addAdminUser(form.getEmail(), form.getPassword(), form.getAdminStatus(),
                form.getUserRole(), form.getUserName(), form.getNationality()) == false)
            return res.setError(EAllError.register_failed);
        return res.setError(EAllError.ok).setParam(form.getEmail());
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AdminLoginForm> doLogin = (Channel ch, ResponseData<EAllError> res, AdminLoginForm form) -> {
        AdminUserRec rec = adminCommonRepository.getAdminUser(form.getEmail());
        if(rec== DbRecord.Empty)
            return res.setError(EAllError.eNotExistUser);

        if(rec.getPassword().equals(form.getPassword()) == false)
            return res.setError(EAllError.eWrongAccountInfo);

        String token = StrUtils.getSha256Uuid("token:");
        adminCommonRepository.upsertAdminToken(form.getEmail(), token, getRemoteIp(ch));
        return res.setError(EAllError.ok).setParam("email", form.getEmail()).setParam("token", token);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AdminLogoutForm> adminLogout = (Channel ch, ResponseData<EAllError> res, AdminLogoutForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        adminCommonRepository.updateLeave(form.getEmail());
        adminCommonRepository.updateAdminToken(form.getEmail(), "");
        return res.setError(EAllError.ok).setParam("email", form.getEmail());
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AddAppForm> addApp = (Channel ch, ResponseData<EAllError> res, AddAppForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        if(StrUtils.isAlphaNumeric(form.getScode()) == false)
            return res.setError(EAllError.scode_allowed_only_alphabet);
        if( adminCommonRepository.hasAdminSCode(form.getScode()) == true )
            return res.setError(EAllError.already_exist_scode);

        String appId = KeyGen.makeKeyWithSeq("appId:");
        String appToken = Crypto.AES256Cipher.getInst().enc(appId+ AsciiSplitter.ASS.CHUNK+form.getScode());

        if( (res = this.createAppDatabase(res, form)).getError() != EAllError.ok)
            return res;

        if(adminCommonRepository.addAdminApp(form.getScode(), appId, appToken, form.getEmail(), form.getTitle(),
                form.getVersion(), form.getDescription(), form.getAppStatus(), form.getFcmId(), form.getFcmKey(),
                form.getStoreUrl(), form.isUpdateForce()) == false)
            return res.setError(EAllError.failed_to_add_app);
        return res.setError(EAllError.ok).setParam("appId", appId).setParam("appToken", appToken);
    };

    private ResponseData<EAllError> createAppDatabase(ResponseData<EAllError> res, AddAppForm form) {
        if( form.isEmptyExternalDb() == true) {
            if( DbAdminManager.getInst().createDatabaseOnInternal(form.getScode(), form.getScode()) == false )
                return res.setError(EAllError.failed_to_create_app_database);
        }else {
            if( DbAdminManager.getInst().createDatabaseOnExternal(form.getScode(), form.getScode(), form.getDbHost(), form.getDbOptions(), form.getDbUserId(), form.getDbPassword()) == false )
                return res.setError(EAllError.invalid_db_parameter);
            adminCommonRepository.updateExternalDbInfo(form.getEmail(), form.getScode(), form.getDbHost(), form.getDbOptions(), form.getDbUserId(), form.getDbPassword());
        }

        DbAdminManager.getInst().createTables(form.getScode());
        DbAdminManager.getInst().removeDatabase(form.getScode());
        return res.setError(EAllError.ok);
    }

    ICommandFunction<Channel, ResponseData<EAllError>, DelAppForm> delApp = (Channel ch, ResponseData<EAllError> res, DelAppForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        if( adminCommonRepository.getAdminUser(form.getEmail(), form.getConfirmPassword()) == AdminUserRec.Empty )
            return res.setError(EAllError.unauthorized_user);

        adminCommonRepository.updateAdminAppStatus(form.getEmail(), form.getAppId(), EAdminAppStatus.delete);
        //TODO
        //TODO
        //TODO scode 접근 못하도록 막아야 함
        //TODO
        //TODO

        //DbAdminManager.getInst().removeDatabase(form.getScode());
        return res.setError(EAllError.ok);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AppListForm> appList = (Channel ch, ResponseData<EAllError> res, AppListForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        List<AdminAppRec> appList = adminCommonRepository.getAdminAppList(form.getEmail(), form.getAppStatus(), form.getOffset(), form.getCount());
        if(appList.size()<1)
            return res.setError(EAllError.eNoListData);

        return res.setError(EAllError.ok).setParam("result", appList);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, ModifyAppForm> modifyApp = (Channel ch, ResponseData<EAllError> res, ModifyAppForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        if( adminCommonRepository.getAdminUser(form.getEmail(), form.getConfirmPassword()) == AdminUserRec.Empty )
            return res.setError(EAllError.unauthorized_user);

        if( adminCommonRepository.updateAdminApp(form.getEmail(), form.getAppId(), form.getTitle(), form.getVersion(),
                form.isUpdateForce(), form.getStoreUrl(), form.getDescription(), form.getAppStatus(),
                form.getFcmId(), form.getFcmKey()) == false )
            return res.setError(EAllError.eFailedToUpdateApp);
        return res.setError(EAllError.ok).setParam("appId", form.getAppId());
    };

    ICommandFunction<Channel, ResponseData<EAllError>, AppCountForm> appCount = (Channel ch, ResponseData<EAllError> res, AppCountForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        int count = adminCommonRepository.getAdminAppCount(form.getEmail(), form.getAppStatus());
        return res.setError(EAllError.ok).setParam("count", count);
    };

    ICommandFunction<Channel, ResponseData<EAllError>, UpdateAppForm> updateApp = (Channel ch, ResponseData<EAllError> res, UpdateAppForm form) -> {
        if( adminCommonRepository.isAvailableToken(form.getEmail(), form.getToken(), getRemoteIp(ch)) == false )
            return res.setError(EAllError.unauthorized_or_expired_user);

        if( adminCommonRepository.getAdminUser(form.getEmail(), form.getConfirmPassword()) == AdminUserRec.Empty )
            return res.setError(EAllError.unauthorized_user);

        if( adminCommonRepository.updateAdminAppStatus(form.getEmail(), form.getAppId(), form.getAppStatus()) == false )
            return res.setError(EAllError.eFailedToUpdateApp);
        return res.setError(EAllError.ok).setParam("appId", form.getAppId());
    };

}
