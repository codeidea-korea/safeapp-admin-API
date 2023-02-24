package com.safeapp.admin.web.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "concern_accident_exp_files")
@Data
@NoArgsConstructor
public class ConcernAccidentExpFiles extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result")
    private ConcernAccidentExp conExp;

    @Column(name = "url")
    private String url;

    @Builder
    public ConcernAccidentExpFiles(Long id, ConcernAccidentExp conExp, String url) {
        this.id = id;
        this.conExp = conExp;
        this.url = url;
    }

}