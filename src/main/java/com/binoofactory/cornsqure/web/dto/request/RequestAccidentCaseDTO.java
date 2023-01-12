package com.binoofactory.cornsqure.web.dto.request;

import io.netty.channel.ChannelHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "사고사례 ")
public class RequestAccidentCaseDTO {
    @Schema(description = "제목")
    String title;

    @Schema(description = "유저ID")
    Long userId;

    @Schema(description = "태그")
    String tags;

    @Schema(description = "이름")
    String name;

    @Schema(description = "사고발생시각")
    LocalDateTime accidentAt;

    @Schema(description = "사고UID")
    String accidentUid;

    @Schema(description = "사고이유")
    String accidentReason;

    @Schema(description = "사고원인")
    String accidentCause;

    @Schema(description = "원인상세")
    String causeDetail;

    @Schema(description = "반응")
    String response;
}
