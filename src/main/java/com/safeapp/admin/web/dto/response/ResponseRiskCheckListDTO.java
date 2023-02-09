package com.safeapp.admin.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Schema(description = "위험성 평가표 목록 응답")
@Data
@AllArgsConstructor
public class ResponseRiskCheckListDTO {

    @Schema(description = "위험성 평가표 총 개수")
    Long count;

    @Schema(description = "위험성 평가표 목록")
    List<ResponseCheckListProjectDTO> list;

}