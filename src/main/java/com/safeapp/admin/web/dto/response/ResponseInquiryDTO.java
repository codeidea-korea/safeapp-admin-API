package com.safeapp.admin.web.dto.response;

import com.safeapp.admin.web.data.InquiryNounType;
import com.safeapp.admin.web.data.InquiryServiceType;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.Inquiry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "고객센터 응답")
@Data
@NoArgsConstructor
public class ResponseInquiryDTO {

    @Schema(description = "고객센터 PK")
    Long id;

    @Schema(description = "등록자(사용자) 이름")
    String userName;

    @Schema(description = "등록일시")
    LocalDateTime createdAt;

    @Schema(description = "문의 유형")
    InquiryNounType inquiryType;

    @Schema(description = "서비스 유형")
    InquiryServiceType serviceType;

    @Schema(description = "제목")
    String title;

    @Schema(description = "내용")
    String contents;

    @Schema(description = "첨부파일 실제경로")
    private String attachment;

    @Schema(description = "첨부파일 본래파일명")
    private String attachmentName;



    @Schema(description = "답변 여부")
    YN isAnswer;

    @Schema(description = "답변자(관리자) 이름")
    String adminName;

    @Schema(description = "답변일시")
    LocalDateTime answerAt;

    @Schema(description = "답변 내용")
    String answer;



    @Builder
    public ResponseInquiryDTO(Inquiry inquiry) {
        this.id = inquiry.getId();
        if(!Objects.isNull(inquiry.getInquiryUser())) {
            this.userName = inquiry.getInquiryUser().getUserName();
        }
        if(!Objects.isNull(inquiry.getInquiryAdmin())) {
            this.userName = inquiry.getInquiryAdmin().getAdminName();
        }
        this.createdAt = inquiry.getCreatedAt();
        this.inquiryType = inquiry.getInquiryType();
        this.serviceType = inquiry.getServiceType();
        this.title = inquiry.getTitle();
        this.contents = inquiry.getContents();
        this.attachment = inquiry.getAttachment();
        this.attachmentName = inquiry.getAttachmentName();

        this.isAnswer = inquiry.getIsAnswer();
        if(this.isAnswer == YN.Y) {
            this.adminName = inquiry.getAnswerAdmin().getAdminName();
            this.answerAt = inquiry.getAnswerAt();
            this.answer = inquiry.getAnswer();
        }
    }

}