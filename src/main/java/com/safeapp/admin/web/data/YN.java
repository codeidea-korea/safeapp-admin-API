package com.safeapp.admin.web.data;

public enum YN {

    Y, N;
    
    public static YN convertBoolean2YN(boolean is) {
        return is ? YN.Y : YN.N;
    }

}