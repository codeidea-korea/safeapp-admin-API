package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_project_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListProjectDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "is_depth", nullable = false)
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
    private CheckListProject checklistProject;

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

    public void edit(CheckListProjectDetail chkPrjDet) {
        setUpdatedAt(LocalDateTime.now());
        setDepth(chkPrjDet.getDepth());
        setIsDepth(chkPrjDet.getIsDepth());
        setParentDepth(chkPrjDet.getParentDepth());
        setContents(chkPrjDet.getContents());
        setOrders(chkPrjDet.getOrders());
        setParentOrders(chkPrjDet.getParentOrders());
        setTypes(chkPrjDet.getTypes());
    }

}