package com.ccz.apps.maple.api.controller.v1.file;

import com.ccz.apps.maple.api.serverhandler.MapleServerHandler;
import com.ccz.modules.common.utils.SysUtils;
import com.ccz.modules.controller.file.FileForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@Api(tags = "Maple File APIs")
@RequestMapping(value = "/api/v1/maple/file", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FileSwaggerController {
    private static String UPLOADED_FOLDER = "./upfiles";

    @Autowired
    private MapleServerHandler mapleServerHandler;

    public FileSwaggerController() {
        File file = new File(UPLOADED_FOLDER);
        if( file.exists() == false)
            file.mkdirs();
    }

    @ApiOperation(value = "Single File Upload", notes = "Single File Upload", response = String.class)
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public String singleFileUpload(@ModelAttribute @Validated FileForm.UploadForm form) {
        if (form.getMultipartFile().isEmpty())
            return "Please select a file to upload";

        form.setServerIp(SysUtils.getLinuxIp());
        form.setUploadDir(UPLOADED_FOLDER);
        String result = mapleServerHandler.process(null, form);
        return result;
    }

    @ApiOperation(value = "Multi File Upload", notes = "Multi File Upload", response = String.class)
    @RequestMapping(value = "/multiUpload", method = {RequestMethod.POST})
    public String multiFileUpload(@ModelAttribute @Validated FileForm.MultiUploadForm form) {
        if (form.getFilenames().size() < 1)
            return "Please select a file to upload";

        form.setServerIp(SysUtils.getLinuxIp());
        form.setUploadDir(UPLOADED_FOLDER);
        String result = mapleServerHandler.process(null, form);
        return result;
    }

}
