package com.ccz.modules.common.repository;

import com.ccz.modules.common.dbhelper.DbHelper;

import java.util.List;

public abstract class CommonRepository {

    public abstract String getPoolName();

    public boolean multiQueries(List<String> queries) {
        return DbHelper.multiQuery(getPoolName(), queries.toArray(new String[queries.size()]));
    }

}
