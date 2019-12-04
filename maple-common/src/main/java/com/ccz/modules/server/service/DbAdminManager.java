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

    private String adminDbName;
    private String adminHost;
    private String adminOptions;
    private String adminUser;
    private String adminPassword;

    private List<String> appTableQueryList = new ArrayList<>();
    private List<String> adminTableQueryList = new ArrayList<>();

    AdminCommonRepository adminCommonRepository = new AdminCommonRepository();

    public void initAdminCommon(String dbName, String host, String options, String userId, String password) throws IOException {
        this.adminDbName = dbName;
        this.adminHost = host;
        this.adminOptions = options;
        this.adminUser = userId;
        this.adminPassword = password;

        loadAdminTableQueries();
        loadAppTableQueries();
    }

    public boolean createAdminDatabase() {
        return new DatabaseMaker().createAdminDatabase();
    }
    public void removeAdminConnectionPoolForCreateDatabase() {
        DbConnMgr.getInst().removeConnectionPool(adminDbName);
    }

    public boolean createAdminConnectionPool() {
        String jdbcUrl = String.format("jdbc:mysql://%s/%s?%s", adminHost, adminDbName, adminOptions);
        try {
            DbConnMgr.getInst().createConnectionPool(adminDbName, jdbcUrl, adminUser, adminPassword, 2, 4);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createDatabaseOnInternal(String scode) {
        return new DatabaseMaker().createDatabase(scode, adminHost, adminOptions, adminUser, adminPassword);
    }

    public boolean createDatabaseOnExternal(String scode, String host, String options, String user, String pw) {
        return new DatabaseMaker().createDatabase(scode, host, options, user, pw);
    }

    public void removeDatabase(String dbName) {
        DbConnMgr.getInst().removeConnectionPool(dbName);
    }


    public AdminAppRec getServiceAppInfo(String dbName) {
        return adminCommonRepository.getAdminAppByScode(dbName);
    }

    public boolean createTables(String scode) {
        for(String query : appTableQueryList) {
            if( DbHelper.nonSelect(scode, query) == false )
                return false;
        }
        return true;
    }

    public boolean createAdminTables() {
        for(String query : adminTableQueryList) {
            DbHelper.nonSelect(adminDbName, query);
        }
        return true;
    }

    private void loadAppTableQueries() throws IOException {
        Collection<String> resources = ResourceList.getResources(Pattern.compile(".*\\.sql"));
        for (String resource : resources) {
            File file = new File(resource);
            if (file.isFile() == true && file.getName().contains("app.sql") == true) {
                String fileData = Files.asCharSource(file, Charsets.UTF_8).read();
                String[] queries = fileData.split("\n\n", -1);
                appTableQueryList = Arrays.stream(queries).collect(Collectors.toList());
            }
        }
    }

    private void loadAdminTableQueries() throws IOException {
        Collection<String> resources = ResourceList.getResources(Pattern.compile(".*\\.sql"));
        for (String resource : resources) {
            File file = new File(resource);
            if (file.isFile() == true && file.getName().contains("admin.sql") == true) {
                String fileData = Files.asCharSource(file, Charsets.UTF_8).read();
                String[] queries = fileData.split("\n\n", -1);
                adminTableQueryList = Arrays.stream(queries).collect(Collectors.toList());
            }
        }
    }

    private class DatabaseMaker {

        public boolean createDatabase(String dbName, String host, String options, String user, String pw) {
            try {
                DbConnMgr.getInst().createConnectionPool(dbName, "jdbc:mysql://"+host+"?"+options, user, pw, 2, 4);
                return DbHelper.createDatabase(dbName, dbName);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean createAdminDatabase() {
            try {
                String jdbcUrl = String.format("jdbc:mysql://%s?%s", adminHost, adminOptions);
                DbConnMgr.getInst().createConnectionPool(adminDbName, jdbcUrl, adminUser, adminPassword, 2, 4);
                return DbHelper.createDatabase(adminDbName, adminDbName);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
