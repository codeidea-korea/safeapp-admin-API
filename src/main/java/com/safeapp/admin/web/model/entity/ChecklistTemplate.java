package com.safeapp.admin.web.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;

import lombok.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistTemplate extends BaseTimeEntity {

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

    @Column(name = "visibled")
    private YN visibled;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checker")
    private Users checker;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewer")
    private Users reviewer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "approver")
    private Users approver;

    @Transient
    private YN liked;

    //자식테이블 맵핑
    @OneToMany(mappedBy = "checklistTemplate")
    private List<ChecklistTemplateDetail> details = new ArrayList<>();

    @Builder
    public ChecklistTemplate(Long id, Project project,  Users user, String name,
         String tag, String relatedAcidNo,
         Users checker, Users reviewer,  Users approver,
        List<ChecklistTemplateDetail> details) {
        super();
        this.id = id;
        this.project = project;
        this.user = user;
        this.name = name;
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
        this.details = details;
    }

}