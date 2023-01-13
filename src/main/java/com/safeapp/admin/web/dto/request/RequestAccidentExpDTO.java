package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import static org.hibernate.sql.InFragment.NOT_NULL;

@Getter
@Setter
@Schema(description = "사고 사례")
public class RequestAccidentExpDTO {

    @Schema(description = "제목")
    @NotBlank(message = NOT_NULL)
    String title;

    @Schema(description = "유저ID")
    @NotBlank(message = NOT_NULL)
    Long userId;
}
