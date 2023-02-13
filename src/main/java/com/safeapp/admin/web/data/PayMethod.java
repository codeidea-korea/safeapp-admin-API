package com.safeapp.admin.web.data;

public enum PayMethod {

    card("신용카드"),
    vbank("가상계좌"),
    trans("실시간계좌이체"),
    phone("휴대폰소액결제"),
    samsung("삼성페이"),
    kpay("kpay앱 직접호출"),
    kakaopay("카카오페이 직접호출"),
    payco("페이코 직접호출"),
    lpay("LPAY 직접호출"),
    ssgpay("ssg페이 직접호출"),
    tospay("토스간편결제 직접호출"),
    cultureland("문화상품권"),
    smartculture("스마트문상"),
    happymoney("해피머니"),
    booknlife("도서상품권"),
    point("베네피아 포인트 등 포인트 결제"),
    wechat("위쳇 페이"),
    alipay("알리페이"),
    unionpay("유니온페이"),
    tenpay("텐페이");

    private String name;

    PayMethod(String name) {
        this.name = name;
    }

}