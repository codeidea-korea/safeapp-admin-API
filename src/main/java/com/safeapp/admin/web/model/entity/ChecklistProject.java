package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.data.YN;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import static com.safeapp.admin.web.data.StatusType.READY;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_projects")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ChecklistProject extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint COMMENT '체크리스트 프로젝트 고유아이디'")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project", columnDefinition = "bigint COMMENT '프로젝트'")
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
    private Users user;

    @Column(name = "name", columnDefinition = "varchar(255) COMMENT '체크리스트 프로젝트 이름'")
    private String name;

    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name = "visibled")
    private YN visibled;

    @Column(name = "tag")
    private String tag;

    @Column(name = "related_acid_no")
    private String relatedAcidNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checker", columnDefinition = "bigint COMMENT '체크자'")
    private Users checker;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer", columnDefinition = "bigint COMMENT '리뷰자'")
    private Users reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approver", columnDefinition = "bigint COMMENT '승인자'")
    private Users approver;

    @Column(name = "recheck_reason", columnDefinition = "text COMMENT '재검토사유'")
    private String recheckReason;

    @Column(name = "check_at")
    private LocalDateTime checkAt;

    @Column(name = "review_at")
    private LocalDateTime review_at;

    @Column(name = "approve_at")
    private LocalDateTime approve_at;

    @Enumerated(STRING)
    @Column(name = "status", columnDefinition = "varchar(255) COMMENT '상태'")
    private StatusType status = READY;

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

    //자식 테이블 정의

    @OneToMany(mappedBy = "checklistProject")
    @JsonManagedReference
    private List<ChecklistProjectDetail> checklistProjectDetailList = new ArrayList<>();

    public void update(ChecklistProject checklistProject){
        setApprover(checklistProject.getApprover());
        setCheckAt(checklistProject.getCheckAt());
        setChecker(checklistProject.getChecker());
        setName(checklistProject.getName());
        setProject(checklistProject.getProject());
        setRecheckReason(checklistProject.getRecheckReason());
        setRelatedAcidNo(checklistProject.getRelatedAcidNo());
        setReviewer(checklistProject.getReviewer());
        setTag(checklistProject.getTag());
        setUser(checklistProject.getUser());
        setVisibled(checklistProject.getVisibled());

        if(checklistProject.checklistProjectDetailList.isEmpty() == false) {
            for(ChecklistProjectDetail updateDetail : checklistProject.checklistProjectDetailList){
                boolean isInDB = checklistProjectDetailList.equals(updateDetail);
                if(isInDB == false){
                    checklistProjectDetailList.add(updateDetail);
                }
            }
            for (ChecklistProjectDetail dbDetail : checklistProjectDetailList){
                boolean isInUpdate = checklistProject.checklistProjectDetailList.equals(dbDetail);
                if(isInUpdate == false) {
                    checklistProjectDetailList.remove(dbDetail);
                }
            }
        }
    }

    @Builder
    public ChecklistProject(Long id, long projectId, Project project, long userId, Users user, String name,
         int views, int likes, YN visibled, String tag,
        String relatedAcidNo, long checkerId, Users checker, long reviewerId, Users reviewer, long approverId,
        Users approver, String recheckReason, LocalDateTime checkAt, List<ChecklistProjectDetail> details, YN liked,
        YN createdAtDescended, YN viewsDescended, YN likesDescended, String detailContents) {
        super();
        this.id = id;
        this.name = name;
        this.visibled = visibled;
        this.tag = tag;
        this.relatedAcidNo = relatedAcidNo;
        this.project = project;
        this.user = user;
        if(checker != null){
            this.checker = checker;
        }
        if(reviewer != null){
            this.reviewer = reviewer;
        }

        if(approver != null){
            this.approver = approver;
        }
        this.detailContents = detailContents;
        this.checklistProjectDetailList = details;
    }
}
