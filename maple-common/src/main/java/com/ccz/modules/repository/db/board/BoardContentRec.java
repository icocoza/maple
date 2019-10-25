package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EBoardContentType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardContentRec extends DbRecord {

	private String boardId;
	private String userId;
	private String content;
	private LocalDateTime deletedAt;

	public BoardContentRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardContentRec rec = (BoardContentRec)r;
		rec.boardId = rd.getString("boardId");
		rec.userId = rd.getString("userId");
		rec.content = rd.getString("content");
		rec.deletedAt = rd.getLocalDateTime("deletedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardContentRec(poolName));
	}

	public boolean insert(String boardId, String userId, String content) {
		String sql = String.format("INSERT INTO boardContent (boardId, userId, content)" +
								   "VALUES('%s', '%s', '%s')", boardId, userId, content);
		return super.insert(sql);
	}

	public boolean updateDelete(String boardId, String userId) {
		String sql = String.format("UPDATE boardContent SET deletedAt=NOW() WHERE userId='%s' AND boardId='%s'", userId, boardId);
		return super.update(sql);
	}

	public boolean updateContent(String boardId, String userId, String content) {
		String sql = String.format("UPDATE boardContent SET content='%s' WHERE userId='%s' AND boardId='%s'", userId, boardId);
		return super.update(sql);
	}

	public String getContent(String boardId) {
		String sql = String.format("SELECT * FROM boardContent WHERE boardId='%s'", boardId);
		BoardContentRec content = (BoardContentRec) super.getOne(sql);
		return content != null? content.getContent() : null;
	}

}
