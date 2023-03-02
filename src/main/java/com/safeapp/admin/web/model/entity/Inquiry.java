package com.safeapp.admin.web.model.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.safeapp.admin.web.data.InquiryNounType;
import com.safeapp.admin.web.data.InquiryServiceType;
import com.safeapp.admin.web.data.YN;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static javax.persistence.EnumType.STRING;

@Entity(name = "inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inquiry_user")
    private Users inquiryUser;

    @ManyToOne
    @JoinColumn(name = "inquiry_admin")
    private Admins inquiryAdmin;

    @Enumerated(STRING)
    @Column(name = "inquiry_type")
    private InquiryNounType inquiryType;

    @Enumerated(STRING)
    @Column(name = "service_type")
    private InquiryServiceType serviceType;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "attachment_name")
    private String attachmentName;



    @Column(name = "is_answer")
    private YN isAnswer;

    @ManyToOne
    @JoinColumn(name = "answer_admin")
    private Admins answerAdmin;

    @Column(name = "answer_at")
    private LocalDateTime answerAt;

    @Column(name = "answer")
    private String answer;



    @Transient
    private Long adminId;



    @Builder
    public Inquiry(Long id, InquiryNounType inquiryType, InquiryServiceType serviceType, String title, String contents,
            YN isAnswer, String answer) {

        super();

        this.id = id;
        this.inquiryType = inquiryType;
        this.serviceType = serviceType;
        this.title = title;
        this.contents = contents;

        this.isAnswer = isAnswer;
        this.answer = answer;
    }

    /*
    public void edit(Inquiry newInquiry) {
        this.inquiryType = newInquiry.getInquiryType();
        this.serviceType = newInquiry.getServiceType();
        this.title = newInquiry.getTitle();
        this.contents = newInquiry.getContents();
        setUpdatedAt(newInquiry.getUpdatedAt());
    }
    */

    public void answer(Inquiry newInquiry) {
        this.isAnswer = newInquiry.getIsAnswer();
        this.answerAdmin = newInquiry.getAnswerAdmin();
        this.answerAt = newInquiry.getAnswerAt();
        this.answer = newInquiry.getAnswer();
    }

}