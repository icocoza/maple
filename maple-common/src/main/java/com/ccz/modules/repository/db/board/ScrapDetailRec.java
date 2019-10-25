package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScrapDetailRec extends DbRecord {

	private String scrapId;
	private String url;
	private String title;
	private String subTitle;
	private String scrapIp;
	private String scrapPath;
	private String scrapExt;
	private String scrapimg;
	
	private String body;
	
	public ScrapDetailRec(String poolName) {
		super(poolName);
		// TODO Auto-generated constructor stub
	}
	
	public ScrapDetailRec(DbReader rd) {
		super("");
		doLoad(rd, this);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		ScrapDetailRec rec = (ScrapDetailRec)r;
		rec.scrapId = rd.getString("scrapId");
		rec.url = rd.getString("url");
		rec.title = rd.getString("title");
		rec.subTitle = rd.getString("subTitle");
		rec.scrapIp = rd.getString("scrapIp");
		rec.scrapPath = rd.getString("scrapPath");
		rec.scrapExt = rd.getString("scrapExt");
		rec.scrapimg = String.format("http://%s:8080/scrap?scrapId=%s", rec.scrapIp, rec.scrapId);
		rec.body = rd.getString("body");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new ScrapDetailRec(poolName));
	}
	
	public List<ScrapDetailRec> getScrapDetailList(String boardId) {
		String sql = String.format("SELECT * FROM scrap "
				+ "LEFT JOIN scrapbody ON scrap.scrapid = scrapbody.scrapid "
				+ "LEFT JOIN boardscrap ON scrap.scrapid = boardscrap.scrapid "
				+ "WHERE boardscrap.boardid='%s'", boardId);
		return super.getList(sql).stream().map(e->(ScrapDetailRec)e).collect(Collectors.toList());
	}

}
