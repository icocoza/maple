package com.ccz.modules.common.dbhelper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public abstract class DbRecord {
	@JsonIgnore
	public String poolName, serviceCode;
	@JsonIgnore
	public static final DbRecord Empty = null;

	abstract public boolean createTable();
    abstract protected DbRecord doLoad(DbReader rd, DbRecord r);
    
    abstract protected DbRecord onLoadOne(DbReader rd);
    abstract protected DbRecord onLoadList(DbReader rd);
    
    public DbRecord(String poolName) {
    	this.poolName = poolName;
    }
    
	protected DbRecord getOne(String sql)
    {
        DbReader rd = DbHelper.select(poolName, sql);
        try
        {
            if (rd != DbReader.Empty && rd.hasNext() )
            		return onLoadOne(rd);
            return DbRecord.Empty;
        }
        catch(Throwable e) {
        		e.printStackTrace();
        		return DbRecord.Empty;
        }
        finally
        {
            if (rd != DbReader.Empty)
                rd.close();
        }
    }
	
    protected List<DbRecord> getList(String sql)
    {
        DbReader rd = DbHelper.select(poolName, sql);
        return getList(rd);
    }
    
    protected List<DbRecord> getList(String sql, String ids[])
    {
        DbReader rd = DbHelper.preparedSelect(poolName, sql, ids);
        return getList(rd);
    }

    private List<DbRecord> getList(DbReader rd)
    {
        List<DbRecord> records = new LinkedList<DbRecord>();
        try
        {
            while (rd!=DbReader.Empty && rd.hasNext()==true)
                records.add(onLoadList(rd));
        }
        catch(Exception e) {
        		e.printStackTrace();
        }
        finally {
            if(rd != DbReader.Empty)
                rd.close();
        }
        return records;
    }

    protected boolean exist(String sql) {
    	return DbHelper.exist(poolName, sql);
    }
    
    protected int count(String sql) {
    	return DbHelper.count(poolName, sql);
    }
    
	protected boolean createTable(String sql) {
		return DbHelper.nonSelect(poolName, sql);
	}
	
	protected boolean insert(String sql) {
		return DbHelper.nonSelect(poolName, sql);
	}

	protected int insertAndGetKey(String sql) {
		return DbHelper.insertAndGetKey(poolName, sql);
	}

	protected boolean delete(String sql) {
		return DbHelper.nonSelect(poolName, sql);
	}
	
	protected boolean update(String sql) {
		return DbHelper.nonSelect(poolName, sql);
	}
}
