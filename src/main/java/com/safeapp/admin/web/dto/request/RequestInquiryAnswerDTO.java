package com.safeapp.admin.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description ="고객센터 답변 등록 요청")
@Data
public class RequestInquiryAnswerDTO {

    @Schema(description = "답변자(관리자) PK")
    private Long answerAdminId;

    @Schema(description = "답변 내용")
    private String answer;

}