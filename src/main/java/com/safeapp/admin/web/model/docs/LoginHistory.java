package com.safeapp.admin.web.model.docs;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Document
@Data
public class LoginHistory {

    @Id
    @ApiModelProperty(value = "mongo PK")
    private String docId;

    @ApiModelProperty("회원 ID")
    private String userId;

    @ApiModelProperty("회원 비밀번호")
    private String password;

    @ApiModelProperty("로그인 성공 여부")
    private boolean isSuccess;

    @ApiModelProperty("로그인일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDt;

    @ApiModelProperty("접근 정보")
    private String platformInfo;

    @Builder
    public LoginHistory(String docId, String userId, String password, boolean isSuccess,
            LocalDateTime createDt, String platformInfo) {

        super();

        this.docId = docId;
        this.userId = userId;
        this.password = password;
        this.isSuccess = isSuccess;
        this.createDt = createDt;
        this.platformInfo = platformInfo;
    }

}