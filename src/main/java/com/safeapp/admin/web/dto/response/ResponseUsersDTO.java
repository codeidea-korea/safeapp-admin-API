package com.safeapp.admin.web.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.safeapp.admin.web.model.entity.BaseTimeEntity;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.EnumType.STRING;

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
    private String userID;

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

    @Schema(description = "")
    private LocalDateTime marketingAllowedAt;

    @Schema(description = "")
    private YN messageAllowed;

    @Schema(description = "")
    private LocalDateTime messageAllowedAt;

    // 자식 테이블 맵핑
    /*
    @OneToMany(mappedBy = "admin")
    private List<AccidentExp> accidentExps = new ArrayList<>();
    */
    @Schema(description = "")
    private List<ChecklistProject> checklistProjectList = new ArrayList<>();
    @Schema(description = "")
    private List<UserAuth> userAuthList = new ArrayList<>();

    @Builder
    public ResponseUsersDTO(Users user) {
        this.userID = user.getUserID();
        this.userName = user.getUserName();
        this.email = user.getEmail();
    }

}