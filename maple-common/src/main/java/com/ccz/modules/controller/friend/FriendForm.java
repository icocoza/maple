package com.ccz.modules.controller.friend;

import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EFriendType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

public class FriendForm {

    @Data
    public class IdName {
        private String userId;
        private String userName;

        public IdName(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }
    }

    @Data
    public class NameType {
        private String userName;
        private EFriendType friendType;

        public NameType(String userName, EFriendType friendType) {
            this.userName = userName;
            this.friendType = friendType;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class FriendNameForm extends CommonForm {
        private String userName;
    }

    @ApiModel(description = "Add Friend Form")
    @Data
    @EqualsAndHashCode(callSuper=false)
    public class AddFriendForm extends FriendNameForm {
        @ApiModelProperty(value = "friend type", example = "friend type", required = true)
        private EFriendType friendType;
        @ApiModelProperty(value = "friend id and name", example = "friend id and name", required = true)
        private List<IdName> friendList = new ArrayList<>();
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class DelFriendForm extends FriendNameForm{
        @ApiModelProperty(value = "friend id list", example = "friend id list", required = true)
        private List<String> friendIds;
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class ChangeFriendType extends FriendNameForm {
        private List<NameType> friendTypeList;
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class FriendCountForm extends FriendNameForm {
        private EFriendType friendType;
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class FriendIdListForm extends FriendNameForm {
        private EFriendType friendType;
        private int offset, count;
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class FriendInfoForm extends DelFriendForm {

    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class AppendMeForm extends FriendNameForm {
        private int offset;
        private int count;
        private EFriendType friendType = EFriendType.friend;
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public class BlockMeForm extends AppendMeForm{
        public BlockMeForm() {
            super.friendType = EFriendType.block;
        }
    }

}
