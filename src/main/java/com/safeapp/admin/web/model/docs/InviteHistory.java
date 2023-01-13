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

@Data
@Document
public class InviteHistory {
    @Id
    @ApiModelProperty(value = "몽고 식별자")
    private String id;
    private long groupId;
    private String groupName;
    private String contents;
    private String userMail;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime efectiveEndAt;

    private String urlData;

    @Builder
    public InviteHistory(String id, long groupId, String groupName, String contents, String userMail,
        LocalDateTime createdAt, LocalDateTime efectiveEndAt, String urlData) {

        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.contents = contents;
        this.userMail = userMail;
        this.createdAt = createdAt;
        this.efectiveEndAt = efectiveEndAt;
        this.urlData = urlData;
    }
}
