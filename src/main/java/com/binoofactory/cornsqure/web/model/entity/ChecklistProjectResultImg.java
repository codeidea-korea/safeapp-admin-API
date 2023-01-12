package com.binoofactory.cornsqure.web.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "checklist_project_result_images")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChecklistProjectResultImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result",  columnDefinition = "bigint COMMENT '체크리스트 프로젝트 결과'")
    private ChecklistProjectResult result;

    @Column(name = "url", columnDefinition = "varchar(500) COMMENT 'URL'")
    private String url;

    @Builder
    public ChecklistProjectResultImg(long id, long resultId, String url) {

        this.id = id;
        this.url = url;
    }
}
