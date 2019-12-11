package com.ccz.modules.common.action;

import com.ccz.modules.common.dbhelper.DbConnMgr;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.utils.AsciiSplitter;
import com.ccz.modules.common.utils.Crypto;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import com.ccz.modules.domain.constant.EAllError;
import com.ccz.modules.repository.db.admin.AdminAppRec;
import com.ccz.modules.server.service.DbAdminManager;
import lombok.Data;

@Data
public class AppTokenHelper {
    private String appId;
    private String scode;

    private AdminAppRec app;

    private EAllError error = EAllError.ok;

    public AppTokenHelper(String appToken) {
        String appIdAndScode = Crypto.AES256Cipher.getInst().dec(appToken);//.enc(appId+ AsciiSplitter.ASS.CHUNK+form.getScode());
        String[] split = appIdAndScode.split(AsciiSplitter.ASS.CHUNK, -1);
        if(split.length<2) {
            error = EAllError.appTokenNotValidated;
            return;
        }

        appId = split[0];
        scode = split[1];

        app = DbAdminManager.getInst().getAppByScode(scode);
        if(app == DbRecord.Empty) {
            error = EAllError.NoServiceCode;
            return;
        }
        if(app.getStatus() != EAdminAppStatus.READY) {
            error = EAllError.NotAvailableServiceCode;
            return;
        }

        try {
            if( DbConnMgr.getInst().hasPool(scode) == true)
                return;
            DbConnMgr.getInst().createConnectionPool(scode, app.getDbHost(), app.getDbUserId(), app.getDbPassword(), app.getDbInitConn(), app.getDbMaxConn());
        }catch (Exception e) {
            e.printStackTrace();
            error = EAllError.FailToMakePooling;
        }
    }
}
