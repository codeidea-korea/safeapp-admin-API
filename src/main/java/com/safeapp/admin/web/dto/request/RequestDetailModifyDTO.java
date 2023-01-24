package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RequestDetailModifyDTO {

    @Schema(description = "고유 아이디")
    Long id;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "주소")
    String address;

    @Schema(description = "상세주소")
    String addressDetail;

        @Schema(description = "도구")
    String tools;
        @Schema(description = "위험요소타입")
    String riskFactorType;

        @Schema(description = "관련법")
    String relateLaw;

        @Schema(description = "관련가이드")
    String relateGuide;

        @Schema(description = "위험타입")
    String riskType;

        @Schema(description = "반응감소")
    String reduceResponse;

        @Schema(description = "체크메모")
    String checkMemo;

        @Schema(description = "상태")
        String status;

        @Schema(description = "부모순서")
        int parentOrders;

        @Schema(description = "순서")
        int orders;

        @Schema(description = "깊이")
        int depth;

        @Schema(description = "메모")
        String memo;

        @Schema(description = "부모깊이")
        int parentDepth;

        @Schema(description = "제목여부")
        YN izTitle;

        @Schema(description = "타입")
        String types;

}