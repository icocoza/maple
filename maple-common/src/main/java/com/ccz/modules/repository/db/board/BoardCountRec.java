package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class BoardCountRec extends DbRecord {

	private String boardId;
	private int likes;
	private int dislikes;
	private int visit;
	private int reply;
	private LocalDateTime modifiedAt;

	public BoardCountRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardCountRec rec = (BoardCountRec)r;
		rec.boardId = rd.getString("boardId");
		rec.likes = rd.getInt("likes");
		rec.dislikes = rd.getInt("dislikes");
		rec.visit = rd.getInt("visit");
		rec.visit = rd.getInt("reply");
		rec.modifiedAt = rd.getLocalDateTime("modifiedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardCountRec(poolName));
	}

	public boolean insert(String boardId) {
		String sql = String.format("INSERT INTO boardCount(boardId) VALUES('%s')", boardId);
		return super.insert(sql);
	}

	public boolean incLike(String  boardId, boolean bInc) {
		String sql = String.format("UPDATE boardCount SET likes=likes+%d WHERE  boardId='%s'", bInc? 1:-1,  boardId);
		return super.update(sql);
	}

	public boolean incDislike(String  boardId, boolean bInc) {
		String sql = String.format("UPDATE boardCount SET dislikes=dislikes+%d WHERE  boardId='%s'", bInc? 1:-1,  boardId);
		return super.update(sql);
	}

	public boolean incVisit(String  boardId) {
		String sql = String.format("UPDATE boardCount SET visit=visit+1 WHERE  boardId='%s'",  boardId);
		return super.update(sql);
	}

	public boolean incReply(String  boardId, boolean bInc) {
		String sql = String.format("UPDATE boardCount SET reply=reply+%d WHERE  boardId='%s'",  bInc? 1:-1, boardId);
		return super.update(sql);
	}

	public BoardCountRec getCount(String  boardId) {
		String sql = String.format("SELECT * FROM boardCount WHERE  boardId='%s'",  boardId);
		return (BoardCountRec) super.getOne(sql);
	}

}
