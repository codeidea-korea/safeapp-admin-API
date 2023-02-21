package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "공지사항 수정 요청")
@Data
public class RequestNoticeEditDTO {

    @Schema(description = "유형")
    private NoticeType type;

    @Schema(description = "우선게시")
    private Boolean priority;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String contents;

}