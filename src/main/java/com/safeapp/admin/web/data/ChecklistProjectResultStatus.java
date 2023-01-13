package com.safeapp.admin.web.data;

import lombok.Getter;

@Getter
public enum ChecklistProjectResultStatus {
    NONE(1);

    private final int code;

    ChecklistProjectResultStatus(int code) {
        this.code = code;
    }
}
