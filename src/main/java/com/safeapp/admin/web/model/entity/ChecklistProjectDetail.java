package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_project_details")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ChecklistProjectDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "depth")
    private int depth;

    @Column(name = "is_depth", nullable = false)
    private YN izTitle;

    @Column(name = "parent_depth")
    private int parentDepth;

    @Column(name = "contents")
    private String contents;

    @Column(name = "orders")
    private int orders;

    @Column(name = "parent_orders")
    private int parentOrders;

    @Column(name = "types")
    private String types;

    @Column(name = "memo")
    private String memo;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checklist_project", columnDefinition = "bigint COMMENT '체크리스트 프로젝트'")
    @JsonManagedReference
    private ChecklistProject checklistProject;

    //자식테이블 매핑
    @OneToMany(mappedBy = "checklistProjectDetail")
    private List<ChecklistProjectResult> checklistProjectResultList = new ArrayList<>();

    public void update(ChecklistProjectDetail detail) {
        setDepth(detail.getDepth());
        setIzTitle(detail.getIzTitle());
        setParentDepth(detail.getParentDepth());
        setContents(detail.getContents());
        setOrders(detail.getOrders());
        setParentOrders(detail.getParentOrders());
        setTypes(detail.getTypes());
    }

    @Builder
    public ChecklistProjectDetail(long id, int depth, YN izTitle, int parentDepth, String contents, int orders,
        int parentOrders, String types, long checklistId) {
        super();
        this.id = id;
        this.depth = depth;
        this.izTitle = izTitle;
        this.parentDepth = parentDepth;
        this.contents = contents;
        this.orders = orders;
        this.parentOrders = parentOrders;
        this.types = types;
    }

    @Override
    public boolean equals(Object object) {
        ChecklistProjectDetail detail = (ChecklistProjectDetail) object;

        if(detail.id == this.id){
            return true;
        }
        return false;
    }
}
