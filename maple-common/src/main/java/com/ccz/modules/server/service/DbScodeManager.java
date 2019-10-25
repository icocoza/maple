package com.ccz.modules.server.service;

import com.ccz.modules.common.dbhelper.DbConnMgr;
import com.ccz.modules.common.dbhelper.DbHelper;
import com.ccz.modules.repository.db.admin.AdminAppRec;

import java.sql.SQLException;
import java.util.List;

public class DbScodeManager {

    public static DbScodeManager s_pThis;

    public static DbScodeManager getInst() {
        return s_pThis = (s_pThis == null ? new DbScodeManager() : s_pThis);
    }
    public static void freeInst() {		s_pThis = null; 	}

    public boolean initDbForServiceCode(String scode) throws SQLException {
        if( DbConnMgr.getInst().hasPool(scode) == true)
            return true;
        AdminAppRec rec = DbAdminManager.getInst().getServiceAppInfo(scode);
        if( rec == AdminAppRec.Empty)
            return false;
        DbConnMgr.getInst().createConnectionPool(scode, rec.getDbHost(), rec.getDbUserId(), rec.getDbPassword(),
                rec.getDbInitConn(), rec.getDbMaxConn());
        return true;
    }

}
