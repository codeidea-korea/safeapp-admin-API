package com.binoofactory.cornsqure.web.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Many;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "risk_template_details")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RiskTemplateDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "risk_temp_id")
    private long riskTempId;

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

    @Column(name = "due_user_id")
    private long dueUserId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "due_user")
    private Users dueUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "check_user")
    private Users checkUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "risk_template", columnDefinition = "bigint COMMENT '위험요소 템플릿'")
    private RiskTemplate riskTemplate;

    @Column(name = "parent_orders")
    private int parentOrders;

    @Column(name = "orders")
    private int orders;

    @Column(name = "depth")
    private int depth;

    @Column(name = "parent_depth")
    private int parentDepth;

    @Builder
    public RiskTemplateDetail(long id, long riskTempId, String contents, String address, String addressDetail,
        String tools, String riskFactorType, String relateLaw, String relateGuide, String riskType,
        String reduceResponse, String checkMemo, long dueUserId, Users dueUser, Users checkUser, int parentOrders,
        int orders, int depth, int parentDepth) {
        super();
        this.id = id;
        this.riskTempId = riskTempId;
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
        this.dueUserId = dueUserId;
        this.dueUser = dueUser;
        this.checkUser = checkUser;
        this.parentOrders = parentOrders;
        this.orders = orders;
        this.depth = depth;
        this.parentDepth = parentDepth;
    }
}
