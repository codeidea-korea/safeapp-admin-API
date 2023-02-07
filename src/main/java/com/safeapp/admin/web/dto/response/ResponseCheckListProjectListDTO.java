package com.safeapp.admin.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "체크리스트 목록 응답")
@Data
@AllArgsConstructor
public class ResponseCheckListProjectListDTO {

    @Schema(description = "체크리스트 총 개수")
    Long count;

    @Schema(description = "체크리스트 목록")
    List<ResponseCheckListProjectDTO> list;

}