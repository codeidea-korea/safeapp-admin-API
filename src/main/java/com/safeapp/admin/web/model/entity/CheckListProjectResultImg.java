package com.safeapp.admin.web.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "checklist_project_result_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListProjectResultImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result")
    private CheckListProjectResult result;

    @Column(name = "url")
    private String url;

    @Builder
    public CheckListProjectResultImg(long id, long resultId, String url) {

        this.id = id;
        this.url = url;

    }

}