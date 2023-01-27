package com.safeapp.admin.web.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;

import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원 응답")
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
    private UserType userType;

    @Schema(description = "user_id")
    private String userId;

    @Schema(description = "user_name")
    private String userName;

    @Schema(description = "image")
    private String image;

    @Schema(description = "sns_allowed")
    private YN snsAllowed;

    @Schema(description = "sns_type")
    SNSType snsType;

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

    /*
    @Schema(description = "user")
    private List<UserAuth> userAuthList = new ArrayList<>();
    */

    @Builder
    public ResponseUsersDTO(Users user) {
        this.id = user.getId();
        this.deleted = user.getDeleted();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phoneNo = user.getPhoneNo();
        this.userType = user.getUserType();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.image = user.getImage();
        this.snsAllowed = user.getSnsAllowed();
        this.snsType = user.getSnsType();
        this.snsValue = user.getSnsValue();
        this.marketingAllowed = user.getMarketingAllowed();
        this.marketingAllowedAt = user.getMarketingAllowedAt();
        this.messageAllowed = user.getMarketingAllowed();
        this.messageAllowedAt = user.getMessageAllowedAt();

        if(user.getCheckListProjectList() != null) {
            this.checkListProjectList = user.getCheckListProjectList();
        }

        /*
        if(user.getUserAuthList() != null) {
            this.userAuthList = user.getUserAuthList();
        }
        */
    }

}