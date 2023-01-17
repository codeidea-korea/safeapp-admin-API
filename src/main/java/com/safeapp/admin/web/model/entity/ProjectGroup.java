package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "project_groups")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project", columnDefinition = "bigint COMMENT '프로젝트'")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
    private Admins admin;

    @Builder
    public ProjectGroup(long id, String name, Admins admin, Project project) {
        super();
        this.id = id;
        this.admin = admin;
        this.project = project;
        this.name = name;
    }
}
