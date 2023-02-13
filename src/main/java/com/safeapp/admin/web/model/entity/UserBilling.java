package com.safeapp.admin.web.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.safeapp.admin.web.data.YN;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Entity(name = "user_billing")
@Data
@NoArgsConstructor
public class UserBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Enumerated(STRING)
    @Column(name = "use_yn")
    private YN useYN;

    @Column(name = "payment_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime paymentAt;

    @Builder
    public UserBilling(Long id, Long userId, Long paymentId, YN useYN, LocalDateTime paymentAt) {
        this.id = id;
        this.userId = userId;
        this.paymentId = paymentId;
        this.useYN = useYN;
        this.paymentAt = paymentAt;
    }

}