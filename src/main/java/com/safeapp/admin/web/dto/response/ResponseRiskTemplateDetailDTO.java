package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.RiskTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "위험성체크 상세")
public class ResponseRiskTemplateDetailDTO {
    @Schema(description = "상세 ID")
    Long id;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "주소")
    String address;

    @Schema(description = "상세 주소")
    String addressDetail;

    @Schema(description = "도구")
    String tools;

    @Schema(description = "위험팩터타입")
    String riskFactorType;

    @Schema(description = "관련법")
    String relateLaw;

    @Schema(description = "관련가이드")
    String relatedGuide;

    @Schema(description = "위험타입")
    String riskType;

    @Schema(description = "감소응답")
    String reduceReponse;

    @Schema(description = "체크메모")
    String checkMemo;

    @Schema(description = "due유저ID")
    Long dueUserId;

    @Schema(description = "due유저이름")
    String dueUserName;

    @Schema(description = "체크유저ID")
    Long checkUserId;

    @Schema(description = "체크유저이름")
    String checkUserName;

    @Schema(description = "상태")
    String status;

    @Schema(description = "부모순서")
    int parentOrder;

    @Schema(description = "순서")
    int orders;

    @Schema(description = "깊이")
    int depth;

    @Schema(description = "부모깊이")
    int parentDepth;

    @Builder
    public ResponseRiskTemplateDetailDTO(RiskTemplateDetail detail){
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
        if(detail.getDueUser() != null){
            this.dueUserId = detail.getDueUser().getId();
            this.dueUserName = detail.getDueUser().getUserName();
        }
        if(detail.getCheckUser() != null){
            this.checkUserId = detail.getCheckUser().getId();
            this.checkUserName =detail.getCheckUser().getUserName();
        }
        this.parentOrder = detail.getParentOrders();
        this.orders = detail.getOrders();
        this.depth = detail.getDepth();
        this.parentDepth = detail.getParentDepth();
    }
}
