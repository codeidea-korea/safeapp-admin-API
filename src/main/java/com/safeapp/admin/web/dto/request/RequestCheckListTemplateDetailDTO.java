package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema(description = "체크리스트 템플릿 상세")
public class RequestCheckListTemplateDetailDTO {

    @Schema(description = "깊이")
    @NotBlank(message = NOT_NULL)
    int depth;

    @Schema(description = "제목여부")
    @NotBlank(message = NOT_NULL)
    YN izTitle;

    @Schema(description = "부모깊이")
    @NotBlank(message = NOT_NULL)
    int parentDepth;

    @Schema(description = "내용")
    @NotBlank(message = NOT_NULL)
    String contents;

    @Schema(description = "순서")
    @NotBlank(message = NOT_NULL)
    int orders;

    @Schema(description = "부모 순서")
    @NotBlank(message = NOT_NULL)
    int parentOrders;

    @Schema(description = "타입")
    @NotBlank(message = NOT_NULL)
    String types;
}
