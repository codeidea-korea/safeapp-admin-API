package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_project_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "is_depth")
    private YN isDepth;

    @Column(name = "parent_depth")
    private Integer parentDepth;

    @Column(name = "contents")
    private String contents;

    @Column(name = "orders")
    private Integer orders;

    @Column(name = "parent_orders")
    private Integer parentOrders;

    @Column(name = "types")
    private String types;

    @Column(name = "memo")
    private String memo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checklist_project")
    @JsonManagedReference
    private CheckListProject checkListProject;

    // 자식 테이블 매핑
    @OneToMany(mappedBy = "checkListProjectDetail")
    private List<CheckListProjectResult> checklistProjectResultList = new ArrayList<>();

    @Builder
    public CheckListProjectDetail(Long id, Integer depth, YN isDepth, Integer parentDepth, String contents,
                                  Integer orders, Integer parentOrders, String types) {

        super();

        this.id = id;
        this.depth = depth;
        this.isDepth = isDepth;
        this.parentDepth = parentDepth;
        this.contents = contents;
        this.orders = orders;
        this.parentOrders = parentOrders;
        this.types = types;
    }

    @Override
    public boolean equals(Object object) {
        CheckListProjectDetail chkPrjDet = (CheckListProjectDetail)object;

        if(chkPrjDet.id == this.id) {
            return true;
        }

        return false;
    }

}