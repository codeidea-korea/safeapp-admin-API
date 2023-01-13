package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ConcernAccidentExp;

public interface ConcernAccidentExpService extends CRUDService<ConcernAccidentExp> {
    ConcernAccidentExp generate(ConcernAccidentExp userSeq);
}
