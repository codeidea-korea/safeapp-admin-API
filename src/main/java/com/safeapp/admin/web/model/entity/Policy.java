package com.safeapp.admin.web.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "admin")
    private Admins admin;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Builder
    public Policy(Long id, String title, String contents) {
        super();

        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public void edit(Policy newPolicy) {
        setAdmin(newPolicy.getAdmin());
        setUpdatedAt(newPolicy.getUpdatedAt());
        setContents(newPolicy.getContents());
    }

}