package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardVoteUserRec extends DbRecord {

	private String boardId;
	private String userId;
	private String voteItemId;
	private LocalDateTime votedAt;

	public BoardVoteUserRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardVoteUserRec rec = (BoardVoteUserRec)r;
		rec.boardId = rd.getString("boardId");
		rec.userId = rd.getString("userId");
		rec.voteItemId = rd.getString("voteItemId");
		rec.votedAt = rd.getLocalDateTime("votedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardVoteUserRec(poolName));
	}

	public boolean insert(String userId, String boardId, String voteItemId) {
		String sql = String.format("INSERT INTO voteUser (boardId, userId, voteItemId) VALUES('%s','%s','%s')", boardId, userId, voteItemId);
		return super.insert(sql);
	}

	public boolean updateSelectItem(String userId, String boardId, String voteItemId) {
		String sql = String.format("UPDATE voteUser SET voteItemId='%s', votedAt=NOW() WHERE userId='%s' AND boardId='%s'",
				voteItemId, userId, boardId);
		return super.update(sql);
	}

	public boolean delete(String userId, String boardId) {
		String sql = String.format("DELETE FROM voteUser WHERE userId='%s' AND boardId='%s'",  userId, boardId);
		return super.delete(sql);
	}

	public BoardVoteUserRec getVoteUser(String userId, String boardId) {
		String sql = String.format("SELECT * FROM voteUser WHERE userId='%s' AND boardId='%s'",  userId, boardId);
		return (BoardVoteUserRec) super.getOne(sql);
	}

	public List<BoardVoteUserRec> getVoteUserList(String boardId) {
		String sql = String.format("SELECT * FROM voteUser WHERE boardId = '%s'",  boardId);
		return super.getList(sql).stream().map(e->(BoardVoteUserRec)e).collect(Collectors.toList());
	}

}
