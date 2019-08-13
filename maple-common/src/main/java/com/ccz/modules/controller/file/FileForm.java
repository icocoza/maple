package com.ccz.modules.controller.file;

import com.ccz.modules.controller.common.CommonForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


public class FileForm {
    //String fileId, String userName, String fileServer, String fileName, String fileType, long size, String comment
    //String fileId, int width, int height, long size
    //String fileId, String thumbName, int thumbWidth, int thumbHeight
    //String fileId, String boardId, boolean enabled

    @Data
    @ApiModel(description = "Single File Upload Form")
    public class UploadForm extends CommonForm {

        public UploadForm() {
            super.setCmd("uploadFile");
        }

        @ApiModelProperty(value = "업로딩 파일", example = "업로딩 파일", required = true)
        private MultipartFile multipartFile;

        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "파일 설명", example = "이미지 파일입니다.", required = true)
        private String comment;

        @ApiModelProperty(hidden = true)
        private String serverIp;

        @ApiModelProperty(hidden = true)
        private String uploadDir;

        public String getFilename() {
            return multipartFile.getOriginalFilename();
        }
    }

    @Data
    @ApiModel(description = "Multi Files Upload Form")
    public class MultiUploadForm extends CommonForm {
        public MultiUploadForm() {
            super.setCmd("multiUploadFile");
        }
        @ApiModelProperty(value = "업로딩 멀티 파일", example = "업로딩 멀티 파일", required = true)
        private List<MultipartFile> multipartFiles;

        @ApiModelProperty(value = "사용자 이름", example = "홍길동", required = true)
        private String userName;

        @ApiModelProperty(value = "파일 설명", example = "이미지 파일입니다.", required = true)
        private String comment;

        @ApiModelProperty(hidden = true)
        private String serverIp;

        @ApiModelProperty(hidden = true)
        private String uploadDir;

        public List<String> getFilenames() {
            return multipartFiles.stream().map(x -> x.getOriginalFilename()).collect(Collectors.toList());
        }
    }

    @Data
    public class FileResult {
        private String fileName;
        private String fileId;

        public FileResult(String fileName, String fileId) {
            this.fileName = fileName;
            this.fileId = fileId;
        }
    }

}
