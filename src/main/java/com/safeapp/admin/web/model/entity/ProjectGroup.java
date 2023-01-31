package com.safeapp.admin.web.model.entity;

import javax.persistence.*;

import com.safeapp.admin.web.data.UserAuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "project_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_auth_type")
    private String userAuthType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "project")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    @Builder
    public ProjectGroup(Long id, String name, String userAuthType, Users user, Project project) {
        super();

        this.id = id;
        this.name = name;
        this.userAuthType = userAuthType;

        this.user = user;
        this.project = project;
    }

}