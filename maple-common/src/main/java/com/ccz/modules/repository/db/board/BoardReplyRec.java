package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EBoardPreferences;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardReplyRec extends DbRecord {

	private String replyId;
	private String parentId;
	private String boardId;
	private String userId;
	private String userName;
	private int depth;
	private String body;
	private LocalDateTime replyAt;
	private LocalDateTime deletedAt;

	public BoardReplyRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardReplyRec rec = (BoardReplyRec)r;
		rec.replyId = rd.getString("replyId");
		rec.parentId = rd.getString("parentId");
		rec.boardId = rd.getString("boardId");
		rec.userId = rd.getString("userId");
		rec.userName = rd.getString("userName");
		rec.depth = rd.getInt("depth");
		rec.body = rd.getString("body");
		rec.replyAt = rd.getLocalDateTime("replyAt");
		rec.deletedAt = rd.getLocalDateTime("deletedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardReplyRec(poolName));
	}

	public boolean insert(String replyId, String boardId, String parentId, String userId, String userName, int depth, String body) {
		String sql = String.format("INSERT INTO boardReply (replyId, parentId, boardId, userId, userName, depth, body) "
						+ "VALUES('%s', '%s', '%s', '%s', '%s', %d, '%s')", replyId, parentId, boardId, userId, userName, depth, body);
		return super.insert(sql);
	}

	public boolean delete(String replyId, String userId) {
		String sql = String.format("DELETE FROM boardReply WHERE replyId='%s' AND userId='%s", replyId, userId);
		return super.delete(sql);
	}

	public boolean deleteIfNoChild(String replyId, String boardId, String userId) {
		String sql = String.format("SELECT replyId FROM boardReply WHERE parentId='%s'", replyId);
		if(super.exist(sql)==false) {	//delete a record if not exist child, else update messge(means delete only body)
			sql = String.format("DELETE FROM %s WHERE boardId='%s' AND replyId='%s' AND userId='%s'", boardId, replyId, userId);
			return super.delete(sql);
		}
		return updateMsg(replyId, userId, "-- deleted by user --");
	}

	public boolean updateMsg(String replyId, String userId, String body) {
		String sql = String.format("UPDATE boardReply SET body='%s' WHERE replyId='%s' AND userId='%s'", body, replyId, userId);
		return super.update(sql);
	}

	public List<BoardReplyRec> getList(String boardId, int offset, int count) {
		String sql = String.format("SELECT * FROM boardReply WHERE boardId='%s' LIMIT %d, %d",
				boardId, offset, count);

		return super.getList(sql).stream().map(e->(BoardReplyRec)e).collect(Collectors.toList());
	}

}
