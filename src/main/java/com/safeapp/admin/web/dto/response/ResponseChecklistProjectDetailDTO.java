package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "체크리스트 상세")
public class ResponseChecklistProjectDetailDTO {
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
    public ResponseChecklistProjectDetailDTO(ChecklistProjectDetail detail) {
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
