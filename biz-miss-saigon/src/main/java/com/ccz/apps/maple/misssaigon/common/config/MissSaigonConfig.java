package com.ccz.apps.maple.misssaigon.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Getter
@Configuration
public class MissSaigonConfig {

    //라이브러리 자체 yml을 로딩하는 방법을 잘 모르겠음. 추후 찾아보고 보강합시다.
    //@Value("${mysql.url}")
    private String mysqlUrl = "jdbc:mysql://45.76.220.83/misssaigon?zeroDateTimeBehavior=convertToNull&useUnicode=yes&characterEncoding=UTF-8&connectTimeout=3000&autoReconnect=true&serverTimezone=UTC&useSSL=false";

    //@Value("${mysql.username}")
    private String mysqlUsername = "misssaigon";

    //@Value("${mysql.password}")
    private String mysqlPassword = "miss.saigon@pw";

    //@Value("${mysql.poolname}")
    private String poolname = "misssaigon";

    private String uploadDir = "./upfiles";

    private float thumbSize = 480;
    private String thumbDir = "thumb";
    private String cropDir = "crop";

    public String getUploadPath(String scode) {
        return Paths.get(uploadDir, scode).toString();
    }

    public String getThumbPath(String scode) {
        return Paths.get(uploadDir, scode, thumbDir).toString();
    }

    public String getCropPath(String scode) {
        return Paths.get(uploadDir, scode, cropDir).toString();
    }

}
