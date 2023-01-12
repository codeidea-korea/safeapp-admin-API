package com.binoofactory.cornsqure.web.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "risk_check_details")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RiskCheckDetail extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "risk_check_id")
    private long riskCheckId;

    @Column(name = "contents")
    private String contents;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "tools")
    private String tools;

    @Column(name = "risk_factor_type")
    private String riskFactorType;

    @Column(name = "relate_law")
    private String relateLaw;

    @Column(name = "relate_guide")
    private String relateGuide;

    @Column(name = "risk_type")
    private String riskType;

    @Column(name = "reduce_response")
    private String reduceResponse;

    @Column(name = "check_memo")
    private String checkMemo;

    @ManyToOne
    @JoinColumn(name = "due_user", columnDefinition = "bigint COMMENT '유저'")
    private Users dueUser;

    @ManyToOne
    @JoinColumn(name = "check_user", columnDefinition = "bigint COMMENT '체크 유저'")
    private Users checkUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "risk_check", columnDefinition = "bigint COMMENT '리스크 체크'")
    private RiskCheck riskCheck;

    @Column(name = "status")
    private String status;

    @Column(name = "parent_orders")
    private int parentOrders;

    @Column(name = "orders")
    private int orders;

    @Column(name = "depth")
    private int depth;

    @Column(name = "parent_depth")
    private int parentDepth;

    @Builder
    public RiskCheckDetail(long id, long riskCheckId, String contents, String address, String addressDetail,
        String tools, String riskFactorType, String relateLaw, String relateGuide, String riskType,
        String reduceResponse, String checkMemo, long dueUserId, Users dueUser, long checkUserId, Users checkUser,
        String status, int parentOrders, int orders, int depth, int parentDepth) {
        super();
        this.id = id;
        this.riskCheckId = riskCheckId;
        this.contents = contents;
        this.address = address;
        this.addressDetail = addressDetail;
        this.tools = tools;
        this.riskFactorType = riskFactorType;
        this.relateLaw = relateLaw;
        this.relateGuide = relateGuide;
        this.riskType = riskType;
        this.reduceResponse = reduceResponse;
        this.checkMemo = checkMemo;
        this.dueUser = dueUser;
        this.checkUser = checkUser;
        this.status = status;
        this.parentOrders = parentOrders;
        this.orders = orders;
        this.depth = depth;
        this.parentDepth = parentDepth;
    }
}
