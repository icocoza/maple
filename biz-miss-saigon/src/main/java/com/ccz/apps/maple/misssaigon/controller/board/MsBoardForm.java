package com.ccz.apps.maple.misssaigon.controller.board;

import com.ccz.modules.controller.common.CommonForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

public class MsBoardForm {

    @ApiModel(description = "Board 아이템 추가 Form")
    @Data
    public class AddBoardForm extends CommonForm {
        @ApiModelProperty(value="Name or Title", example="Name of something", required=true)
        private String title;

        @ApiModelProperty(value="Address", example="Address", required=true)
        private String address;

        @ApiModelProperty(value="Description", example="Description", required=true)
        private String description;

        @ApiModelProperty(value="Convenient Facilities", example="Convenient Facilities", required=true)
        private List<String> facilities;    // ID발급해서 Enum 할당해야 함

        @ApiModelProperty(value="Convenient Comment", example="Convenient Comment", required=true)
        private List<String> convenientComment;    // Convenient Comment

        @ApiModelProperty(value="Operation Guides", example="Operation Guides", required=true)
        private List<String> guides;    // Convenient Comment

        @ApiModelProperty(value="Operation rules", example="Operation rules", required=true)
        private List<String> rules;    // Convenient Rules

        @ApiModelProperty(value="Longitude", example="Longitude", required=true)
        private Double logitude;

        @ApiModelProperty(value="Latitude", example="Latitude", required=true)
        private Double latitude;

        @ApiModelProperty(value="how to find", example="how to find", required=true)
        private String howToFind;   //찾아오는 길

        /*
        이름
주소
소개
편의시설 및 서비스 - 목록
편의시설 및 서비스 - 코멘트
이용안내 - 공지사항
이용안내 - 기본규정
gps
찾아오는 길 --
숙소주변 레져/티켓
후기 - 별점
후기 - 글목록
후기 - 사진
사진 - 소개
사진 - 방
방 - 사진/종류
방 - 인원 수
방 - 가격 / 대실, 숙박

        * */
    }
}
