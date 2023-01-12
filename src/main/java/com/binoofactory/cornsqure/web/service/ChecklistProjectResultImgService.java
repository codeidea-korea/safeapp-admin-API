package com.binoofactory.cornsqure.web.service;

import com.binoofactory.cornsqure.web.model.cmmn.service.BfCRUDService;
import com.binoofactory.cornsqure.web.model.entity.ChecklistProjectResultImg;

public interface ChecklistProjectResultImgService extends BfCRUDService<ChecklistProjectResultImg> {
    ChecklistProjectResultImg generate(ChecklistProjectResultImg userSeq);
}
