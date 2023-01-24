package com.safeapp.admin.web.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.safeapp.admin.web.data.CheckListProjectResultStatus;
import com.safeapp.admin.web.data.CheckType;

import lombok.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_project_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListProjectResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checklist_project_detail")
    private CheckListProjectDetail checkListProjectDetail;

    @Column(name = "is_check")
    private CheckType isCheck;

    @Column(name = "memo")
    private String memo;

    @Column(name = "status")
    private CheckListProjectResultStatus status;

    @OneToMany(mappedBy = "result")
    private List<CheckListProjectResultImg> imgs = new ArrayList<>();

    @Builder
    public CheckListProjectResult(Long id, CheckType isCheck, String memo, CheckListProjectResultStatus status) {
        super();

        this.id = id;
        this.isCheck = isCheck;
        this.memo = memo;
        this.status = status;
    }

}