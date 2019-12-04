package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScrapBodyRec extends DbRecord {

	private String scrapId;
	private String body;

	public ScrapBodyRec(String poolName) {
		super(poolName);
	}

	public ScrapBodyRec(DbReader rd) {
		super("");
		doLoad(rd, this);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		ScrapBodyRec rec = (ScrapBodyRec)r;
		rec.scrapId = rd.getString("scrapId");
		rec.body = rd.getString("body");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new ScrapBodyRec(poolName));
	}
	
	public boolean insert(String scrapId,  String body) {
		String sql = qInsertScrapBody(scrapId, body);
		return super.insert(sql);
	}

	public static String qInsertScrapBody(String scrapId, String body) {
		return String.format("INSERT INTO %s (scrapId, body) VALUES('%s', '%s')", scrapId, body);
	}
	
	public ScrapBodyRec getScrapBody(String scrapId) {
		String sql = String.format("SELECT * FROM %s WHERE scrapId='%s'", scrapId);
		return (ScrapBodyRec) super.getOne(sql);
	}
		
	public boolean updateScrapBody(String scrapId, String body) {
		String sql = String.format("UPDATE %s SET body='%s' WHERE scrapId='%s'", body, scrapId);
		return super.update(sql);
	}

}
