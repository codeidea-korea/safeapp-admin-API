package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ConcernAccidentExp;

public interface ConcernAccidentExpService extends BfCRUDService<ConcernAccidentExp> {
    ConcernAccidentExp generate(ConcernAccidentExp userSeq);
}
