package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScrapRec extends DbRecord {
	
	private String scrapId;
	private String url;
	private String title;
	private String subTitle;
	private String scrapIp;
	private String scrapPath;
	private String scrapExt;
	private String scrapimg;
	
	public ScrapRec(String poolName) {
		super(poolName);
		// TODO Auto-generated constructor stub
	}
	
	public ScrapRec(DbReader rd) {
		super("");
		doLoad(rd, this);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		ScrapRec rec = (ScrapRec)r;
		rec.scrapId = rd.getString("scrapId");
		rec.url = rd.getString("url");
		rec.title = rd.getString("title");
		rec.subTitle = rd.getString("subTitle");
		rec.scrapIp = rd.getString("scrapIp");
		rec.scrapPath = rd.getString("scrapPath");
		rec.scrapExt = rd.getString("scrapExt");
		rec.scrapimg = String.format("http://%s:8080/scrap?scrapId=%s", rec.scrapIp, rec.scrapId);
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new ScrapRec(poolName));
	}
	
	public boolean insert(String scrapId, String url, String title, String subTitle) {
		String sql = qInsertScrap(scrapId, url, title, subTitle);
		return super.insert(sql);
	}

	public static String qInsertScrap(String scrapId, String url, String title, String subTitle) {
		return String.format("INSERT INTO %s (scrapId, url, title, subTitle) VALUES('%s', '%s', '%s', '%s')",
							  scrapId, url, title, subTitle);
	}
	
	public ScrapRec getScrap(String scrapId) {
		String sql = String.format("SELECT * FROM %s WHERE scrapId='%s'", scrapId);
		return (ScrapRec) super.getOne(sql);
	}
	
	public List<ScrapRec> getScrapList(List<String> scrapIds) {
		String inClause = scrapIds.stream().map(x->"'" + x + "'").collect(Collectors.joining(","));
		String sql = String.format("SELECT * FROM %s WHERE scrapId IN (%s)", inClause);
		return super.getList(sql).stream().map(e->(ScrapRec)e).collect(Collectors.toList());
	}

	public List<ScrapRec> getScrapListByUrl(List<String> urls) {
		String inClause = urls.stream().map(x->"'" + x + "'").collect(Collectors.joining(","));
		String sql = String.format("SELECT * FROM %s WHERE url IN (%s)", inClause);
		return super.getList(sql).stream().map(e->(ScrapRec)e).collect(Collectors.toList());
	}
	
	public boolean updateScrap(String scrapId, String scrapIp, String scrapPath, String scrapExt) {
		String sql = String.format("UPDATE %s SET scrapIp='%s', scrapPath='%s', scrapExt='%s' WHERE scrapId='%s'", scrapIp, scrapPath, scrapExt, scrapId);
		return super.update(sql);
	}

}
