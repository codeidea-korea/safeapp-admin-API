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

import static javax.persistence.FetchType.LAZY;

@Entity(name = "concern_accident_exps")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ConcernAccidentExp extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;
    
    @Column(name = "user_id")
    private long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
    private Users user;

    @Column(name = "views")
    private int views;

    @Column(name = "image")
    private String image;

    @Column(name = "tags")
    private String tags;

    @Column(name = "name")
    private String name;

    @Column(name = "accident_user_name")
    private String accidentUserName;

    @Column(name = "accident_type")
    private String accidentType;

    @Column(name = "accident_place")
    private String accidentPlace;

    @Column(name = "accident_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime accidentAt;

    @Column(name = "accident_reason")
    private String accidentReason;

    @Column(name = "accident_cause")
    private String accidentCause;

    @Column(name = "cause_detail")
    private String causeDetail;

    @Column(name = "response")
    private String response;
    
    @Transient
    private YN createdAtDescended;
    
    @Transient
    private YN viewsDescended;
    
    @Transient
    private String detailContents;

    @Builder
    public ConcernAccidentExp(long id, String title, long userId, Users user, LocalDateTime createdAt, int views,
        String image, String tags, String name, String accidentUserName, String accidentType, LocalDateTime accidentAt,
        String accidentReason, String accidentCause, String causeDetail, String response, YN createdAtDescended,
        YN viewsDescended, String detailContents) {
        super();
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.user = user;

        this.views = views;
        this.image = image;
        this.tags = tags;
        this.name = name;
        this.accidentUserName = accidentUserName;
        this.accidentType = accidentType;
        this.accidentAt = accidentAt;
        this.accidentReason = accidentReason;
        this.accidentCause = accidentCause;
        this.causeDetail = causeDetail;
        this.response = response;
        this.createdAtDescended = createdAtDescended;
        this.viewsDescended = viewsDescended;
        this.detailContents = detailContents;
    }
}
