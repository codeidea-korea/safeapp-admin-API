package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "accident_exps")
@Data
@NoArgsConstructor
public class AccidentExp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin")
    private Admins admin;

    @Column(name = "tags")
    private String tags;

    @Column(name = "name")
    private String name;

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

    @Column(name = "image")
    private String image;

    @Column(name = "views" )
    @ColumnDefault("0")
    private Integer views;

    @Column(name = "accident_uid")
    private String accidentUid;

    @Transient
    private String detailContents;

    @Transient
    private String keyword;

    @Transient
    private String adminName;

    @Transient
    private String phoneNo;

    @Transient
    private String createdAtStart;

    @Transient
    private String createdAtEnd;

    @Transient
    private YN createdAtDesc;

    @Transient
    private YN viewsDesc;

    @Builder
    public AccidentExp(Long id, String title, Admins admin, String tags, String name, LocalDateTime accidentAt,
            String accidentReason, String accidentCause, String causeDetail, String response, String image, Integer views,
            String accidentUid, String detailContents, String keyword, String adminName, String phoneNo,
            String createdAtStart, String createdAtEnd, YN createdAtDesc, YN viewsDesc) {

        this.id = id;
        this.title = title;
        this.admin = admin;
        this.tags = tags;
        this.name = name;
        this.accidentAt = accidentAt;
        this.accidentReason = accidentReason;
        this.accidentCause = accidentCause;
        this.causeDetail = causeDetail;
        this.response = response;
        this.image = image;
        this.views = views;
        this.accidentUid = accidentUid;
        this.detailContents = detailContents;
        this.keyword = keyword;
        this.adminName = adminName;
        this.phoneNo = phoneNo;
        this.createdAtStart = createdAtStart;
        this.createdAtEnd = createdAtEnd;
        this.createdAtDesc = createdAtDesc;
        this.viewsDesc = viewsDesc;
    }

    public void edit(AccidentExp newAccExp) {
        setUpdatedAt(newAccExp.getUpdatedAt());
        setTitle(newAccExp.getTitle());
        setTags(newAccExp.getTags());
        setName(newAccExp.getName());
        setAccidentAt(newAccExp.getAccidentAt());
        setAccidentReason(newAccExp.getAccidentReason());
        setAccidentCause(newAccExp.getAccidentCause());
        setCauseDetail(newAccExp.getCauseDetail());
        setResponse(newAccExp.getResponse());
        setImage(newAccExp.getImage());
        setAccidentUid(newAccExp.getAccidentUid());
    }

}