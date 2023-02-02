package com.safeapp.admin.web.model.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "createdAt", columnDefinition = "datetime(6) COMMENT '생성날짜'", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", columnDefinition = "datetime(6) COMMENT '수정날짜'", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "delete_yn", columnDefinition = "bit(1) COMMENT '삭제여부'", nullable = false)
    private Boolean deleteYn = false;

}