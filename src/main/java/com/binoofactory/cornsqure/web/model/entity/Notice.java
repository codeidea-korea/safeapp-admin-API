package com.binoofactory.cornsqure.web.model.entity;

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

@Entity(name = "notices")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Notice extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "user_id")
    private long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user", insertable = false, updatable = false)
    private Users user;

    @Builder
    public Notice(long id, String type, String title, String contents, LocalDateTime createdAt, long userId) {
        super();
        this.id = id;
        this.type = type;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }
}
