package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.safeapp.admin.web.data.StatusType;
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

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "risk_checks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskCheck extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project")
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin")
    private Admins admin;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    @Column(name = "name")
    private String name;

    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name = "tag")
    private String tag;

    @Column(name = "related_acid_no")
    private String relatedAcidNo;

    @ManyToOne
    @JoinColumn(name = "due_user")
    private Users dueUser;

    @ManyToOne
    @JoinColumn(name = "check_user")
    private Users checkUser;

    @ManyToOne
    @JoinColumn(name = "checker")
    private Users checker;

    @Column(name = "check_at")
    private LocalDateTime checkAt;

    @ManyToOne
    @JoinColumn(name = "approver")
    private Users approver;

    @Column(name = "approve_at")
    private LocalDateTime approveAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer1")
    private Users reviewer1;

    @Column(name = "review1_at")
    private LocalDateTime review1_at;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer2")
    private Users reviewer2;

    @Column(name = "review2_at")
    private LocalDateTime review2_at;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer3")
    private Users reviewer3;

    @Column(name = "review3_at")
    private LocalDateTime review3_at;

    @Column(name = "visibled")
    private YN visibled;

    @Column(name = "instruct_work")
    private String instructWork;

    @Column(name = "instruct_detail")
    private String instructDetail;

    @Enumerated(STRING)
    @Column(name = "status")
    private StatusType status = StatusType.READY;

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

    @Column(name = "recheck_reason")
    private String recheckReason;
    
    @Transient
    private String detailContents;

    @Transient
    private String keyword;

    @Transient
    private String userName;

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

    @OneToMany(mappedBy = "riskCheck")
    private List<RiskCheckDetail> riskCheckDetailList = new ArrayList<>();

    @Builder
    public RiskCheck(Long id, Project project, Admins admin, Users user, String name, Integer views, Integer likes, String tag, String relatedAcidNo,
            Users checker, Users approver, YN visibled, String instructWork, Users reviewer1, Users reviewer2, Users reviewer3,
            String instructDetail, LocalDateTime workStartAt, LocalDateTime workEndAt, String etcRiskMemo,
            String detailContents, String keyword, String userName, String phoneNo, String createdAtStart,
            String createdAtEnd, YN createdAtDesc, YN viewsDesc) {

        super();

        this.id = id;
        if(!Objects.isNull(admin)) {
            this.admin = admin;
        }
        if(!Objects.isNull(user)) {
            this.user = user;
        }
        this.name = name;
        this.project = project;

        this.views = views;
        this.likes = likes;
        this.tag = tag;
        this.relatedAcidNo = relatedAcidNo;

        this.visibled = visibled;
        this.instructWork = instructWork;
        this.instructDetail = instructDetail;
        this.workStartAt = workStartAt;
        this.workEndAt = workEndAt;
        this.etcRiskMemo = etcRiskMemo;
        this.detailContents = detailContents;
        this.keyword = keyword;
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.createdAtStart = createdAtStart;
        this.createdAtEnd = createdAtEnd;
        this.createdAtDesc = createdAtDesc;
        this.viewsDesc = viewsDesc;

        if(checker != null) {
            this.checker = checker;
        }
        if(approver != null) {
            this.approver = approver;
        }
        if(reviewer1 != null) {
            this.reviewer1 = reviewer1;
        }
        if(reviewer2 != null) {
            this.reviewer2 = reviewer2;
        }
        if(reviewer3 != null) {
            this.reviewer3 = reviewer3;
        }
    }

    public void edit(RiskCheck riskCheck) {
        setApprover(riskCheck.getApprover());
        setChecker(riskCheck.getChecker());
        setEtcRiskMemo(riskCheck.getEtcRiskMemo());
        setInstructDetail(riskCheck.getInstructDetail());
        setInstructWork(riskCheck.getInstructWork());
        setName(riskCheck.getName());
        setProject(riskCheck.getProject());
        setRecheckReason(riskCheck.getRecheckReason());
        setRelatedAcidNo(riskCheck.getRelatedAcidNo());
        setStatus(riskCheck.getStatus());
        setTag(riskCheck.getTag());
        setVisibled(riskCheck.getVisibled());
        setWorkEndAt(riskCheck.getWorkEndAt());
        setWorkStartAt(riskCheck.getWorkStartAt());
    }

}