package com.ccz.apps.maple.misssaigon.repository.file;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.modules.common.dbhelper.DbRecord;
import com.ccz.modules.common.repository.CommonRepository;
import com.ccz.modules.repository.db.file.FileCommonRepository;
import com.ccz.modules.repository.db.file.UploadCropRec;
import com.ccz.modules.repository.db.file.UploadFileRec;
import com.ccz.modules.repository.db.user.UserCommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileRepository  extends FileCommonRepository {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    public String getPoolName() {
        return missSaigonConfig.getPoolName();
    }

    @PostConstruct
    public void init() {
        File uploadDir = new File(missSaigonConfig.getUploadPath(missSaigonConfig.getPoolName()));
        if(uploadDir.exists() == false)
            uploadDir.mkdirs();
        File thumbDir = new File(missSaigonConfig.getThumbPath(missSaigonConfig.getPoolName()));
        if(thumbDir.exists() == false)
            thumbDir.mkdirs();
        File cropDir = new File(missSaigonConfig.getCropPath(missSaigonConfig.getPoolName()));
        if(cropDir.exists() == false)
            cropDir.mkdirs();
    }


    public UploadCropRec getCropFile(String boardid) {
        return new UploadCropRec(missSaigonConfig.getPoolName()).getFile(boardid);
    }

}
