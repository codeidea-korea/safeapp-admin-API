package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.RiskCheckDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "위험성 평가표 상세 응답")
@Data
public class ResponseRiskCheckDetailDTO {

    @Schema(description = "위험성 평가표 상세 PK")
    Long id;

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
    String relateLaw;

    @Schema(description = "관련 가이드")
    String relatedGuide;

    @Schema(description = "위험 유형")
    String riskType;

    @Schema(description = "감소 응답")
    String reduceReponse;

    @Schema(description = "체크 메모")
    String checkMemo;

    @Schema(description = "이행 담당자 PK")
    Long dueUserId;

    @Schema(description = "이행 담당자 이름")
    String dueUserName;

    @Schema(description = "점검자 PK")
    Long checkUserId;

    @Schema(description = "점검자 이름")
    String checkUserName;

    @Schema(description = "상태")
    String status;

    @Schema(description = "부모 순서")
    int parentOrder;

    @Schema(description = "순서")
    int orders;

    @Schema(description = "깊이")
    int depth;

    @Schema(description = "부모 깊이")
    int parentDepth;

    @Builder
    public ResponseRiskCheckDetailDTO(RiskCheckDetail detail) {
        this.id = detail.getId();
        this.contents = detail.getContents();
        this.address = detail.getAddress();
        this.addressDetail = detail.getAddressDetail();
        this.tools = detail.getTools();
        this.riskFactorType = detail.getRiskFactorType();
        this.relateLaw = detail.getRelateLaw();
        this.relatedGuide = detail.getRelateGuide();
        this.riskType = detail.getRiskType();
        this.reduceReponse = detail.getReduceResponse();
        this.checkMemo = detail.getCheckMemo();

        this.status = detail.getStatus();
        this.parentOrder = detail.getParentOrders();
        this.depth = detail.getDepth();
        this.orders = detail.getOrders();
        this.parentDepth = detail.getParentDepth();

        if(detail.getDueUser() != null) {
            this.dueUserId = detail.getDueUser().getId();
            this.dueUserName = detail.getDueUser().getUserName();
        }
        if(detail.getCheckUser() != null) {
            this.checkUserId = detail.getCheckUser().getId();
            this.checkUserName =detail.getCheckUser().getUserName();
        }
    }

}