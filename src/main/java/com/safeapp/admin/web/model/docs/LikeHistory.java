package com.safeapp.admin.web.model.docs;

import com.safeapp.admin.web.data.YN;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Document
public class LikeHistory {
    @Id
    @ApiModelProperty(value = "몽고 식별자")
    private String docId;

    @ApiModelProperty("사용자 식별자")
    private long userId;

    @ApiModelProperty("게시판 식별자")
    private long boardId;

    @ApiModelProperty("구분")
    private String type;

    @ApiModelProperty("좋아요 여부")
    private YN liked;

    @Builder
    public LikeHistory(String docId, long userId, long boardId, String type, YN liked) {
        super();
        this.docId = docId;
        this.userId = userId;
        this.boardId = boardId;
        this.type = type;
        this.liked = liked;
    }
}
