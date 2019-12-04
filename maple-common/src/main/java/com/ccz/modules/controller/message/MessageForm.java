package com.ccz.modules.controller.message;

import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EMessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public class MessageForm {

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class MessageIdForm extends CommonForm {
        private String userName;
        private String channelId;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ChatMessageForm extends MessageIdForm {
        private EMessageType messageType;
        private String content;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class SyncMessageForm extends MessageIdForm {
        private int offset;
        private int count;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ReadMessageForm extends MessageIdForm {
        private List<String> messageIds;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class DelMessageForm extends MessageIdForm {
        private List<String> messageIds;
    }

}

