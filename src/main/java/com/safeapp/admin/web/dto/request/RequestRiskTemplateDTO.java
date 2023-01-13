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
@Schema(description = "위험성체크 템플릿")
public class RequestRiskTemplateDTO {

    @Schema(description = "프로젝트 ID")
    @NotBlank(message = NOT_NULL)
    Long projectId;

    @Schema(description = "유저ID")
    @NotBlank(message = NOT_NULL)
    Long userId;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String name;

    @Schema(description = "전체공개여부")
    @NotBlank(message = NOT_NULL)
    YN visibled;

    @Schema(description = "태그")
    @NotBlank(message = NOT_NULL)
    String tag;

    @Schema(description = "체커ID")
    @NotBlank(message = NOT_NULL)
    Long checkerId;

    @Schema(description = "체크시간")
    LocalDateTime checkAt;

    @Schema(description = "관련번호?(이거 뭔지 좀 알려주세요.)")
    String relatedAcidNo;

    @Schema(description = "작업시작시간")
    LocalDateTime workStartAt;

    @Schema(description = "작업종료시간")
    LocalDateTime workEndAt;

    @Schema(description = "기타위험성메모")
    String etcRiskMemo;

    @Schema(description = "검토자 ID")
    Long reviewerId;

    @Schema(description = "승인자ID")
    Long approverId;

    @Schema(description = "상태")
    String status;

    @Schema(description = "dueuser")
    Long dueUserId;

    @Schema(description = "instructwork?이거 알려주세요.")
    String instructWork;

    @Schema(description = "instructDetail 이것도 알려주세요.")
    String instructDetail;

}
