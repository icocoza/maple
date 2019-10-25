package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class BoardScrapRec extends DbRecord {
	
	@Getter private String boardId, scrapId;

	public BoardScrapRec(String poolName) {
		super(poolName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardScrapRec rec = (BoardScrapRec)r;
		rec.boardId = rd.getString("boardId");
		rec.scrapId = rd.getString("scrapId");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardScrapRec(poolName));
	}
	
	public boolean insert(String boardId, String scrapId) {
		String sql = qInsertScrap(boardId, scrapId);
		return super.insert(sql);
	}

	public static String qInsertScrap(String boardId, String scrapId) {
		return String.format("INSERT INTO boardScrap (boardId, scrapId) VALUES('%s', '%s')", boardId, scrapId);
	}
	
	public BoardScrapRec getScrapId(String boardId) {
		String sql = String.format("SELECT * FROM boardScrap WHERE boardId='%s'", boardId);
		return (BoardScrapRec) super.getOne(sql);
	}
	
	public List<BoardScrapRec> getScrapIdList(String boardId) {
		String sql = String.format("SELECT * FROM boardScrap WHERE boardId='%s'", boardId);
		return super.getList(sql).stream().map(e->(BoardScrapRec)e).collect(Collectors.toList());
	}

	public boolean delete(String boardId) {
		String sql = String.format("DELETE FROM boardScrap WHERE boardId='%s'", boardId);
		return super.delete(sql);
	}

}
