package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RiskCheck extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project", columnDefinition = "bigint COMMENT '프로젝트'")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
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
    @JoinColumn(name = "checker", columnDefinition = "bigint COMMENT '체커'")
    private Users checker;

    @Column(name = "check_at")
    private LocalDateTime checkAt;

    @ManyToOne
    @JoinColumn(name = "reviewer", columnDefinition = "bigint COMMENT '리뷰어'")
    private Users reviewer;

    @Column(name = "review_at")
    private  LocalDateTime reviewAt;

    @ManyToOne
    @JoinColumn(name = "approver", columnDefinition = "bigint COMMENT '승인자'")
    private Users approver;

    @Column(name = "approve_at")
    private LocalDateTime approveAt;

    @Column(name = "visibled")
    private YN visibled;

    @Column(name = "instruct_work")
    private String instructWork;

    @Column(name = "instruct_detail")
    private String instructDetail;

    @Enumerated(STRING)
    @Column(name = "status", columnDefinition = "varchar(255) COMMENT '상태'")
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

    @ManyToOne
    @JoinColumn(name = "check_user_id", insertable = false, updatable = false)
    private Users checkUser;

    @Column(name = "recheck_reason")
    private String recheckReason;

    @Transient
    private YN liked;
    
    @Transient
    private YN createdAtDescended;
    
    @Transient
    private YN viewsDescended;
    
    @Transient
    private YN likesDescended;
    
    @Transient
    private String detailContents;

    @OneToMany(mappedBy = "riskCheck")
    private List<RiskCheckDetail> riskCheckDetailList = new ArrayList<>();

    public void update(RiskCheck riskCheck){
        setApprover(riskCheck.getApprover());
        setChecker(riskCheck.getChecker());
        setCheckUser(riskCheck.getCheckUser());
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
        setUser(riskCheck.getUser());
        setReviewer(riskCheck.getReviewer());
    }

    @Builder
    public RiskCheck(Long id, Project project, long userId, Users user, String name, LocalDateTime createdAt,
        LocalDateTime updatedAt, int views, int likes, String tag, String relatedAcidNo, long checkerId, Users checker,
        long reviewerId, Users reviewer, long approverId, Users approver, YN visibled, String instructWork,
        String instructDetail, LocalDateTime workStartAt, LocalDateTime workEndAt, String etcRiskMemo, long dueUserId,
        long checkUserId, Users checkUser, String status, String recheckReason, List<RiskCheckDetail> details, YN liked,
        YN createdAtDescended, YN viewsDescended, YN likesDescended, String detailContents) {
        super();
        this.id = id;
        this.user = user;
        this.name = name;
        this.project = project;

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
        this.recheckReason = recheckReason;
        this.liked = liked;
        this.createdAtDescended = createdAtDescended;
        this.viewsDescended = viewsDescended;
        this.likesDescended = likesDescended;
        this.detailContents = detailContents;
    }
}
