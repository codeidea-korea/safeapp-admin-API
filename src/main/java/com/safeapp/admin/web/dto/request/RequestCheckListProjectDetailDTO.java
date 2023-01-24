package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "체크리스트 상세 요청")
@Data
public class RequestCheckListProjectDetailDTO {

    @Schema(description = "깊이")
    @NotBlank(message = NOT_NULL)
    Integer depth;

    @Schema(description = "제목 여부")
    @NotBlank(message = NOT_NULL)
    YN isDepth;

    @Schema(description = "부모 깊이")
    @NotBlank(message = NOT_NULL)
    Integer parentDepth;

    @Schema(description = "내용")
    @NotBlank(message = NOT_NULL)
    String contents;

    @Schema(description = "순서")
    @NotBlank(message = NOT_NULL)
    Integer orders;

    @Schema(description = "부모 순서")
    @NotBlank(message = NOT_NULL)
    Integer parentOrders;

    @Schema(description = "타입")
    @NotBlank(message = NOT_NULL)
    String types;

}