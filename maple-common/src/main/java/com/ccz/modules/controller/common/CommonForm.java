package com.ccz.modules.controller.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "공통 Form")
@Data
public class CommonForm {

//    @ApiModelProperty(value="Service Code", example="APP01")
//    private String scode;

    @ApiModelProperty(value="Command", example="signin", hidden = true)
    private String cmd;


}
