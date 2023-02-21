package com.safeapp.admin.web.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "notice_files")
@Data
@NoArgsConstructor
public class NoticeFiles extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice")
    private Notice notice;

    @Column(name = "url")
    private String url;

    @Builder
    public NoticeFiles(Long id, Notice notice, String url) {
        this.id = id;
        this.notice = notice;
        this.url = url;
    }

}