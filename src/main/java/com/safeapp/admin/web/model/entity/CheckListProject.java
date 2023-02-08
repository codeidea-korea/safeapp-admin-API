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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListProject extends BaseTimeEntity {

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
    @JoinColumn(name = "checker")
    private Users checker;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer")
    private Users reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approver")
    private Users approver;

    @Column(name = "recheck_reason")
    private String recheckReason;

    @Column(name = "check_at")
    private LocalDateTime checkAt;

    @Column(name = "review_at")
    private LocalDateTime review_at;

    @Column(name = "approve_at")
    private LocalDateTime approve_at;

    @Enumerated(STRING)
    @Column(name = "status")
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

    // 자식 테이블 매핑
    @OneToMany(mappedBy = "checkListProject")
    @JsonManagedReference
    private List<CheckListProjectDetail> checkListProjectDetailList = new ArrayList<>();

    @Builder
    public CheckListProject(Long id, Project project, Users user, String name, YN visibled, String tag, String relatedAcidNo,
            Users checker, Users reviewer, Users approver, List<CheckListProjectDetail> details, String detailContents) {

        super();

        this.id = id;
        this.name = name;
        this.visibled = visibled;
        this.tag = tag;
        this.relatedAcidNo = relatedAcidNo;
        this.project = project;
        this.user = user;
        this.detailContents = detailContents;
        this.checkListProjectDetailList = details;

        if(checker != null) {
            this.checker = checker;
        }
        if(reviewer != null) {
            this.reviewer = reviewer;
        }
        if(approver != null) {
            this.approver = approver;
        }
    }

    public void edit(CheckListProject checkListProject) {
        setApprover(checkListProject.getApprover());
        setCheckAt(checkListProject.getCheckAt());
        setChecker(checkListProject.getChecker());
        setName(checkListProject.getName());
        setProject(checkListProject.getProject());
        setRecheckReason(checkListProject.getRecheckReason());
        setRelatedAcidNo(checkListProject.getRelatedAcidNo());
        setReviewer(checkListProject.getReviewer());
        setTag(checkListProject.getTag());
        setUser(checkListProject.getUser());
        setVisibled(checkListProject.getVisibled());

        if(checkListProject.checkListProjectDetailList.isEmpty() == false) {
            for(CheckListProjectDetail addDetail : checkListProject.checkListProjectDetailList) {
                boolean isInDB = checkListProjectDetailList.equals(addDetail);
                if(isInDB == false) {
                    checkListProjectDetailList.add(addDetail);
                }
            }
            for(CheckListProjectDetail removeDetail : checkListProjectDetailList) {
                boolean isInDB = checkListProject.checkListProjectDetailList.equals(removeDetail);
                if(isInDB == false) {
                    checkListProjectDetailList.remove(removeDetail);
                }
            }
        }
    }

}