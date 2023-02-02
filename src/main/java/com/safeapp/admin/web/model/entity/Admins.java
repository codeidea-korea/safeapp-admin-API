package com.safeapp.admin.web.model.entity;

import javax.persistence.*;
import javax.xml.soap.Text;

import com.safeapp.admin.web.data.AdminType;
import com.safeapp.admin.web.data.SNSType;
import com.safeapp.admin.web.data.UserType;
import com.safeapp.admin.web.data.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Entity(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admins extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "admin_id")
    private String adminId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "memo")
    private String memo;

    @Column(name = "logged_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime loggedAt;

    @Column(name = "marketing_allowed")
    private YN marketingAllowed;

    @Column(name = "marketing_allowed_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime marketingAllowedAt;

    @Column(name = "type")
    private AdminType adminType;

    @Column(name = "deleted")
    private YN deleted;

    public Admins(Admins admins) {
        this.id = admins.id;
        this.adminId = admins.adminId;
        this.email = admins.email;
        this.password = admins.password;
        this.adminName = admins.adminName;
        this.phoneNo = admins.phoneNo;
        this.loggedAt = admins.loggedAt;
        this.marketingAllowed = admins.marketingAllowed;
        this.marketingAllowedAt = admins.marketingAllowedAt;
        this.memo = admins.memo;
        this.adminType = admins.adminType;
        this.deleted = admins.deleted;
    }

    @Builder
    public Admins(long id, String adminId, String email, String password, String adminName, String phoneNo,
            LocalDateTime loggedAt, YN marketingAllowed, LocalDateTime marketingAllowedAt, String memo, AdminType adminType) {

        this.id = id;
        this.adminId = adminId;
        this.email = email;
        this.password = password;
        this.adminName = adminName;
        this.loggedAt = loggedAt;
        this.marketingAllowed = marketingAllowed;
        this.marketingAllowedAt = marketingAllowedAt;
        this.phoneNo = phoneNo;
        this.memo = memo;
        this.adminType = adminType;
        this.deleted = YN.N;
    }

    public void edit(Admins admin) {
        setAdminId(admin.getAdminId());
        setEmail(admin.getEmail());
        setAdminName(admin.getAdminName());
        setPhoneNo(admin.getPhoneNo());
        setMemo(admin.getMemo());
    }

}