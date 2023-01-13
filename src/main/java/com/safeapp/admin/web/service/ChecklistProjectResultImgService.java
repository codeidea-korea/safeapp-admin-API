package com.safeapp.admin.web.service;

import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.ChecklistProjectResultImg;

public interface ChecklistProjectResultImgService extends CRUDService<ChecklistProjectResultImg> {
    ChecklistProjectResultImg generate(ChecklistProjectResultImg userSeq);
}
