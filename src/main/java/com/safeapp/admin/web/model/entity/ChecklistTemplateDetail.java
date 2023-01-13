package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_template_details")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChecklistTemplateDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checklist_template", columnDefinition = "bigint COMMENT '체크리스트 템플릿'")
    private ChecklistTemplate checklistTemplate;

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

    @Column(name = "types")
    private String types;

    @Column(name = "parent_orders")
    private int parentOrders;


    @Builder
    public ChecklistTemplateDetail(long id, long templateId, int depth, YN izTitle, int parentDepth, String contents,
        int orders, String types, int parentOrders) {
        super();
        this.id = id;
        this.depth = depth;
        this.izTitle = izTitle;
        this.parentDepth = parentDepth;
        this.contents = contents;
        this.orders = orders;
        this.types = types;
        this.parentOrders = parentOrders;
    }
}
