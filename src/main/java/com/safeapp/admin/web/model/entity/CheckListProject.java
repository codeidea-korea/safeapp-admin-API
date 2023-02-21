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

    @Column(name = "check_at")
    private LocalDateTime checkAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer")
    private Users reviewer;

    @Column(name = "review_at")
    private LocalDateTime reviewAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approver")
    private Users approver;

    @Column(name = "approve_at")
    private LocalDateTime approveAt;

    @Column(name = "recheck_reason")
    private String recheckReason;

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
            Users checker, Users reviewer, Users approver, List<CheckListProjectDetail> checkListProjectDetailList, String detailContents) {

        super();

        this.id = id;
        this.project = project;
        this.user = user;
        this.name = name;
        this.visibled = visibled;
        this.tag = tag;
        this.relatedAcidNo = relatedAcidNo;
        this.checkListProjectDetailList = checkListProjectDetailList;
        this.detailContents = detailContents;

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

    public void edit(CheckListProject newChkPrj) {
        setProject(newChkPrj.getProject());
        setUser(newChkPrj.getUser());
        setName(newChkPrj.getName());
        setVisibled(newChkPrj.getVisibled());
        setTag(newChkPrj.getTag());
        setRelatedAcidNo(newChkPrj.getRelatedAcidNo());
        setChecker(newChkPrj.getChecker());
        setCheckAt(newChkPrj.getCheckAt());
        setReviewer(newChkPrj.getReviewer());
        setApprover(newChkPrj.getApprover());
        setRecheckReason(newChkPrj.getRecheckReason());

        if(newChkPrj.checkListProjectDetailList.isEmpty() == false) {
            for(CheckListProjectDetail addDetail : newChkPrj.checkListProjectDetailList) {
                boolean isInDB = checkListProjectDetailList.equals(addDetail);
                if(isInDB == false) {
                    checkListProjectDetailList.add(addDetail);
                }
            }
            for(CheckListProjectDetail removeDetail : checkListProjectDetailList) {
                boolean isInDB = newChkPrj.checkListProjectDetailList.equals(removeDetail);
                if(isInDB == false) {
                    checkListProjectDetailList.remove(removeDetail);
                }
            }
        }
    }

}