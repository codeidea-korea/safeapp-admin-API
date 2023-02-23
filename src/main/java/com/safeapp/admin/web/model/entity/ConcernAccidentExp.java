package com.safeapp.admin.web.model.entity;

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

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "concern_accident_exps")
@Data
@NoArgsConstructor
public class ConcernAccidentExp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin")
    private Admins admin;

    @Column(name = "title")
    private String title;

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

    @Column(name = "cause_detail")
    private String causeDetail;

    @Column(name = "accident_reason")
    private String accidentReason;

    @Column(name = "response")
    private String response;

    @Column(name = "image")
    private String image;

    @Column(name = "views")
    @ColumnDefault("0")
    private Integer views;

    @Column(name = "accident_cause")
    private String accidentCause;

    @Column(name = "accident_at")
    private LocalDateTime accidentAt;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user")
    private Users user;

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
    public ConcernAccidentExp(Long id, String title, Admins admin, String tags, String name, String accidentUserName,
            String accidentType, String accidentPlace, String causeDetail, String accidentCause, String response,
            String image, Integer views, String detailContents, String keyword, String adminName, String phoneNo,
            String createdAtStart, String createdAtEnd, YN createdAtDesc, YN viewsDesc) {

        this.id = id;
        this.admin = admin;
        this.title = title;
        this.tags = tags;
        this.name = name;
        this.accidentUserName = accidentUserName;
        this.accidentType = accidentType;
        this.accidentPlace = accidentPlace;
        this.causeDetail = causeDetail;
        this.accidentCause = accidentCause;
        this.response = response;
        this.image = image;
        this.views = views;
        this.detailContents = detailContents;
        this.keyword = keyword;
        this.adminName = adminName;
        this.phoneNo = phoneNo;
        this.createdAtStart = createdAtStart;
        this.createdAtEnd = createdAtEnd;
        this.createdAtDesc = createdAtDesc;
        this.viewsDesc = viewsDesc;
    }

    public void edit(ConcernAccidentExp newConExp) {
        setUpdatedAt(newConExp.getUpdatedAt());
        setTitle(newConExp.getTitle());
        setTags(newConExp.getTags());
        setName(newConExp.getName());
        setAccidentUserName(newConExp.getAccidentUserName());
        setAccidentType(newConExp.getAccidentType());
        setAccidentPlace(newConExp.getAccidentPlace());
        setCauseDetail(newConExp.getCauseDetail());
        setAccidentCause(newConExp.getAccidentCause());
        setResponse(newConExp.getResponse());
        setImage(newConExp.getImage());
    }

}