package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.CheckListProjectResultStatus;
import com.safeapp.admin.web.data.CheckType;
import com.safeapp.admin.web.model.entity.CheckListProjectResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "체크리스트 결과 응답")
@Data
public class ResponseCheckListProjectResultDTO {

    @Schema(description = "id")
    @NotBlank(message = NOT_NULL)
    Long id;

    @Schema(description = "체크여부")
    @NotBlank(message = NOT_NULL)
    CheckType checkYn;

    @Schema(description = "메모")
    String memo;

    @Schema(description = "status")
    CheckListProjectResultStatus status;

    @Schema(description = "detailId")
    Long detailId;

    @Builder
    public ResponseCheckListProjectResultDTO(CheckListProjectResult checkListProjectResult) {
        this.id = checkListProjectResult.getId();
        this.checkYn = checkListProjectResult.getIsCheck();
        this.memo = checkListProjectResult.getMemo();
        this.status = checkListProjectResult.getStatus();
        this.detailId = checkListProjectResult.getCheckListProjectDetail().getId();
    }

}