package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.model.entity.CheckListProject;
import com.safeapp.admin.web.model.entity.CheckListProjectDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "체크리스트 응답")
@Data
public class ResponseCheckListProjectDTO {

    @Schema(description = "체크리스트 PK")
    Long id;

    @Schema(description = "제목")
    String name;

    @Schema(description = "등록자 이름")
    String userName;

    @Schema(description = "프로젝트 PK")
    Long projectId;

    @Schema(description = "등록일시")
    LocalDateTime createdDate;

    @Schema(description = "조회수")
    Integer views;

    @Schema(description = "좋아요 수")
    Integer likeCount;

    @Schema(description = "본문")
    List<String> contents = new ArrayList<>();

    @Builder
    public ResponseCheckListProjectDTO(CheckListProject checkListProject) {
        this.id = checkListProject.getId();
        this.name = checkListProject.getName();
        this.userName = checkListProject.getUser().getUserName();
        this.createdDate = checkListProject.getCreatedAt();
        this.views = checkListProject.getViews();
        this.likeCount = checkListProject.getLikes();

        if(checkListProject.getProject() != null) {
            this.projectId = checkListProject.getProject().getId();
        }

        if(checkListProject.getCheckListProjectDetailList().isEmpty() == false) {
            for(CheckListProjectDetail detail : checkListProject.getCheckListProjectDetailList()) {
                this.contents.add(detail.getContents());
            }
        }
    }

}