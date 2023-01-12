package com.binoofactory.cornsqure.web.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auths")
@Data
@NoArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "allowed_menu")
    private String allowedMenu;

    @Builder
    public Auth(long id, String name, String allowedMenu) {
        this.id = id;
        this.name = name;
        this.allowedMenu = allowedMenu;
    }
}
