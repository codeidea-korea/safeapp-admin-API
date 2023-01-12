package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestDetailModifyDTO;
import com.binoofactory.cornsqure.web.dto.request.RequestStatusChangeDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseChecklistProjectDetailDTO;
import com.binoofactory.cornsqure.web.dto.response.ResponseRiskCheckDetailDTO;
import com.binoofactory.cornsqure.web.data.DocumentType;
import com.binoofactory.cornsqure.web.data.StatusType;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.*;
import org.apache.ibatis.javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.Document;
import java.util.List;

public interface ProjectService extends BfCRUDService<Project> {
    Project generate(Project userSeq);

    List<ResponseChecklistProjectDetailDTO> findAllChecklistDetails(Long id);
    List<ResponseRiskCheckDetailDTO> findAllRiskCheckDetails(Long id);

    Long addProject(Long id, Long projectId, DocumentType documentType) throws NotFoundException;

    Long addNewProject(Long projectId, Long documentId, DocumentType documentType) throws NotFoundException;

    Boolean changeStatus(Long id, DocumentType documentType, StatusType status, String recheckReason, RequestStatusChangeDTO dto, HttpServletRequest request) throws NotFoundException;

    ChecklistProjectDetail templateDetailToProjectDetail(ChecklistTemplateDetail templateDetail);

    ChecklistTemplateDetail projectDetailToTemplateDetail(ChecklistProjectDetail detail);

    RiskCheckDetail templateDetailToRiskDetail(RiskTemplateDetail templateDetail);

    RiskTemplateDetail riskDetailToTemplateDetail(RiskCheckDetail detail);

    Long modifyDetails(List<RequestDetailModifyDTO> dto, DocumentType documentType) throws NotFoundException;
}
