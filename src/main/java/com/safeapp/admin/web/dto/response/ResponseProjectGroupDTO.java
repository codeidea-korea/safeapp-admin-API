package com.safeapp.admin.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "프로젝트 그룹원 응답")
@Data
public class ResponseProjectGroupDTO {

    @Schema(description = "회원 PK")
    Long id;

    @Schema(description = "이름")
    String userName;

    @Schema(description = "이메일")
    String email;

    @Schema(description = "권한")
    String userAuthType;

}