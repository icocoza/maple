package com.ccz.modules.controller.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "공통 Form")
@Data
public class CommonForm {

    @ApiModelProperty(value="Service Code", example="misssaigon", required=true)
    private String scode;
    @ApiModelProperty(value="Command", example="signin", required=true)
    private String cmd;

}
