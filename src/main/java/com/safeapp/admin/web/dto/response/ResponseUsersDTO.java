package com.safeapp.admin.web.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;

import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.UserAuth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원")
@Data
public class ResponseUsersDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "deleted")
    private YN deleted;

    @Schema(description = "email")
    private String email;

    @Schema(description = "password")
    private String password;

    @Schema(description = "phone_no")
    private String phoneNo;

    @Schema(description = "type")
    private UserType type;

    @Schema(description = "user_id")
    private String userId;

    @Schema(description = "user_name")
    private String userName;

    @Schema(description = "image")
    private String image;

    @Schema(description = "sns_allowed")
    private YN snsAllowed;

    @Schema(description = "sns_type")
    SNSType snsType = SNSType.NORMAL;

    @Schema(description = "sns_value")
    String snsValue;

    @Schema(description = "marketing_allowed")
    private YN marketingAllowed;

    @Schema(description = "marketing_allowed_at")
    private LocalDateTime marketingAllowedAt;

    @Schema(description = "message_allowed")
    private YN messageAllowed;

    @Schema(description = "message_allowed_at")
    private LocalDateTime messageAllowedAt;

    // 자식 테이블 맵핑
    @Schema(description = "user")
    private List<CheckListProject> checkListProjectList = new ArrayList<>();

    @Schema(description = "user")
    private List<UserAuth> userAuthList = new ArrayList<>();

}