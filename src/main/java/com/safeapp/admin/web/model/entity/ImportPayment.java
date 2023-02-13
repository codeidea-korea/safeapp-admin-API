package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.PayMethod;
import com.safeapp.admin.web.data.YN;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.EnumType.STRING;

@Entity(name = "if_payments")
@Data
@NoArgsConstructor
public class ImportPayment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "merchant_uid")
    private String merchantUid;

    @Column(name = "pay_method")
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Column(name = "channel")
    private String channel;

    @Column(name = "pg_provider")
    private String pgProvider;

    @Column(name = "emb_pg_provider")
    private String embPgProvider;

    @Column(name = "pg_tid")
    private String pgTid;

    @Column(name = "pg_id")
    private String pgId;

    @Column(name = "apply_num")
    private String applyNum;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "vbank_code")
    private String vbankCode;

    @Column(name = "vbank_name")
    private String vbankName;

    @Column(name = "vbank_num")
    private String vbankNum;

    @Column(name = "vbank_holder")
    private String vbankHolder;

    @Column(name = "vbank_date")
    private Long vbankDate;

    @Column(name = "vbank_issued_at")
    private Integer vbankIssuedAt;

    @Column(name = "card_quota")
    private Integer cardQuota;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "cancel_amount")
    private Integer cancelAmount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "buyer_tel")
    private String buyerTel;

    @Column(name = "buyer_addr")
    private String buyerAddr;

    @Column(name = "buyer_postcode")
    private String buyerPostcode;

    @Column(name = "custom_data")
    private String customData;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "status")
    private String status;

    @Column(name = "started_at")
    private Integer startedAt;

    @Column(name = "paid_at")
    private Integer paidAt;

    @Column(name = "failed_at")
    private Integer failedAt;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Column(name = "cancelled_at")
    private String cancelledAt;

    @Column(name = "customer_uid")
    private String customerUid;

    @Column(name = "escrow")
    private YN escrow;

    @Column(name = "cash_receipt_issued")
    private YN cashReceiptIssued;

    @Enumerated(STRING)
    @Column(name = "update_yn")
    private YN updateYN;

    @Enumerated(STRING)
    @Column(name = "billing_yn")
    private YN billingYN;

    @Enumerated(STRING)
    @Column(name = "cancel_request_yn")
    private YN cancelRequestYN;

    @Builder
    public ImportPayment(Long id, String impUid, String merchantUid, PayMethod payMethod, String channel,
            String pgProvider, String embPgProvider, String pgTid, String pgId, String applyNum, String bankCode,
            String bankName, String cardCode, String cardName, String cardNumber, String cardType, String vbankCode,
            String vbankName, String vbankNum, String vbankHolder, Long vbankDate, Integer vbankIssuedAt, Integer cardQuota,
            String name, Integer amount, Integer cancelAmount, String currency, String buyerName, String buyerEmail,
            String buyerTel, String buyerAddr, String buyerPostcode, String customData, String userAgent, String status,
            Integer startedAt, Integer paidAt, Integer failedAt, String failReason, String cancelReason, String receiptUrl,
            String cancelledAt, String customerUid, YN escrow, YN cashReceiptIssued, YN updateYN, YN billingYN, YN cancelRequestYN) {

        super();

        this.id = id;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.payMethod = payMethod;
        this.channel = channel;
        this.pgProvider = pgProvider;
        this.embPgProvider = embPgProvider;
        this.pgTid = pgTid;
        this.pgId = pgId;
        this.applyNum = applyNum;
        this.bankCode = bankCode;
        this.bankName = bankName;
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.vbankCode = vbankCode;
        this.vbankName = vbankName;
        this.vbankNum = vbankNum;
        this.vbankHolder = vbankHolder;
        this.vbankDate = vbankDate;
        this.vbankIssuedAt = vbankIssuedAt;
        this.cardQuota = cardQuota;
        this.name = name;
        this.amount = amount;
        this.cancelAmount = cancelAmount;
        this.currency = currency;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.buyerPostcode = buyerPostcode;
        this.customData = customData;
        this.userAgent = userAgent;
        this.status = status;
        this.startedAt = startedAt;
        this.paidAt = paidAt;
        this.failedAt = failedAt;
        this.failReason = failReason;
        this.cancelReason = cancelReason;
        this.receiptUrl = receiptUrl;
        this.cancelledAt = cancelledAt;
        this.customerUid = customerUid;
        this.escrow = escrow;
        this.cashReceiptIssued = cashReceiptIssued;
        this.updateYN = updateYN;
        this.billingYN = billingYN;
        this.cancelRequestYN = cancelRequestYN;
    }

}