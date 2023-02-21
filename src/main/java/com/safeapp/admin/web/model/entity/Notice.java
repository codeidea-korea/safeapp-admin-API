package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;

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
    private long id;

    @Enumerated(STRING)
    @Column(name = "type")
    private NoticeType type;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "user_id")
    private long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user", insertable = false, updatable = false)
    private Users user;

    @Column(name = "priority")
    private Boolean priority;

    @Builder
    public Notice(long id, NoticeType type, String title, String contents, long userId) {
        super();

        this.id = id;
        this.type = type;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }

}