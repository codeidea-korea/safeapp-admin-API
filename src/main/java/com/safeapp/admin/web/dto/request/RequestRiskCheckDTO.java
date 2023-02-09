package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "위험성 평가표 요청")
@Data
public class RequestRiskCheckDTO {

    @Schema(description = "프로젝트 PK")
    @NotBlank(message = NOT_NULL)
    Long projectId;

    @Schema(description = "유저 PK")
    @NotBlank(message = NOT_NULL)
    Long userId;

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String name;

    @Schema(description = "공개 여부")
    @NotBlank(message = NOT_NULL)
    YN visibled;

    @Schema(description = "태그")
    String tag;

    @Schema(description = "이행 담당자")
    Long dueUserId;

    @Schema(description = "점검 담당자")
    Long checkUserId;

    @Schema(description = "점검자 PK")
    Long checkerId = null;

    @Schema(description = "검토자 1 PK")
    Long reviewer1Id = null;

    @Schema(description = "검토자 2 PK")
    Long reviewer2Id = null;

    @Schema(description = "검토자 3 PK")
    Long reviewer3Id = null;

    @Schema(description = "승인자 PK")
    Long approverId = null;

    @Schema(description = "관련 사고사례")
    String relatedAcidNo;

    @Schema(description = "재검토 사유")
    String recheckReason;

    @Schema(description = "작업 시작일시")
    LocalDateTime workStartAt;

    @Schema(description = "작업 종료일시")
    LocalDateTime workEndAt;

    @Schema(description = "기타 위험성 메모")
    String etcRiskMemo;

    @Schema(description = "상태")
    String status;

    @Schema(description = "작업개요 - 세부공종")
    String instructWork;

    @Schema(description = "작업개요 - 작업공종")
    String instructDetail;

    @Data
    public static class Reviewers {

        Long reviewerId = null;
        Integer index;

    }

}