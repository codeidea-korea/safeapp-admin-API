package com.safeapp.admin.web.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "accident_exp_files")
@Data
@NoArgsConstructor
public class AccidentExpFiles extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result")
    private AccidentExp accExp;

    @Column(name = "url")
    private String url;

    @Builder
    public AccidentExpFiles(Long id, AccidentExp accExp, String url) {
        this.id = id;
        this.accExp = accExp;
        this.url = url;
    }

}