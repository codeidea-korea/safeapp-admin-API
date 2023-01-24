package com.safeapp.admin.web.data;

import lombok.Getter;

@Getter
public enum CheckListProjectResultStatus {

    NONE(1);

    private final int code;

    CheckListProjectResultStatus(int code) {
        this.code = code;
    }

}