package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class BoardDetailRec extends BoardRec {

	private String serverIp;
	private String subPath;
	private String fileName;
	private String cropUrl;
	private int likes = 0;
	private int dislikes=0;
	private int visit=0;
	private int reply=0;
	private int votes=0;
	private ScrapRec scrap;
	private boolean voted = false;

	public BoardDetailRec(String poolName) {
		super(poolName);
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardDetailRec rec = (BoardDetailRec)super.doLoad(rd, r);
		rec.likes = rd.getInt("likes");
		rec.dislikes = rd.getInt("dislikes");
		rec.visit = rd.getInt("visit");
		rec.reply = rd.getInt("reply");
		
		rec.serverIp = rd.getString("serverIp");
		rec.subPath = rd.getString("subPath");
		rec.fileName = rd.getString("fileName");
		rec.cropUrl = getCropfilePath(rec);
		rec.scrap = new ScrapRec(rd);
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardDetailRec(poolName));
	}
	
	public List<BoardDetailRec> getBoardList(String category, int offset,  int count) {
		String sql = String.format("SELECT * FROM board " + 
				"LEFT JOIN boardcount ON board.boardid=boardcount.boardid " + 
				"LEFT JOIN filecrop ON board.boardid=filecrop.boardid "+
				"LEFT JOIN boardscrap ON board.boardid=boardscrap.boardid "+
				"LEFT JOIN scrap ON scrap.scrapid=boardscrap.scrapid " + 
				"WHERE board.category='%s' AND board.deleted=false ORDER BY board.createtime DESC LIMIT %d, %d", category, offset, count);
		return super.getList(sql).stream().map(e->(BoardDetailRec)e).collect(Collectors.toList());
	}
	
	public List<BoardDetailRec> getBoardList(List<String> boardids) {
		String ids = boardids.stream().map(x -> '\''+x+'\'').collect(Collectors.joining(","));
		String sql = String.format("SELECT * FROM board " + 
				"LEFT JOIN boardcount ON board.boardid=boardcount.boardid " + 
				"LEFT JOIN filecrop ON board.boardid=filecrop.boardid "+
				"LEFT JOIN boardscrap ON board.boardid=boardscrap.boardid "+
				"LEFT JOIN scrap ON scrap.scrapid=boardscrap.scrapid " + 
				"WHERE board.boardid IN (%s) ORDER BY FIELD(board.boardid, %s)", ids, ids);
		return super.getList(sql).stream().map(e->(BoardDetailRec)e).collect(Collectors.toList());
	}
	
	public String getCropfilePath(BoardDetailRec rec) {
		if(rec.fileName==null || rec.fileName.length()<1)
			return null;
		return String.format("http://%s:8080/%s?boardid=%s&scode=%s", rec.serverIp, rec.subPath, rec.getBoardId(), poolName);
	}
}
