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
@Data
@NoArgsConstructor
public class UserAuth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user")
    private Long user;

    /*
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user")
    private Users user;
    */

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

    @Column(name = "efective_stop_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime efectiveStopAt;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "status")
    private String status;

    @Column(name = "add_month")
    private Integer addMonth;

    @Column(name = "member_cnt")
    private Integer memberCnt;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "memo")
    private String memo;

    @Builder
    public UserAuth(Long id, Long user, LocalDateTime efectiveStartAt, LocalDateTime efectiveEndAt, LocalDateTime efectiveStopAt,
            Long price, String orderType, String status, Integer addMonth, Integer memberCnt, Long paymentId) {

        super();

        this.id = id;
        this.user = user;
        this.efectiveStartAt = efectiveStartAt;
        this.efectiveEndAt = efectiveEndAt;
        this.efectiveStopAt = efectiveStopAt;
        this.orderType = orderType;
        this.status = status;
        this.addMonth = addMonth;
        this.memberCnt = memberCnt;
        this.paymentId = paymentId;
    }

}