package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.YN;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_template_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListTemplateDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checklist_template")
    private CheckListTemplate checkListTemplate;

    @Column(name = "depth")
    private int depth;

    @Column(name = "is_depth")
    private YN isDepth;

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
    public CheckListTemplateDetail(long id, int depth, YN isDepth, int parentDepth, String contents,
                                   int orders, String types, int parentOrders) {

        super();

        this.id = id;
        this.depth = depth;
        this.isDepth = isDepth;
        this.parentDepth = parentDepth;
        this.contents = contents;
        this.orders = orders;
        this.types = types;
        this.parentOrders = parentOrders;
    }
}
