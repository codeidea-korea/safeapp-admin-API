package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.safeapp.admin.web.data.ProjectType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "projects")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Project extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startAt;

    @Column(name = "end_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endAt;

    @Column(name = "max_user_count")
    private long maxUserCount;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "contents")
    private String contents;

    @Column(name = "image")
    private String image;

    @Column(name = "status")
    private ProjectType status;


    @Builder
    public Project(long id, String name, LocalDateTime startAt, LocalDateTime endAt, long maxUserCount, String address,
        String addressDetail, String contents, String image, ProjectType status, LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.startAt = startAt;
        this.endAt = endAt;
        this.maxUserCount = maxUserCount;
        this.address = address;
        this.addressDetail = addressDetail;
        this.contents = contents;
        this.image = image;
        this.status = status;
    }
}
