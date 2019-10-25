package com.ccz.modules.server.service;

import com.ccz.modules.common.dbhelper.DbConnMgr;
import com.ccz.modules.common.dbhelper.DbHelper;
import com.ccz.modules.common.utils.ResourceList;
import com.ccz.modules.repository.db.admin.AdminAppRec;
import com.ccz.modules.repository.db.admin.AdminCommonRepository;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DbAdminManager {

    public static DbAdminManager s_pThis;

    public static DbAdminManager getInst() {
        return s_pThis = (s_pThis == null ? new DbAdminManager() : s_pThis);
    }
    public static void freeInst() {		s_pThis = null; 	}

    private String adminHost;
    private String adminOptions;
    private String adminUser;
    private String adminPassword;

    private List<String> tableQueryList = new ArrayList<>();

    AdminCommonRepository adminCommonRepository = new AdminCommonRepository();

    public void initAdminCommon(String host, String options, String userId, String password) throws IOException {
        this.adminHost = host;
        this.adminOptions = options;
        this.adminUser = userId;
        this.adminPassword = password;

        loadCreateTableQueries();
    }

    public boolean createDatabaseOnInternal(String scode, String dbName) {
        return new DatabaseMaker().createDatabase(scode, dbName, adminHost, adminOptions, adminUser, adminPassword);
    }

    public boolean createDatabaseOnExternal(String scode, String dbName, String host, String options, String user, String pw) {
        return new DatabaseMaker().createDatabase(scode, dbName, host, options, user, pw);
    }

    public void removeDatabase(String dbName) {
        DbConnMgr.getInst().removeConnectionPool(dbName);
    }


    public AdminAppRec getServiceAppInfo(String scode) {
        return adminCommonRepository.getAdminAppByScode(scode);
    }

    private class DatabaseMaker {

        public boolean createDatabase(String poolName, String dbName, String host, String options, String user, String pw) {
            try {
                DbConnMgr.getInst().createConnectionPool(poolName, "jdbc:mysql://"+host+"?"+options, user, pw, 2, 4);
                return DbHelper.createDatabase(poolName, dbName);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean createTables(String scode) {
        for(String query : tableQueryList) {
            if( DbHelper.createDatabase(scode, query) == false )
                return false;
        }
        return true;
    }

    private void loadCreateTableQueries() throws IOException {
        Collection<String> resources = ResourceList.getResources(Pattern.compile(".*\\.sql"));
        for (String resource : resources) {
            File file = new File(resource);
            if (file.isFile() == true && file.getName().contains("createTables") == true) {
                String fileData = Files.asCharSource(file, Charsets.UTF_8).read();
                String[] queries = fileData.split("\n\n", -1);
                tableQueryList = Arrays.stream(queries).collect(Collectors.toList());
            }
        }
    }

}
