package com.ccz.apps.maple.misssaigon.controller.board;

import com.ccz.apps.maple.misssaigon.repository.board.BoardRepository;
import com.ccz.apps.maple.misssaigon.repository.file.FileRepository;
import com.ccz.modules.controller.file.FileCommandCommonAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BoardCommandAction extends FileCommandCommonAction {

    @Autowired
    BoardRepository boardRepository;

    public BoardCommandAction() {

    }

    @PostConstruct
    public void init() {
        super.initCommandFunctions(boardRepository);
    }
}
