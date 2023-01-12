package com.binoofactory.cornsqure.web.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.ibatis.annotations.Many;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "risk_templates")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RiskTemplate extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project")
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user")
    private Users user;

    @Column(name = "name")
    private String name;

    @Column(name = "views")
    private int views = 0;

    @Column(name = "likes")
    private int likes = 0;

    @Column(name = "tag")
    private String tag;

    @Column(name = "related_acid_no")
    private String relatedAcidNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checker")
    private Users checker;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer")
    private Users reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approver")
    private Users approver;

    @Column(name = "visibled")
    private YN visibled;

    @Column(name = "instruct_work")
    private String instructWork;

    @Column(name = "instruct_detail")
    private String instructDetail;

    @Column(name = "work_start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime workStartAt;

    @Column(name = "work_end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime workEndAt;

    @Column(name = "etc_risk_memo")
    private String etcRiskMemo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "due_user", columnDefinition = "bigint COMMENT 'due유저'")
    private Users dueUser;

    @OneToMany(mappedBy = "riskTemplate")
    private List<RiskTemplateDetail> details = new ArrayList<>();

    @Transient
    private YN liked;

    @Builder
    public RiskTemplate(Long id, Project project, Users user, String name,  int views, int likes, String tag, String relatedAcidNo,  Users checker,
        long reviewerId, Users reviewer,  Users approver, YN visibled, String instructWork,
        String instructDetail, LocalDateTime workStartAt, LocalDateTime workEndAt, String etcRiskMemo,
        Users dueUser, List<RiskTemplateDetail> details) {
        super();
        this.id = id;
        this.project = project;
        this.user = user;
        this.name = name;
        this.views = views;
        this.likes = likes;
        this.tag = tag;
        this.relatedAcidNo = relatedAcidNo;
        if(checker != null){
            this.checker = checker;
        }
        if(reviewer != null){
            this.reviewer = reviewer;
        }
        if(approver != null){
            this.approver = approver;
        }
        this.visibled = visibled;
        this.instructWork = instructWork;
        this.instructDetail = instructDetail;
        this.workStartAt = workStartAt;
        this.workEndAt = workEndAt;
        this.etcRiskMemo = etcRiskMemo;
        this.dueUser = dueUser;
        this.details = details;
    }
}
