package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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

import static javax.persistence.EnumType.STRING;

@Entity(name = "admins")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "type")
public class Admins extends BaseTimeEntity {

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
    private AdminType type;

    @Column(name = "admin_id")
    private String adminID;

    @Column(name = "admin_name")
    private String adminName;

    public Admins(Admins admins) {
        this.id = admins.id;
        this.deleted = admins.deleted;
        this.email = admins.email;
        this.password = admins.password;
        this.phoneNo = admins.phoneNo;
        this.type = admins.type;
        this.adminID = admins.adminID;
        this.adminName = admins.adminName;
    }

    @Builder
    public Admins(long id, String email, String password, String phoneNo, AdminType type,
                  String adminID, String adminName) {

        this.id = id;
        this.deleted = YN.N;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.type = type;
        this.adminID = adminID;
        this.adminName = adminName;
    }

}