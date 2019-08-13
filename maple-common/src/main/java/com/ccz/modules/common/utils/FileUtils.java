package com.ccz.modules.common.utils;

import java.io.File;

public class FileUtils {
    public static String getFileName(String filename) {
        int slash = filename.lastIndexOf('/');
        if(slash<0)
            slash = 0;
        int dot = filename.lastIndexOf('.');
        if (dot > 0)
            return filename.substring(slash+1, dot);
        else if(slash>=0)
            return filename.substring(slash);
        return filename;
    }

    public static String getFileExt(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0)
            extension = filename.substring(i+1);
        return extension;
    }

    public String getFileExt(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getUrlExt(String url) {
        int i = url.lastIndexOf('/');
        if(i<0)
            return "";
        String ext = getFileExt(url.substring(i+1));
        if(ext.contains("?")) {
            return ext.substring(0, ext.indexOf("?"));
        }
        return ext;
    }

}
