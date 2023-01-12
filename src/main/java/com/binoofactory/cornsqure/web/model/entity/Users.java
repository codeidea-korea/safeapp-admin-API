package com.binoofactory.cornsqure.web.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.binoofactory.cornsqure.web.data.SNSType;
import com.binoofactory.cornsqure.web.data.UserType;
import com.binoofactory.cornsqure.web.data.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import static com.binoofactory.cornsqure.web.data.SNSType.NORMAL;
import static javax.persistence.EnumType.STRING;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private UserType type;

    @Column(name = "sns_allowed")
    private YN snsAllowed;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

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

    @Column(name = "image")
    private String image;

    @Column(name = "password")
    private String password;

    @Column(name = "deleted")
    private YN deleted;

    @Enumerated(STRING)
    @Column(name = "sns_type")
    SNSType snsType = NORMAL;

    @Column(name = "sns_value")
    String snsValue;

    // 자식 테이블 맵핑
    @OneToMany(mappedBy = "user")
    private List<AccidentExp> accidentExps = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<ChecklistProject> checklistProjectList = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<UserAuth> userAuthList = new ArrayList<>();
    
    public Users(Users users) {
        this.id = users.id;
        this.type = users.type;
        this.snsAllowed = users.snsAllowed;
        this.userId = users.userId;
        this.phoneNo = users.phoneNo;
        this.userName = users.userName;
        this.email = users.email;
        this.marketingAllowed = users.marketingAllowed;
        this.marketingAllowedAt = users.marketingAllowedAt;
        this.messageAllowed = users.messageAllowed;
        this.messageAllowedAt = users.messageAllowedAt;
        this.image = users.image;
        this.password = users.password;
        this.deleted = users.deleted;
    }

    @Builder
    public Users(long id, UserType type, YN snsAllowed, String userId, String phoneNo, String userName, String email,
        YN marketingAllowed, LocalDateTime marketingAllowedAt, YN messageAllowed, LocalDateTime messageAllowedAt,
        String image, String password) {
        
        this.id = id;
        this.type = type;
        this.snsAllowed = snsAllowed;
        this.userId = userId;
        this.phoneNo = phoneNo;
        this.userName = userName;
        this.email = email;
        this.marketingAllowed = marketingAllowed;
        this.marketingAllowedAt = marketingAllowedAt;
        this.messageAllowed = messageAllowed;
        this.messageAllowedAt = messageAllowedAt;
        this.image = image;
        this.password = password;
        this.deleted = YN.N;
    }
}
