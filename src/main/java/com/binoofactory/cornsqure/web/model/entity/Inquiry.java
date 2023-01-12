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

@Entity(name = "inquiries")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "answer")
    private String answer;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @Column(name = "is_answer")
    private YN isAnswer;

    @Column(name = "attachment")
    private String attachment;

    @ManyToOne
    @JoinColumn(name = "answer_user", columnDefinition = "bigint COMMENT '응답유저'")
    private Users answerUser;

    @Builder
    public Inquiry(long id, String serviceName, String title, String contents, String answer, LocalDateTime createdAt,
        YN isAnswer, String attachment, long answerUserId) {
        super();
        this.id = id;
        this.serviceName = serviceName;
        this.title = title;
        this.contents = contents;
        this.answer = answer;
        this.createdAt = createdAt;
        this.isAnswer = isAnswer;
        this.attachment = attachment;
    }
}
