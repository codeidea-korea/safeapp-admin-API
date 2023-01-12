package com.binoofactory.cornsqure.web.model.entity.cmmn;

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
public class BfFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_no")
    @ApiModelProperty("리소스 파일 일련번호")
    private long fileSeq;

    @Column(name = "file_nm")
    @ApiModelProperty("리소스 파일 명")
    private String fileName;

    @Column(name = "web_file_nm")
    @ApiModelProperty("리소스 파일 웹 경로")
    private String webPath;

    // TODO: 추후 공통 코드로 관리되어야 함. -> 우선은 String 으로 이후 enum, 이후 공통 코드로 변경 필.
    @Column(name = "upload_type")
    @ApiModelProperty("리소스 파일 종류 (확장자)")
    private String type;

    @Column(name = "grp_file_no")
    @ApiModelProperty("리소스 파일 그룹 식별자 (어떤 상황에 따라 그룹핑이 필요한 경우)")
    private Integer fileGroupNo;

    @Column(name = "create_dt")
    @ApiModelProperty("생성 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @Builder
    public BfFile(String fileName, String webPath, String type, Integer fileGroupNo) {
        this.fileName = fileName;
        this.webPath = webPath;
        this.type = type;
        this.fileGroupNo = fileGroupNo;
    }
}
