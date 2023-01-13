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
@Schema(description = "위험성체크")
public class RequestRiskCheckDTO {

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
    Long checkerId = null;

    @Schema(description = "리뷰자ID")
    Long reviewerId = null;

    @Schema(description = "승인자ID")
    Long approverId = null;


    @Schema(description = "관련번호?(이거 뭔지 좀 알려주세요.)")
    String relatedAcidNo;

    @Schema(description = "재확인사유")
    String recheckReason;

    @Schema(description = "작업시작시간")
    LocalDateTime workStartAt;

    @Schema(description = "작업종료시간")
    LocalDateTime workEndAt;

    @Schema(description = "기타위험성메모")
    String etcRiskMemo;

    @Schema(description = "상태")
    String status;

    @Schema(description = "instructwork?이거 알려주세요.")
    String instructWork;

    @Schema(description = "instructDetail 이것도 알려주세요.")
    String instructDetail;




}
