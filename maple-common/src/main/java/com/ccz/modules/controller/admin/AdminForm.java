package com.ccz.modules.controller.admin;

import com.ccz.modules.controller.common.CommonForm;
import com.ccz.modules.domain.constant.EAdminAppStatus;
import com.ccz.modules.domain.constant.EAdminStatus;
import com.ccz.modules.domain.constant.EFriendType;
import com.ccz.modules.domain.constant.EUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "Admin Management Form")
public class AdminForm {

    @ApiModel(description = "Common Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AdminCommonForm extends CommonForm{
        @ApiModelProperty(value="User ID", example="uid:xxxxxxxxx")
        private String uid;
    }

    @ApiModel(description = "Add User Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AdminUserForm extends AdminCommonForm {
        @ApiModelProperty(value="Email", example="test@email.com", required=true)
        private String email;
        @ApiModelProperty(value="패스워드", example="*****", required=true)
        private String password;
        @ApiModelProperty(value="상태", hidden = true)
        private EAdminStatus adminStatus;
        @ApiModelProperty(value="사용자 롤", hidden = true)
        private EUserRole userRole = EUserRole.user;
        @ApiModelProperty(value="사용자 이름", example="고길동", required=true)
        private String userName;
    }

    @ApiModel(description = "Login Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AdminLoginForm extends AdminCommonForm {
        @ApiModelProperty(value="Email", example="test@email.com", required=true)
        private String email;
        @ApiModelProperty(value="패스워드", example="*****", required=true)
        private String password;
    }

    @ApiModel(description = "Logout Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AdminLogoutForm extends AdminCommonForm {
        @ApiModelProperty(value="Token", example="token string", required=true)
        private String token;
    }

    @ApiModel(description = "Add App Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AddAppForm extends AdminCommonForm {
        @ApiModelProperty(value="Service Code(Unique)", example="MAPLE_APP", required=true)
        private String scode;
        @ApiModelProperty(value="Token", example="token string", required=true)
        private String token;
        @ApiModelProperty(value="앱 타이틀", example="서비스 앱", required=true)
        private String title;
        @ApiModelProperty(value="앱 설명", example="좋은앱", required=true)
        private String description;
        @ApiModelProperty(value="앱 상태")
        private EAdminAppStatus appStatus;
        @ApiModelProperty(value="FCM Push ID", example="PushID")
        private String fcmId;
        @ApiModelProperty(value="FCM Push Key", example="PushKey")
        private String fcmKey;

        //Optional, 외부 DB 사용시
        @ApiModelProperty(value="External DB 사용유무", required = true)
        private boolean useExternalDb = false;

        @ApiModelProperty(value="DB Host", example="10.10.10.10")
        private String dbHost;
        @ApiModelProperty(value="DB Options", example="zeroDateTimeBehavior=convertToNull&useUnicode=yes&characterEncoding=UTF-8&connectTimeout=2000&autoReconnect=true&serverTimezone=UTC&useSSL=false")
        private String dbOptions;
        @ApiModelProperty(value="User Id", example="accountId")
        private String dbUserId;
        @ApiModelProperty(value="User Password", example="********")
        private String dbPassword;

        public boolean isEmptyExternalDb() {
            return dbHost == null && dbOptions == null && dbUserId == null && dbPassword == null;
        }
    }

    @ApiModel(description = "Delete App Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class DelAppForm extends AdminCommonForm {
        @ApiModelProperty(value="Service Code(Unique)", example="MAPLE_APP")
        private String scode;
        @ApiModelProperty(value="Token", example="token string", required=true)
        private String token;
        @ApiModelProperty(value="패스워드", example="*****", required=true)
        private String password;
    }

    @ApiModel(description = "App List Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AppListForm extends AdminCommonForm {
        @ApiModelProperty(value="Token", example="token string", required=true)
        private String token;
        @ApiModelProperty(value="리스트 시작 옵셋", example="0", required=true)
        private int offset = 0;
        @ApiModelProperty(value="요청 갯수", example="1000", required=true)
        private int count;
        @ApiModelProperty(value="앱 상태")
        private EAdminAppStatus appStatus;
    }

    @ApiModel(description = "Modify App Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class ModifyAppForm extends AddAppForm {
        @ApiModelProperty(value="Service Code(Unique)", example="MAPLE_APP")
        private String scode;
        @ApiModelProperty(value="패스워드", example="*****", required=true)
        private String password;
    }

    @ApiModel(description = "App Count Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class AppCountForm extends AdminCommonForm {
        @ApiModelProperty(value="Token", example="token string", required=true)
        private String token;
        @ApiModelProperty(value="앱 상태", example="all", required=true)
        private EAdminAppStatus appStatus;
    }

    @ApiModel(description = "Update App Form")
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class UpdateAppForm extends ModifyAppForm {
        @ApiModelProperty(value="앱 상태", example="all", required=true)
        private EAdminAppStatus appStatus;
    }
}
