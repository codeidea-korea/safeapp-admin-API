package com.safeapp.admin.web.model.entity.cmmn;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sy_file_dtl")
@Data
@NoArgsConstructor
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    @ApiModelProperty("파일 PK")
    private Long fileNo;

    @Column(name = "file_nm")
    @ApiModelProperty("서버상 파일 경로 + 파일명")
    private String fileNm;

    @Column(name = "web_file_nm")
    @ApiModelProperty("웹상 파일 경로 + 파일명")
    private String webFileNm;

    @Column(name = "upload_type")
    @ApiModelProperty("파일 확장자")
    private String uploadType;

    @Column(name = "grp_file_no")
    @ApiModelProperty("파일 그룹 PK (그룹핑이 필요한 경우)")
    private Integer grpFileNo;

    @Column(name = "create_dt")
    @ApiModelProperty("파일 업로드일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @Builder
    public Files(String fileNm, String webFileNm, String uploadType, Integer grpFileNo) {
        this.fileNm = fileNm;
        this.webFileNm = webFileNm;
        this.uploadType = uploadType;
        this.grpFileNo = grpFileNo;
    }

}