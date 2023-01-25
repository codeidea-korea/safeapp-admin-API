package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.CheckListTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "체크리스트 템플릿 상세 응답")
@Data
public class ResponseCheckListTemplateDetailDTO {

    @Schema(description = "체크리스트 템플릿 상세 PK")
    Long id;

    @Schema(description = "깊이")
    int depth;

    @Schema(description = "제목 여부")
    YN isDepth;

    @Schema(description = "부모 깊이")
    int parentDepth;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "순서")
    int orders;

    @Schema(description = "부모 순서")
    int parentOrders;

    @Schema(description = "타입")
    String types;

    @Builder
    public ResponseCheckListTemplateDetailDTO(CheckListTemplateDetail detail) {
        this.id = detail.getId();
        this.depth = detail.getDepth();
        this.isDepth = detail.getIsDepth();
        this.parentDepth = detail.getParentDepth();
        this.contents = detail.getContents();
        this.orders = detail.getOrders();
        this.parentOrders = detail.getParentOrders();
        this.types = detail.getTypes();
    }

}