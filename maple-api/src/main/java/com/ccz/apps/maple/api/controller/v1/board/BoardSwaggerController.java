package com.ccz.apps.maple.api.controller.v1.board;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "MissSaigon Board APIs")
@RequestMapping(value = "/api/v1/misssaigon/board", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BoardSwaggerController {
}
