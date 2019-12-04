package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class BoardVoteUserCountRec extends DbRecord {
	private String boardId;
	private int voteCount=0;

	public BoardVoteUserCountRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardVoteUserCountRec rec = (BoardVoteUserCountRec)r;

		rec.boardId = rd.getString("boardId");
		rec.voteCount = rd.getInt("voteCount");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardVoteUserCountRec(poolName));
	}
	
	public Map<String, Integer>  getVoteCount(List<String> boardIds) {
		String queryBoardIds = boardIds.stream().map(x -> "'" + x + "'").collect(Collectors.joining(","));
		String sql = String.format("SELECT boardId, count(boardId) as voteCount FROM voteUser WHERE boardId in (%s) group by boardId", queryBoardIds);
		return super.getList(sql).stream().map(e->(BoardVoteUserCountRec)e).collect(Collectors.toMap(x->x.boardId, x->x.voteCount));
	}

	public Map<String, Integer>  getVotedBoardId(String userId, List<String> boardIds) {
		String queryBoardIds = boardIds.stream().map(x -> "'" + x + "'").collect(Collectors.joining(","));
		String sql = String.format("SELECT boardId FROM voteUser WHERE boardId in (%s) AND userId='%s'", queryBoardIds, userId);
		return super.getList(sql).stream().map(e->(BoardVoteUserCountRec)e).collect(Collectors.toMap(x->x.boardId, x->x.voteCount));
	}

}
