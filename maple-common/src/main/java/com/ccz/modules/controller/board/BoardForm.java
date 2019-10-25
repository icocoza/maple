package com.ccz.modules.controller.board;

import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EBoardContentType;
import com.ccz.modules.domain.constant.EBoardPreferences;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BoardForm {

    @Data
    public class AddBoardForm extends CommonForm {
        private String userName;
        private String title;
        private String content;
        private EBoardContentType contentType;
        private String category = "none";
        private boolean hasImage;
        private boolean hasFile;

        private List<String> fileIds;
    }

    @Data
    public class BoardIdForm extends CommonForm {
        private String boardId;
        private String userName;
    }

    @Data
    public class DelBoardForm extends BoardIdForm {
        ;
    }

    @Data
    public class UpdateTitleForm extends BoardIdForm {
        private String title;
    }

    @Data
    public class UpdateContentForm extends BoardIdForm {
        private String content;
        private boolean hasImage;
        private boolean hasFile;
    }

    @Data
    public class UpdateCategoryForm extends BoardIdForm {
        private String category;
    }

    @Data
    public class UpdateBoardForm extends AddBoardForm {
        private String boardId;
    }

    @Data
    public class BoardListForm extends CommonForm {
        private String category = "none";
        private String userName;
        private int offset;
        private int count;

    }

    @Data
    public class BoardContentForm extends BoardIdForm {
    }

    @Data
    public class BoardLikeForm extends BoardIdForm {
        private EBoardPreferences preferences;
        private boolean added;
    }

    @Data
    public class BoardDislikeForm extends BoardLikeForm {
    }

    @Data
    public class AddReplyForm extends BoardIdForm {
        private String parentReplyId = "0";
        private int depth;
        private String body;
    }

    @Data
    public class DelReplyForm extends BoardIdForm {
        private String replyId;
    }

    @Data
    public class ReplyListForm extends BoardIdForm {
        private int offset;
        private int count;
    }

    //for vote
    @Data
    public class AddVoteForm extends AddBoardForm {
        private LocalDateTime expiredAt;
        public List<String> itemList = new ArrayList<>();
    }

    @Data
    public class VoteItemListForm extends BoardIdForm {
    }

    @Data
    public class SelectVoteItemForm extends BoardIdForm {
        private String voteItemId;
        private boolean selected;
    }

    @Data
    public class VoteUpdateForm extends BoardIdForm {
        private LocalDateTime expiredAt;
        private Boolean closed;
        private List<VoteItemData> voteItems;
    }

    @Data
    public class VoteChangeForm extends BoardIdForm {
        private String voteItemId;
    }

    @Data
    public class VoteInfoListForm extends BoardIdForm {
        private List<String> boardIds;
    }

    @Data
    public class BoardSearch extends BoardIdForm {
        private String searchWord;
        private int offset;
        private int count;
    }

    ////inner data////
    @Data
    public class VoteItemData {
        private String voteItemId;
        private String voteText;
    }

}