package com.ccz.modules.repository.db.board;

import com.ccz.modules.common.dbhelper.DbReader;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.domain.constant.EBoardContentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoardRec extends DbRecord {

	private String boardId;
	private String userId;
	private String userName;
	private String title;
	private String shortContent;
	private boolean hasImage;
	private boolean hasFile;
	private String category;
	private EBoardContentType contentType;
    private LocalDateTime deletedAt;
	private LocalDateTime createdAt;

	public BoardRec(String poolName) {
		super(poolName);
	}

	@Override
	public boolean createTable() {
		return false;
	}

	@Override
	protected DbRecord doLoad(DbReader rd, DbRecord r) {
		BoardRec rec = (BoardRec)r;
		rec.boardId = rd.getString("boardId");
		rec.userId = rd.getString("userId");
		rec.userName = rd.getString("userName");
		rec.title = rd.getString("title");
		rec.shortContent = rd.getString("shortContent");
		rec.hasImage = rd.getBoolean("hasImage");
		rec.hasFile = rd.getBoolean("hasFile");
		rec.category = rd.getString("category");
		rec.contentType = EBoardContentType.getType(rd.getString("contentType"));
		rec.createdAt = rd.getLocalDateTime("createdAt");
		rec.deletedAt = rd.getLocalDateTime("deletedAt");
		return rec;
	}

	@Override
	protected DbRecord onLoadOne(DbReader rd) {
		return doLoad(rd, this);
	}

	@Override
	protected DbRecord onLoadList(DbReader rd) {
		return doLoad(rd, new BoardRec(poolName));
	}

	public boolean insert(String boardId, String userId, String userName, String title,
						  String shortContent, boolean hasImage, boolean hasFile, String category,
						  EBoardContentType contentType) {
		String sql = String.format("INSERT INTO board (boardId, userId, userName, title, "+
								   "shortContent, hasImage, hasFile, category, contentType)" +
				"VALUES('%s', '%s', '%s', '%s', '%s', %b, &b, '%s', '%s')",
				boardId, userId, userName, title, shortContent, hasImage, hasFile, category, contentType.getValue());
		return super.insert(sql);
	}

	public boolean updateDelete(String boardId, String userId) {
		String sql = String.format("UPDATE board SET deletedAt=NOW() WHERE userId='%s' AND boardId='%s'", userId, boardId);
		return super.update(sql);
	}

	public boolean updateBoard(String boardId, String userId, String title,
							   String shortContent, boolean hasImage, boolean hasFile, String category,
							   EBoardContentType contentType) {
		String sql = String.format("UPDATE board SET title='%s', shortContent='%s', hasImage=%b, hasFile=%b, " +
						"category='%s', contentType='%s' WHERE userId='%s' AND boardId='%s'",
						title, shortContent, hasImage, hasFile, category, contentType.getValue(), userId, boardId);
		return super.update(sql);
	}

	public boolean updateTitle(String boardId, String userId, String title) {
		String sql = String.format("UPDATE board SET title='%s' WHERE userId='%s' AND boardId='%s'", title, userId, boardId);
		return super.update(sql);
	}

	public boolean updateContent(String boardId, String userId, String content, boolean hasImage, boolean hasFile) {
		String sql = String.format("UPDATE board SET content='%s', hasImage=%b, hasFile=%b WHERE userId='%s' AND boardId='%s'",
				content, hasImage, hasFile, userId, boardId);
		return super.update(sql);
	}

	public boolean updateCategory(String boardId, String userId, String category) {
		String sql = String.format("UPDATE board SET category='%s' WHERE userId='%s' AND boardId='%s'", category, userId, boardId);
		return super.update(sql);
	}

	public List<BoardRec> getList(String category, int offset, int count) {
		String sql = String.format("SELECT * FROM board WHERE category='%s' ORDER BY createdAt DESC LIMIT %d, %d",
				category, offset, count);
		return super.getList(sql).stream().map(e->(BoardRec)e).collect(Collectors.toList());
	}

	public List<BoardRec> getList(String userId, String category, int offset, int count) {
		String sql = String.format("SELECT * FROM board WHERE userId='%s' AND category='%s' ORDER BY createdAt DESC LIMIT %d, %d",
				userId, category, offset, count);
		return super.getList(sql).stream().map(e->(BoardRec)e).collect(Collectors.toList());
	}
}
