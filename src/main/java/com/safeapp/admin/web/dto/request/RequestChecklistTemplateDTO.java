package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema(description = "체크리스트 템플릿")
public class RequestChecklistTemplateDTO {
    @Schema(description = "프로젝트 ID")
    @NotBlank(message = NOT_NULL)
    Long projectId;

    @Schema(description = "유저ID")
    @NotBlank(message = NOT_NULL)
    Long userId;

    @Schema(description = "체크리스트 이름")
    @NotBlank(message = NOT_NULL)
    String name;

    @Schema(description = "전체공개여부")
    @NotBlank(message = NOT_NULL)
    YN visibled;

    @Schema(description = "태그")
    @NotBlank(message = NOT_NULL)
    String tag;

    @Schema(description = "체커ID")
    Long checkerId;

    @Schema(description = "리뷰자ID")
    Long reviewerId;

    @Schema(description = "승인자ID")
    Long approverId;

    @Schema(description = "체크시간")
    LocalDateTime checkAt;

    @Schema(description = "관련번호?(이거 뭔지 좀 알려주세요.)")
    String relatedAcidNo;

    @Schema(description = "재확인사유")
    String recheckReason;


}
