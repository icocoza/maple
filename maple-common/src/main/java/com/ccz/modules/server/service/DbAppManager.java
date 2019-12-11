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

public class DbAppManager {

    public static DbAppManager s_pThis;

    public static DbAppManager getInst() {
        return s_pThis = (s_pThis == null ? new DbAppManager() : s_pThis);
    }
    public static void freeInst() {		s_pThis = null; 	}

    private String inDbName;
    private String inDbHost;
    private String inDbOptions;
    private String inDbUser;
    private String inDbPassword;

    private List<String> appTableQueryList = new ArrayList<>();
    private List<String> adminTableQueryList = new ArrayList<>();

    AdminCommonRepository adminCommonRepository = new AdminCommonRepository();

    public void initInternalDb(String dbName, String host, String options, String userId, String password) throws IOException {
        this.inDbName = dbName;
        this.inDbHost = host;
        this.inDbOptions = options;
        this.inDbUser = userId;
        this.inDbPassword = password;

        loadAppTableQueries();
    }

    public boolean createDatabaseOnInternal(String dbName) {
        return new DatabaseMaker().createDatabase(dbName, inDbHost, inDbOptions, inDbUser, inDbPassword);
    }

    public boolean createDatabaseOnExternal(String scode, String host, String options, String user, String pw) {
        return new DatabaseMaker().createDatabase(scode, host, options, user, pw);
    }

    public boolean createConnectionPool(String dbName) {
        String jdbcUrl = String.format("jdbc:mysql://%s/%s?%s", inDbHost, dbName, inDbOptions);
        try {
            DbConnMgr.getInst().createConnectionPool(dbName, jdbcUrl, inDbUser, inDbPassword, 4, 8);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createConnectionPool(String dbName, String host, String options, String userId, String password) {
        String jdbcUrl = String.format("jdbc:mysql://%s/%s?%s", host, dbName, options);
        try {
            DbConnMgr.getInst().createConnectionPool(dbName, jdbcUrl, userId, password, 4, 8);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeDatabase(String dbName) {
        DbConnMgr.getInst().removeConnectionPool(dbName);
    }


    public boolean createTables(String dbName) {
        for(String query : appTableQueryList) {
            DbHelper.nonSelect(dbName, query);
        }
        return true;
    }

    private void loadAppTableQueries() throws IOException {
        Collection<String> resources = ResourceList.getResources(Pattern.compile(".*\\.sql"));
        for (String resource : resources) {
            File file = new File(resource);
            if (file.isFile() == true && file.getName().contains("apps.sql") == true) {
                String fileData = Files.asCharSource(file, Charsets.UTF_8).read();
                String[] queries = fileData.split("\n\n", -1);
                appTableQueryList = Arrays.stream(queries).collect(Collectors.toList());
            }
        }
    }

    private class DatabaseMaker {

        public boolean createDatabase(String dbName, String host, String options, String user, String pw) {
            try {
                DbConnMgr.getInst().createConnectionPool(dbName, "jdbc:mysql://"+host+"?"+options, user, pw, 1, 2);
                return DbHelper.createDatabase(dbName, dbName);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                DbConnMgr.getInst().removeConnectionPool(dbName);
            }
        }
    }


}
