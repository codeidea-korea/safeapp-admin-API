package com.safeapp.admin.conf.handler;

import com.safeapp.admin.web.model.cmmn.Pages;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableHandlerInArgument implements HandlerMethodArgumentResolver {

    private static final int MIN_OFFSET_VALUE = 1;
    private static final int MIN_SIZE_VALUE = 1;
    private static final int MAX_OFFSET_VALUE = Integer.MAX_VALUE;
    private static final int MAX_SIZE_VALUE = 1000;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return Pages.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String offsetString = webRequest.getParameter("pageNo");
        String limitString = webRequest.getParameter("pageSize");

        int offset = 1;
        int limit = 10;

        if(!StringUtils.isEmpty(offsetString) && StringUtils.isNumeric(offsetString)) {
            int tmpOffset = Integer.valueOf(offsetString);
            if(MIN_OFFSET_VALUE <= tmpOffset && tmpOffset <= MAX_OFFSET_VALUE) {

                offset = tmpOffset;
            }
        }
        if(!StringUtils.isEmpty(limitString) && StringUtils.isNumeric(limitString)) {
            int tmpLimit = Integer.valueOf(limitString);
            if(MIN_SIZE_VALUE <= tmpLimit && tmpLimit <= MAX_SIZE_VALUE) {

                limit = tmpLimit;
            }
        }

        return new Pages(offset, limit);
    }

}