package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "project_groups")
@Data
@NoArgsConstructor
public class ProjectGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "project")
    private Long project;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    @Builder
    public ProjectGroup(Long id, String name, Long project, Users user) {
        super();

        this.id = id;
        this.name = name;
        this.project = project;
        this.user = user;
    }

}