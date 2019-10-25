package com.ccz.modules.controller.admin;

import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import com.ccz.modules.domain.constant.EAdminStatus;
import com.ccz.modules.domain.constant.EFriendType;
import com.ccz.modules.domain.constant.EUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class AdminForm {

    @Data
    public class AdminCommonForm extends CommonForm{
        @ApiModelProperty(value="이메일 주소", example="email@test.com", required=true)
        private String email;
        @ApiModelProperty(value="토큰", example="token string", required=true)
        private String token;
    }

    @Data
    public class AdminUserForm extends AdminCommonForm {
        private String password;
        private EAdminStatus adminStatus;
        private EUserRole userRole;
        private String userName;
        private String nationality;
    }

    @Data
    public class AdminLoginForm extends AdminCommonForm {
        private String password;
    }

    @Data
    public class AdminLogoutForm extends AdminCommonForm {
    }

    @Data
    public class AddAppForm extends AdminCommonForm {
        private String title;
        private String version;
        private String storeUrl;
        private String description;
        private EAdminAppStatus appStatus;
        private boolean updateForce;
        private String fcmId;
        private String fcmKey;

        //Optional, 외부 DB 사용시
        private String dbHost;
        private String dbOptions;
        private String dbUserId;
        private String dbPassword;

        public boolean isEmptyExternalDb() {
            return dbHost == null && dbOptions == null && dbUserId == null && dbPassword == null;
        }
    }

    @Data
    public class DelAppForm extends AdminCommonForm {
        private String scode;
        private String appId;
        private String confirmPassword;
    }

    @Data
    public class AppListForm extends AdminCommonForm {
        private int offset;
        private int count;
        private EAdminAppStatus appStatus;
    }

    @Data
    public class ModifyAppForm extends AddAppForm {
        private String appId;

        private String confirmPassword;
    }

    @Data
    public class AppCountForm extends AdminCommonForm {
        private EAdminAppStatus appStatus;
    }

    @Data
    public class UpdateAppForm extends ModifyAppForm {
        private EAdminAppStatus appStatus;
    }
}
