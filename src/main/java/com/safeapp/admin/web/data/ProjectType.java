package com.safeapp.admin.web.data;

import lombok.Getter;

@Getter
public enum ProjectType {
    NONE(1);

    private final int code;

    ProjectType(int code) {
        this.code = code;
    }
}
