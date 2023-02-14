package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "멤버쉽 결제 응답")
@Data
public class ResponseMembershipDTO {

    @Schema(description = "멤버쉽 결제 PK")
    private Long id;

    @Schema(description = "멤버쉽 기간 시작일시")
    private LocalDateTime efectiveStartAt;

    @Schema(description = "멤버쉽 기간 종료일시")
    private LocalDateTime efectiveEndAt;

    @Schema(description = "메모")
    private String memo;

    @Builder
    public ResponseMembershipDTO(UserAuth userAuth) {
        this.id = userAuth.getId();
        this.efectiveStartAt = userAuth.getEfectiveStartAt();
        this.efectiveEndAt = userAuth.getEfectiveEndAt();
        this.memo = userAuth.getMemo();
    }

}