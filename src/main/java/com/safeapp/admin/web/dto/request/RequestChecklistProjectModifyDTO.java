package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema(description = "체크리스트 수정")
public class RequestChecklistProjectModifyDTO {

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
    @NotBlank(message = NOT_NULL)
    Long checkerId;

    @Schema(description = "리뷰자ID")
    @NotBlank(message = NOT_NULL)
    Long reviewerId;

    @Schema(description = "승인자ID")
    @NotBlank(message = NOT_NULL)
    Long approverId;

    @Schema(description = "체크시간")
    LocalDateTime checkAt;

    @Schema(description = "관련번호?(이거 뭔지 좀 알려주세요.)")
    String relatedAcidNo;

    @Schema(description = "재확인사유")
    String recheckReason;

    List<DetailModifyDTO> details = new ArrayList<>();

    @Getter
    @Setter
    public static class DetailModifyDTO {

        @Schema(description = "디테일ID")
        Long id;

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

}
