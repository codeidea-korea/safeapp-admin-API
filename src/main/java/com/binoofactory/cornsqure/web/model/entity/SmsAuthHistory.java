package com.binoofactory.cornsqure.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "sms_auth_histories")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SmsAuthHistory extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name = "efected_ended_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime efectedEndedAt;


    @Builder
    public SmsAuthHistory(long id, String phoneNo, String authCode, LocalDateTime efectedEndedAt,
        LocalDateTime createdAt) {
        super();
        this.id = id;
        this.phoneNo = phoneNo;
        this.authCode = authCode;
        this.efectedEndedAt = efectedEndedAt;
    }
}
