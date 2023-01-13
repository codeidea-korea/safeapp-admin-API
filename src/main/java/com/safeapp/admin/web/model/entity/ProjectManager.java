package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "project_managers")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "project", columnDefinition = "bigint COMMENT '프로젝트'")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user", columnDefinition = "bigint COMMENT '유저'")
    private Users user;
}
