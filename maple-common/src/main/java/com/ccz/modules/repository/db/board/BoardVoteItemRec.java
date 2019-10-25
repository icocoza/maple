package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardVoteItemRec extends DbRecord {

	private String voteItemId;
	private String boardId;
	private String itemText;
	private int selectCount;

	public BoardVoteItemRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardVoteItemRec rec = (BoardVoteItemRec)r;
		rec.voteItemId = rd.getString("voteItemId");
		rec.boardId = rd.getString("boardId");
		rec.itemText = rd.getString("itemText");
		rec.selectCount = rd.getInt("selectCount");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardVoteItemRec(poolName));
	}

	public boolean insert(String boardId, String voteItemId, String itemText) {
		String sql = String.format("INSERT INTO voteItem (voteItemId, boardId, itemText) VALUES('%s','%s','%s')", 
				voteItemId, boardId, itemText);
		return super.insert(sql);
	}

	public boolean incVoteItem(String boardId, String voteItemId) {
		String sql = String.format("UPDATE voteItem SET selectcount=selectcount+1 WHERE voteItemId='%s' AND boardId='%s'",
				 voteItemId, boardId);
		return super.update(sql);
	}

	public boolean decVoteItem(String boardId, String voteItemId) {
		String sql = String.format("UPDATE voteItem SET selectcount=selectcount-1 WHERE voteItemId='%s' AND boardId='%s'",
				 voteItemId, boardId);
		return super.update(sql);
	}

	public boolean updateVoteText(String boardId, String voteItemId, String itemText) {
		String sql = String.format("UPDATE voteItem SET itemText='%s' WHERE boardId ='%s' AND voteItemId='%s'",
				 itemText, boardId, voteItemId);
		return super.update(sql);
	}

	public boolean deleteVote(String boardId) {
		String sql = String.format("DELETE FROM voteItem WHERE boardId='%s'",  boardId);
		return super.delete(sql);
	}
	public boolean deleteVoteItem(String boardId, String voteItemId) {
		String sql = String.format("DELETE FROM voteItem WHERE boardId='%s' AND voteItemId='%s'",  boardId, voteItemId);
		return super.delete(sql);
	}

	public List<BoardVoteItemRec> getVoteItemList(String boardId) {
		String sql = String.format("SELECT * FROM voteItem WHERE boardId='%s'",  boardId);
		return super.getList(sql).stream().map(e->(BoardVoteItemRec)e).collect(Collectors.toList());
	}


}
