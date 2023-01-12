package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.dto.request.RequestAccidentCaseDTO;
import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.AccidentExp;
import org.apache.ibatis.javassist.NotFoundException;

public interface AccidentExpService extends BfCRUDService<AccidentExp> {
    AccidentExp generate(AccidentExp userSeq);

    AccidentExp toEntity(RequestAccidentCaseDTO dto) throws NotFoundException;
}
