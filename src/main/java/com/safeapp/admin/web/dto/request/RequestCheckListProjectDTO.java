package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "체크리스트 요청")
@Data
public class RequestCheckListProjectDTO {

    @Schema(description = "프로젝트 PK")
    @NotBlank(message = NOT_NULL)
    Long projectId;

    @Schema(description = "회원 PK")
    @NotBlank(message = NOT_NULL)
    Long userId;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String name;

    @Schema(description = "공개 여부")
    @NotBlank(message = NOT_NULL)
    YN visibled;

    @Schema(description = "태그")
    @NotBlank(message = NOT_NULL)
    String tag;

    @Schema(description = "점검자 ID")
    Long checkerId = null;

    @Schema(description = "검토자 ID")
    Long reviewerId = null;

    @Schema(description = "승인자 ID")
    Long approverId = null;

    @Schema(description = "관련 사고사례")
    String relatedAcidNo;

    @Schema(description = "재검토 사유")
    String recheckReason;

}