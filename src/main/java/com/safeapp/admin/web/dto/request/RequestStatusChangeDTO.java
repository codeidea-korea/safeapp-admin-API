package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "상태값변경")
public class RequestStatusChangeDTO {

    List<Detail> details = new ArrayList<>();
    @Getter
    @Setter
    public static class Detail{
        @Schema(description = "디테일ID")
        Long detailId;
        @Schema(description = "메모")
        String memo;
    }
}
