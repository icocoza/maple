package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EBoardPreferences;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class BoardLikeRec extends DbRecord {

	private String boardId;
	private String userId;
	private String userName;
	private EBoardPreferences preferences;
	private int score;
	private LocalDateTime votedAt;

	public BoardLikeRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardLikeRec rec = (BoardLikeRec)r;
		rec.boardId = rd.getString("boardId");
		rec.userId = rd.getString("userId");
		rec.userName = rd.getString("userName");
		rec.preferences = EBoardPreferences.getType(rd.getString("preferences"));
		rec.score = rd.getInt("score");
		rec.votedAt = rd.getLocalDateTime("votedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardLikeRec(poolName));
	}

	public boolean insert(String boardId, String userId, String userName, EBoardPreferences preferences) {
		String sql = String.format("INSERT INTO boardVoter (boardId, userId, userName, preferences) "
				+ "VALUES('%s', '%s', '%s', '%s')", boardId, userId, userName, preferences);
		return super.insert(sql);
	}

	public boolean delete(String boardId) {
		String sql = String.format("DELETE FROM boardVoter WHERE boardId='%s'", boardId);
		return super.delete(sql);
	}

	public boolean delete(String boardId, String userId, EBoardPreferences preferences ) {
		String sql = String.format("DELETE FROM boardVoter WHERE boardId='%s' AND userId='%s' AND preferences='%s'",
				boardId, userId, preferences);
		return super.delete(sql);
	}

	public BoardLikeRec getPreference(String boardId, String userId) {
		String sql = String.format("SELECT * FROM boardVoter WHERE boardId='%s' AND userId='%s'",boardId, userId);
		return (BoardLikeRec) super.getOne(sql);
	}

}
