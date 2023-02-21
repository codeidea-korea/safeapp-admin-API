package com.safeapp.admin.web.service;

import com.safeapp.admin.web.dto.request.RequestPolicyEditDTO;
import com.safeapp.admin.web.model.cmmn.service.CRUDService;
import com.safeapp.admin.web.model.entity.Policy;
import org.apache.ibatis.javassist.NotFoundException;

public interface PolicyService extends CRUDService<Policy> {

    Policy toEditEntity(RequestPolicyEditDTO editDto) throws NotFoundException;

}