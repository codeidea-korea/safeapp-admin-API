package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "프로젝트 그룹원 수정 요청")
@Data
public class RequestProjectGroupEditDTO {

    @Schema(description = "프로젝트 그룹원 PK", example = "1")
    private Long id;

    @Schema(description = "권한", example = "TEAM_USER")
    private String userAuthType;

    @Schema(description = "삭제여부", example = "false")
    private Boolean deleteYn = false;

}