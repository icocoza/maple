package com.ccz.modules.controller.message;

import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EMessageType;
import lombok.Data;

import java.util.List;

public class MessageForm {

    @Data
    public class MessageIdForm extends CommonForm {
        private String userName;
        private String channelId;
    }

    @Data
    public class ChatMessageForm extends MessageIdForm {
        private EMessageType messageType;
        private String content;
    }

    @Data
    public class SyncMessageForm extends MessageIdForm {
        private int offset;
        private int count;
    }

    @Data
    public class ReadMessageForm extends MessageIdForm {
        private List<String> messageIds;
    }

    @Data
    public class DelMessageForm extends MessageIdForm {
        private List<String> messageIds;
    }

}

