package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestDetailModifyDTO;
import com.safeapp.admin.web.dto.request.RequestStatusChangeDTO;
import com.safeapp.admin.web.dto.response.ResponseChecklistProjectDetailDTO;
import com.safeapp.admin.web.dto.response.ResponseRiskCheckDetailDTO;
import com.safeapp.admin.web.data.DocumentType;
import com.safeapp.admin.web.data.StatusType;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.binoofactory.cornsqure.web.model.entity.*;
import com.cornsqure.admin.web.model.entity.*;
import com.safeapp.admin.web.model.entity.*;
import org.apache.ibatis.javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProjectService extends CRUDService<Project> {
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
