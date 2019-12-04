package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class BoardVoteRec extends DbRecord {

	private String boardId;
	private String userId;
	private String userName;
	private boolean closed;
	private LocalDateTime expiredAt;

	public BoardVoteRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardVoteRec rec = (BoardVoteRec)r;
		rec.boardId = rd.getString("boardId");
		rec.userId = rd.getString("userId");
		rec.userName = rd.getString("userName");
		rec.closed = rd.getBoolean("closed");
		rec.expiredAt = rd.getLocalDateTime("expiredAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardVoteRec(poolName));
	}

	public boolean insert(String boardId, String userId, String userName, LocalDateTime expiredAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String expiredAtStr = expiredAt.format(formatter);
		String sql = String.format("INSERT INTO vote (boardId, userId, userName, expiredAt) VALUES('%s', '%s', '%s', '%s')",
								boardId, userId, userName, expiredAtStr);
		return super.insert(sql);
	}

	public boolean updateExpireTime(String boardId, String userId, LocalDateTime expiredAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String expiredAtStr = expiredAt.format(formatter);

		String sql = String.format("UPDATE vote SET expiredAt='%s' WHERE boardId='%s' AND userId='%s'",  expiredAtStr, boardId, userId);
		return super.update(sql);
	}

	public boolean updateClose(String boardId, String userId, boolean closed) {
		String sql = String.format("UPDATE vote SET closed=%b WHERE boardId='%s' AND userId='%s'",  closed, boardId, userId);
		return super.update(sql);
	}

	public boolean delete(String boardId, String userId) {
		String sql = String.format("DELETE FROM vote WHERE boardId='%s' AND userId='%s'",  boardId, userId);
		return super.delete(sql);
	}

	public BoardVoteRec getVoteInfo(String boardId) {
		String sql = String.format("SELECT * FROM vote WHERE boardId='%s'",  boardId);
		return (BoardVoteRec) super.getOne(sql);
	}

	public List<BoardVoteRec> getVoteInfoList(List<String> boardIds) {
		String commaBoardIds = boardIds.stream().map(e->"'"+e+"'").collect(Collectors.joining(","));
		String sql = String.format("SELECT * FROM vote WHERE boardId IN (%s)",  commaBoardIds);
		return super.getList(sql).stream().map(e->(BoardVoteRec)e).collect(Collectors.toList());
	}

}
