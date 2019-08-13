package com.ccz.apps.maple.misssaigon.controller.file;

import com.ccz.apps.maple.misssaigon.repository.file.FileRepository;
import com.ccz.modules.controller.file.FileCommandCommonAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FileCommandAction extends FileCommandCommonAction {

    @Autowired
    FileRepository fileRepository;

    public FileCommandAction() {

    }

    @PostConstruct
    public void init() {
        super.initCommandFunctions(fileRepository);
    }
}
