package com.safeapp.admin.web.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reports extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "concern_accidet")
    private ConcernAccidentExp concernAccidentExp;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "report_user")
    private Users reportUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "report_admin")
    private Admins reportAdmin;

    @Column(name = "report_reason")
    private String reportReason;

    @Builder
    public Reports(ConcernAccidentExp concernAccidentExp, Users reportUser, Admins reportAdmin, String reportReason) {
        this.concernAccidentExp = concernAccidentExp;
        this.reportUser = reportUser;
        this.reportAdmin = reportAdmin;
        this.reportReason = reportReason;
    }

}