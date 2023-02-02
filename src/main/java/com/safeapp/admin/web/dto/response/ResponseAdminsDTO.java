package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;

import com.safeapp.admin.web.model.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Schema(description = "관리자 응답")
@Data
public class ResponseAdminsDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "admin_id")
    private String adminId;

    @Schema(description = "email")
    private String email;

    @Schema(description = "admin_name")
    private String adminName;

    @Schema(description = "phone_no")
    private String phoneNo;

    @Schema(description = "memo")
    private String memo;

    @Schema(description = "마케팅 정보 수신 동의 여부", example = "1")
    private YN marketingAllowed;

    @Schema(description = "마케팅 정보 수신 동의 시각")
    private LocalDateTime marketingAllowedAt;

    @Schema(description = "type")
    private AdminType adminType;

    @Schema(description = "deleted")
    private YN deleted;

    @Builder
    public ResponseAdminsDTO(Admins admin) {
        this.id = admin.getId();
        this.adminId = admin.getAdminId();
        this.email = admin.getEmail();
        //this.password = admin.getPassword();
        this.adminName = admin.getAdminName();
        this.phoneNo = admin.getPhoneNo();
        this.marketingAllowed = admin.getMarketingAllowed();
        this.marketingAllowedAt = admin.getMarketingAllowedAt();
        this.memo = admin.getMemo();
        this.adminType = admin.getAdminType();
        this.deleted = admin.getDeleted();
    }

}