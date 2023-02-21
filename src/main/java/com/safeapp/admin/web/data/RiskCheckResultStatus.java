package com.safeapp.admin.web.data;

import lombok.Getter;

@Getter
public enum RiskCheckResultStatus {

    NONE(1);

    private final int code;

    RiskCheckResultStatus(int code) {
        this.code = code;
    }

}