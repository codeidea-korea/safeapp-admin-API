
package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Description;

import java.time.format.DecimalStyle;

@Schema(description = "위험성 평가표 상세 요청")
@Data
public class RequestRiskCheckDetailDTO {

    @Schema(description = "위험성 평가표 PK")
    Long riskCheckId;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "주소")
    String address;

    @Schema(description = "상세 주소")
    String addressDetail;

    @Schema(description = "도구")
    String tools;

    @Schema(description = "위험 요소 유형")
    String riskFactorType;

    @Schema(description = "관련 법")
    String relatedLaw;

    @Schema(description = "관련 가이드")
    String relatedGuide;

    @Schema(description = "위험 유형")
    String riskType;

    @Schema(description = "감소 응답")
    String reduceResponse;

    @Schema(description = "체크 메모")
    String checkMemo;

    @Schema(description = "이행 담당자 PK")
    Long dueUserId;

    @Schema(description = "점검자 PK")
    Long checkerUserId;

    @Schema(description = "상태")
    String status;

    @Schema(description = "부모 순서")
    int parentOrders;

    @Schema(description = "순서")
    int orders;

    @Schema(description = "깊이")
    int depth;

    @Schema(description = "부모 깊이")
    int parentDepth;

}