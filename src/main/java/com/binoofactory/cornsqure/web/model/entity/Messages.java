package com.binoofactory.cornsqure.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

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

@Entity(name = "messages")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Messages extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
    private Users user;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "viewed")
    private YN viewed;

    @Column(name = "deleted")
    private YN deleted;


    @Builder
    public Messages(long id, long userId, String title, String contents, YN viewed, YN deleted,
        LocalDateTime createdAt) {

        this.id = id;

        this.title = title;
        this.contents = contents;
        this.viewed = viewed;
        this.deleted = deleted;

    }
}
