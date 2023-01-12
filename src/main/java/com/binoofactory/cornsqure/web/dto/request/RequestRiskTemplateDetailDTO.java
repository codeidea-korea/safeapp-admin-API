
package com.binoofactory.cornsqure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "위험성체크 상세")
public class RequestRiskTemplateDetailDTO {

    @Schema(description = "위험체크템플릿 본문 ID")
    Long riskTemplateId;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "주소")
    String address;

    @Schema(description = "상세 주소")
    String addressDetail;

    @Schema(description = "도구")
    String tools;

    @Schema(description = "위험요소타입")
    String riskFactorType;

    @Schema(description = "관련법")
    String relatedLaw;

    @Schema(description = "관련가이드")
    String relatedGuide;

    @Schema(description = "위험타입")
    String riskType;

    @Schema(description = "감소율?감소응답?")
    String reduceResponse;

    @Schema(description = "체크메모?")
    String checkMemo;

    @Schema(description = "due유저ID")
    Long dueUserId;

    @Schema(description = "체크 유저 ID")
    Long checkerUserId;

    @Schema(description = "상태")
    String status;

    @Schema(description = "부모 순서")
    int parentOrders;

    @Schema(description = "순서")
    int orders;

    @Schema(description = "깊이")
    int depth;

    @Schema(description = "부모순서")
    int parentDepth;
}
