package com.binoofactory.cornsqure.web.dto.response;

import com.binoofactory.cornsqure.web.data.ChecklistProjectResultStatus;
import com.binoofactory.cornsqure.web.data.YN;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema(description = "response resultDTO ")
public class ResponseChecklistProjectResultDTO {

    @Schema(description = "id")
    @NotBlank(message = NOT_NULL)
    Long id;
    @Schema(description = "체크여부")
    @NotBlank(message = NOT_NULL)
    YN checkYn;

    @Schema(description = "메모")
    String memo;

    @Schema(description = "status")
    ChecklistProjectResultStatus status;

    @Schema(description = "detail id")
    Long detailId;

    @Builder
    public ResponseChecklistProjectResultDTO(ChecklistProjectResult checklistProjectResult) {
        this.id = checklistProjectResult.getId();
        this.checkYn = checklistProjectResult.getCheckYn();
        this.memo = checklistProjectResult.getMemo();
        this.status = checklistProjectResult.getStatus();
        this.detailId = checklistProjectResult.getChecklistProjectDetail().getId();
    }
}
