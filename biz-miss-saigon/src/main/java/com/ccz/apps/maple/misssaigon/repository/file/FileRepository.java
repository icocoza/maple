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

    @Override
    public String getPoolName() {
        return missSaigonConfig.getPoolname();
    }

    @PostConstruct
    public void init() {
        File uploadDir = new File(missSaigonConfig.getUploadPath(missSaigonConfig.getPoolname()));
        if(uploadDir.exists() == false)
            uploadDir.mkdirs();
        File thumbDir = new File(missSaigonConfig.getThumbPath(missSaigonConfig.getPoolname()));
        if(thumbDir.exists() == false)
            thumbDir.mkdirs();
        File cropDir = new File(missSaigonConfig.getCropPath(missSaigonConfig.getPoolname()));
        if(cropDir.exists() == false)
            cropDir.mkdirs();
    }


    public UploadCropRec getCropFile(String boardid) {
        return new UploadCropRec(missSaigonConfig.getPoolname()).getFile(boardid);
    }

}
