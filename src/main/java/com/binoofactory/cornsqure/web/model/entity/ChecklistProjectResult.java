package com.binoofactory.cornsqure.web.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.binoofactory.cornsqure.web.data.CheckType;
import com.binoofactory.cornsqure.web.data.ChecklistProjectResultStatus;
import com.binoofactory.cornsqure.web.data.YN;

import lombok.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "checklist_project_results")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChecklistProjectResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint COMMENT '체크리스트 프로젝트 결과값 고유아이디'")
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checklist_project_detail", columnDefinition = "bigint COMMENT '체크리스트 프로젝트 상세'")
    private ChecklistProjectDetail checklistProjectDetail;

    @Column(name = "is_check")
    private CheckType checkYn;

    @Column(name = "memo")
    private String memo;

    @Column(name = "status")
    private ChecklistProjectResultStatus status;

    @OneToMany(mappedBy = "result")
    private List<ChecklistProjectResultImg> imgs = new ArrayList<>();

    @Builder
    public ChecklistProjectResult(long id, long detailId, CheckType check, String memo, ChecklistProjectResultStatus status) {
        super();
        this.id = id;
        this.checkYn = check;
        this.memo = memo;
        this.status = status;
    }
}
