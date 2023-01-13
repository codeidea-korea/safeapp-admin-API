package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.ChecklistProject;
import com.safeapp.admin.web.model.entity.ChecklistProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "체크리스트 메인")
public class ResponseChecklistProjectDTO {
    @Schema(description = "고유 아이디")
    Long id;
    @Schema(description = "제목")
    String name;
    @Schema(description = "프로젝트ID")
    Long projectId;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "등록일")
    LocalDateTime createdDate;

    @Schema(description = "열람횟수")
    Integer views;

    @Schema(description = "좋아요 카운트")
    Integer likeCount;

    @Schema(description = "로그인한 유저가 해당 게시글 좋아요 눌렀는지 체크")
    YN liked;

    @Schema(description = "본문")
    List<String> content = new ArrayList<>();

    @Builder
    public ResponseChecklistProjectDTO(ChecklistProject checklistProject) {
        this.id = checklistProject.getId();
        this.name = checklistProject.getName();
        if(checklistProject.getProject() != null) {
            this.projectId = checklistProject.getProject().getId();
        }
        this.userName = checklistProject.getUser().getUserName();
        this.createdDate = checklistProject.getCreatedAt();
        this.views = checklistProject.getViews();
        this.likeCount = checklistProject.getLikes();
        this.liked = checklistProject.getLiked();
        if(checklistProject.getChecklistProjectDetailList().isEmpty() == false) {
            for(ChecklistProjectDetail detail : checklistProject.getChecklistProjectDetailList()){
                    this.content.add(detail.getContents());
            }
        }
    }
}