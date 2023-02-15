package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Schema(description = "사고사례")
@Data
public class RequestAccidentExpDTO {

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String title;

    @Schema(description = "등록자 PK")
    @NotBlank(message = NOT_NULL)
    Long adminId;

}