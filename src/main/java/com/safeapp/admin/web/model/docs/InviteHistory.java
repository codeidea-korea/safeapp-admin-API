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
public class InviteHistory {

    @Id
    @ApiModelProperty(value = "mongo PK")
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    private long groupId;

    private String groupName;

    private String userMail;

    private String contents;

    private String urlData;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime efectiveEndAt;

    @Builder
    public InviteHistory(String id, LocalDateTime createdAt, long groupId, String groupName, String userMail, String contents,
            String urlData, LocalDateTime efectiveEndAt) {

        this.id = id;
        this.createdAt = createdAt;
        this.groupId = groupId;
        this.groupName = groupName;
        this.userMail = userMail;
        this.contents = contents;
        this.urlData = urlData;
        this.efectiveEndAt = efectiveEndAt;
    }

}