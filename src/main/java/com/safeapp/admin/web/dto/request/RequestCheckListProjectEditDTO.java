package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "체크리스트 수정 요청")
@Data
public class RequestCheckListProjectEditDTO {

    @Schema(description = "프로젝트 PK")
    @NotBlank(message = NOT_NULL)
    Long projectId;

    /*
    @Schema(description = "관리자 PK")
    @NotBlank(message = NOT_NULL)
    Long adminId;

    @Schema(description = "사용자 PK")
    @NotBlank(message = NOT_NULL)
    Long userId;
    */

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
    @NotBlank(message = NOT_NULL)
    Long checkerId;

    @Schema(description = "검토자 ID")
    @NotBlank(message = NOT_NULL)
    Long reviewerId;

    @Schema(description = "승인자 ID")
    @NotBlank(message = NOT_NULL)
    Long approverId;

    @Schema(description = "관련 사고사례")
    String relatedAcidNo;

    @Schema(description = "재검토 사유")
    String recheckReason;

    List<DetailEditDTO> details = new ArrayList<>();

    @Data
    public static class DetailEditDTO {
        @Schema(description = "체크리스트 상세 PK")
        Long id;

        @Schema(description = "깊이")
        @NotBlank(message = NOT_NULL)
        int depth;

        @Schema(description = "제목 여부")
        @NotBlank(message = NOT_NULL)
        YN isDepth;

        @Schema(description = "부모 깊이")
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

}