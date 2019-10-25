package com.ccz.modules.common.repository;

import com.ccz.modules.common.dbhelper.DbHelper;

import java.util.List;

public abstract class CommonRepository {

    public boolean multiQueries(String scode, List<String> queries) {
        if(queries == null || queries.size() < 1)
            return false;
        return DbHelper.multiQuery(scode, queries.toArray(new String[queries.size()]));
    }

}
