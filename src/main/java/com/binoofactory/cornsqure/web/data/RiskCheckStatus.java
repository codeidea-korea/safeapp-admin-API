package com.binoofactory.cornsqure.web.data;

import lombok.Getter;

@Getter
public enum RiskCheckStatus {
    NONE(1);

    private final int code;

    RiskCheckStatus(int code) {
        this.code = code;
    }
}
