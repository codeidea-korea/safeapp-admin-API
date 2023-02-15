package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestAccidentCaseDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.AccidentExp;
import org.apache.ibatis.javassist.NotFoundException;

public interface AccidentExpService extends CRUDService<AccidentExp> {

    AccidentExp toEntity(RequestAccidentCaseDTO addDto) throws NotFoundException;

    AccidentExp generate(AccidentExp newAccExp);

}