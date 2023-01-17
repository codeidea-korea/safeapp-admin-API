package com.safeapp.admin.web.model.entity;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.EnumType.STRING;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "deleted")
    private YN deleted;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "type")
    private UserType type;

    @Column(name = "user_id")
    private String userID;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "image")
    private String image;

    @Column(name = "sns_allowed")
    private YN snsAllowed;

    @Enumerated(STRING)
    @Column(name = "sns_type")
    SNSType snsType = SNSType.NORMAL;

    @Column(name = "sns_value")
    String snsValue;

    @Column(name = "marketing_allowed")
    private YN marketingAllowed;

    @Column(name = "marketing_allowed_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime marketingAllowedAt;

    @Column(name = "message_allowed")
    private YN messageAllowed;

    @Column(name = "message_allowed_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime messageAllowedAt;

    // 자식 테이블 맵핑
    @OneToMany(mappedBy = "admin")
    private List<AccidentExp> accidentExps = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<ChecklistProject> checklistProjectList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<UserAuth> userAuthList = new ArrayList<>();
    
    public Users(Users users) {
        this.id = users.id;
        this.deleted = users.deleted;
        this.email = users.email;
        this.password = users.password;
        this.phoneNo = users.phoneNo;
        this.type = users.type;
        this.userID = users.userID;
        this.userName = users.userName;
        this.image = users.image;
        this.snsAllowed = users.snsAllowed;
        this.marketingAllowed = users.marketingAllowed;
        this.marketingAllowedAt = users.marketingAllowedAt;
        this.messageAllowed = users.messageAllowed;
        this.messageAllowedAt = users.messageAllowedAt;
    }

    @Builder
    public Users(long id, String email, String password, String phoneNo, UserType type, String userID, String userName, String image,
                 YN snsAllowed, YN marketingAllowed, LocalDateTime marketingAllowedAt, YN messageAllowed, LocalDateTime messageAllowedAt) {
        
        this.id = id;
        this.deleted = YN.N;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.type = type;
        this.userID = userID;
        this.userName = userName;
        this.image = image;
        this.snsAllowed = snsAllowed;
        this.marketingAllowed = marketingAllowed;
        this.marketingAllowedAt = marketingAllowedAt;
        this.messageAllowed = messageAllowed;
        this.messageAllowedAt = messageAllowedAt;
    }

}