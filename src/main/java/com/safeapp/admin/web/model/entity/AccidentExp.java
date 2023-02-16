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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

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

    @Column(name = "accident_uid")
    private String accidentUid;

    @Transient
    private String detailContents;
    
    @Column(name = "views" )
    @ColumnDefault("0")
    private Integer views;

    @Transient
    private YN createdAtDesc;

    @Transient
    private YN viewsDesc;

    @Builder
    public AccidentExp(Long id, LocalDateTime createdAt, String title, Admins admin, String tags, String name, LocalDateTime accidentAt,
            String accidentReason, String accidentCause, String causeDetail, String response, String image, String accidentUid,
            String detailContents, Integer views, YN createdAtDesc, YN viewsDesc) {

        super();

        this.id = id;
        this.createdAt = createdAt;
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
        this.accidentUid = accidentUid;
        this.detailContents = detailContents;
        this.views = views;
        this.createdAtDesc = createdAtDesc;
        this.viewsDesc = viewsDesc;
    }

    public void edit(AccidentExp newAccExp) {
        setCreatedAt(newAccExp.getCreatedAt());
        setTitle(newAccExp.getTitle());
        setAdmin(newAccExp.getAdmin());
        setTags(newAccExp.getTags());
        setName(newAccExp.getName());
        setAccidentAt(newAccExp.getAccidentAt());
        setAccidentReason(newAccExp.getAccidentReason());
        setAccidentCause(newAccExp.getAccidentCause());
        setCauseDetail(newAccExp.getCauseDetail());
        setResponse(newAccExp.getResponse());
        setImage(newAccExp.getImage());
        setCreatedAt(newAccExp.getCreatedAt());
        setViews(newAccExp.getViews());
        setAccidentUid(newAccExp.getAccidentUid());
    }

}