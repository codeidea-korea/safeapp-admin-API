package com.safeapp.admin.web.dto.request;

import com.safeapp.admin.web.data.CheckType;
import com.safeapp.admin.web.data.ChecklistProjectResultStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema(description = "request - resultDTO")
public class RequestChecklistProjectResultDTO {

    @Schema(description = "체크여부")
    @NotBlank(message = NOT_NULL)
    CheckType checkYn;

    @Schema(description = "메모")
    String memo;

    @Schema(description = "status")
    ChecklistProjectResultStatus status;

    @Schema(description = "checklist_project_detail_id")
    Long detailId;



}
