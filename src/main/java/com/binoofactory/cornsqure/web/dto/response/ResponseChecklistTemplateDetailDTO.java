package com.binoofactory.cornsqure.web.dto.response;

import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectDetail;
import com.binoofactory.cornsqure.web.model.entity.ChecklistTemplateDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "체크리스트 상세")
public class ResponseChecklistTemplateDetailDTO {
    @Schema(description = "고유 아이디")
    Long id;

    @Schema(description = "깊이")
    int depth;

    @Schema(description = "제목여부")
    YN izTitle;

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
    public ResponseChecklistTemplateDetailDTO(ChecklistTemplateDetail detail) {
        this.id = detail.getId();
        this.depth = detail.getDepth();
        this.izTitle = detail.getIzTitle();
        this.parentDepth = detail.getParentDepth();
        this.contents = detail.getContents();
        this.orders = detail.getOrders();
        this.parentOrders = detail.getParentOrders();
        this.types = detail.getTypes();
    }
}
