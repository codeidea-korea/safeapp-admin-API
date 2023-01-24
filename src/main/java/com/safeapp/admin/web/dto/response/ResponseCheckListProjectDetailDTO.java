package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "체크리스트 상세 응답")
@Data
public class ResponseCheckListProjectDetailDTO {

    @Schema(description = "체크리스트 상세 PK")
    Long id;

    @Schema(description = "깊이")
    Integer depth;

    @Schema(description = "제목 여부")
    YN isDepth;

    @Schema(description = "부모 깊이")
    Integer parentDepth;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "순서")
    Integer orders;

    @Schema(description = "부모 순서")
    Integer parentOrders;

    @Schema(description = "타입")
    String types;

    @Builder
    public ResponseCheckListProjectDetailDTO(CheckListProjectDetail detail) {
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