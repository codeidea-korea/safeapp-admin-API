package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "notices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin")
    private Admins admin;

    @Column(name = "priority")
    private Boolean priority;

    @Enumerated(STRING)
    @Column(name = "type")
    private NoticeType type;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Builder
    public Notice(Long id, NoticeType type, String title, String contents) {
        super();

        this.id = id;
        this.type = type;
        this.title = title;
        this.contents = contents;
    }

    public void edit(Notice newNotice) {
        setType(newNotice.getType());
        setPriority(newNotice.getPriority());
        setTitle(newNotice.getTitle());
        setContents(newNotice.getContents());
        setUpdatedAt(newNotice.getUpdatedAt());
    }

}