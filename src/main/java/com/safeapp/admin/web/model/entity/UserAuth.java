package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "user_auths")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserAuth extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
    private Users user;

    @Column(name = "efective_start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime efectiveStartAt;

    @Column(name = "efective_end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime efectiveEndAt;

    @Column(name = "payment_what")
    private int paymentWhat;

    @Column(name = "price")
    private int price;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @Builder
    public UserAuth(long id, long authId, long userId, LocalDateTime efectiveStartAt, LocalDateTime efectiveEndAt,
        LocalDateTime createdAt, int paymentWhat, int price) {

        this.id = id;

        this.efectiveStartAt = efectiveStartAt;
        this.efectiveEndAt = efectiveEndAt;
        this.createdAt = createdAt;

        this.paymentWhat = paymentWhat;
        this.price = price;
    }
}
