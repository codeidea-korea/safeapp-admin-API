package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin")
    private Admins admin;
    
    @Column(name = "views" )
    @ColumnDefault("0")
    private int views;

    @Column(name = "image")
    private String image;

    @Column(name = "tags")
    private String tags;

    @Column(name = "name")
    private String name;

    @Column(name = "accident_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime accidentAt;

    @Column(name = "accident_uid")
    private String accidentUid;

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
    public AccidentExp(long id, String title, long adminID, Admins admin, int views, String image,
        String tags, String name, LocalDateTime accidentAt, String accidentUid, String accidentReason,
        String accidentCause, String causeDetail, String response, YN createdAtDescended, YN viewsDescended,
        String detailContents) {
        super();
        this.id = id;
        this.title = title;
        this.admin = admin;
        this.views = views;
        this.image = image;
        this.tags = tags;
        this.name = name;
        this.accidentAt = accidentAt;
        this.accidentUid = accidentUid;
        this.accidentReason = accidentReason;
        this.accidentCause = accidentCause;
        this.causeDetail = causeDetail;
        this.response = response;
        this.createdAtDescended = createdAtDescended;
        this.viewsDescended = viewsDescended;
        this.detailContents = detailContents;
    }
}
