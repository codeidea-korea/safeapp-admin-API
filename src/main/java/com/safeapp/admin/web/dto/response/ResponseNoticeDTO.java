package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.NoticeType;
import com.safeapp.admin.web.model.entity.AccidentExp;
import com.safeapp.admin.web.model.entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "공지사항 응답")
@Data
@NoArgsConstructor
public class ResponseNoticeDTO {

    @Schema(description = "공지사항 PK")
    Long id;

    @Schema(description = "등록자(관리자) 이름")
    String adminName;

    @Schema(description = "등록일시")
    LocalDateTime createdAt;

    @Schema(description = "유형")
    NoticeType type;

    @Schema(description = "우선게시")
    Boolean priority;

    @Schema(description = "제목")
    String title;

    @Schema(description = "내용")
    String contents;

    @Builder
    public ResponseNoticeDTO(Notice notice) {
        this.id = notice.getId();
        this.adminName = notice.getAdmin().getAdminName();
        this.createdAt = notice.getCreatedAt();
        this.type = notice.getType();
        this.priority = notice.getPriority();
        this.title = notice.getTitle();
        this.contents = notice.getContents();
    }

}