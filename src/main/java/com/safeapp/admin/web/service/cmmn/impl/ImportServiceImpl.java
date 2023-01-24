package com.safeapp.admin.web.service.cmmn.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import com.safeapp.admin.utils.DateUtil;
import com.safeapp.admin.web.data.YN;
import com.safeapp.admin.web.model.entity.Auth;
import com.safeapp.admin.web.model.entity.ImportPayment;
import com.safeapp.admin.web.model.entity.UserAuth;
import com.safeapp.admin.web.model.entity.Users;
import com.safeapp.admin.web.repos.jpa.AuthRepos;
import com.safeapp.admin.web.repos.jpa.ImportPaymentRepos;
import com.safeapp.admin.web.repos.jpa.UserRepos;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import com.safeapp.admin.web.service.UserAuthService;
import com.safeapp.admin.web.service.cmmn.ImportService;
import com.safeapp.admin.web.service.cmmn.JwtService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ImportServiceImpl implements ImportService {

    private final UserAuthService userAuthService;

    private final JwtService jwtService;

    private final ImportPaymentRepos repos;

    private final UserRepos userRepos;

    private final AuthRepos authRepos;

    private final DateUtil dateUtil;

    @Value("${api.import.store-id}")
    public String storeId;

    @Value("${api.import.rest.key}")
    public String restKey;

    @Value("${api.import.rest.secret}")
    public String restSecret;

    @Value("${api.import.token.url}")
    public String tokenUrl;

    @Value("${api.import.payment.url}")
    public String paymentUrl;

    @Value("${api.import.redirect.url}")
    public String redirectUrl;

    @Autowired
    public ImportServiceImpl(UserAuthService userAuthService, JwtService jwtService,
                             ImportPaymentRepos repos, AuthRepos authRepos, UserRepos userRepos, DateUtil dateUtil) {

        this.userAuthService = userAuthService;
        this.jwtService = jwtService;
        this.repos = repos;
        this.authRepos = authRepos;
        this.userRepos = userRepos;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public String generateOrderNumber(ImportPayment payment, HttpServletRequest httpServletRequest) throws Exception {
        // 고객 주문번호 생성(채번)
        Users loginUser = jwtService.getUserInfoByToken(httpServletRequest);

        payment.setCustomerUid(loginUser.getUserId());
        payment.setMerchantUid(
            "consqure-" + dateUtil.getDatetimeDetail() + "-" + loginUser.getId());
        // 비회원 결제 불가
        if (payment.getAmount() < 0 || StringUtils.isEmpty(payment.getCustomerUid())
            || StringUtils.isEmpty(payment.getName())) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "상품명, 가격, 비회원일 경우 구매가 불가능합니다.");
        }
        payment.setCashReceiptIssued(YN.N);
        payment.setEscrow(YN.N);

        repos.save(payment);

        return payment.getMerchantUid();
    }

    @Transactional
    @Override
    public String callBackImportPayment(String imp_uid, String merchant_uid) throws Exception {

        // 결제 승인 (결제 수신    ->> req.body -> (imp_uid, merchant_uid)
        String token;
        {
            // 토큰 가져오기
            HttpURLConnection con = openConnection(tokenUrl);
            con.setRequestMethod("POST");
            String body = "{\"imp_key\": \"" + restKey + "\", \"imp_secret\":\"" + restSecret + "\"}";
            Gson gson = new Gson();
            JsonObject response = getResponseByOpenConnection(con, body);
            token = response.get("access_token").getAsString();
        }
        String redirectUri = this.redirectUrl;
        {
            // 결제 내역 가져오기
            HttpURLConnection con = openConnection(paymentUrl + imp_uid);
            con.setRequestProperty("Authorization", token);
            con.setRequestMethod("GET");
            JsonObject response = getResponseByOpenConnection(con, "");

            // 결제 내역 저장
            ImportPayment importPayment = repos.findByMerchantUid(merchant_uid);

            importPayment.setApplyNum(JsonElemnetNullCheck(response.get("apply_num")));
            importPayment.setBankCode(JsonElemnetNullCheck(response.get("bank_code")));
            importPayment.setBankName(JsonElemnetNullCheck(response.get("bank_name")));
            importPayment.setBuyerAddr(JsonElemnetNullCheck(response.get("buyer_addr")));
            importPayment.setBuyerEmail(JsonElemnetNullCheck(response.get("buyer_email")));
            importPayment.setBuyerName(JsonElemnetNullCheck(response.get("buyer_name")));
            importPayment.setBuyerPostcode(JsonElemnetNullCheck(response.get("buyer_postcode")));
            importPayment.setBuyerTel(JsonElemnetNullCheck(response.get("buyer_tel")));
            importPayment.setCancelAmount(response.get("cancel_amount").getAsInt());
            importPayment.setCancelledAt(JsonElemnetNullCheck(response.get("cancelled_at")));
            importPayment.setCancelReason(JsonElemnetNullCheck(response.get("cancel_reason")));
            importPayment.setCardCode(JsonElemnetNullCheck(response.get("card_code")));
            importPayment.setCardName(JsonElemnetNullCheck(response.get("card_name")));
            importPayment.setCardNumber(JsonElemnetNullCheck(response.get("card_number")));
            importPayment.setCardQuota(response.get("card_quota").getAsInt());
            importPayment.setCardType(JsonElemnetNullCheck(response.get("card_type")));
            importPayment.setCashReceiptIssued(response.get("cash_receipt_issued").getAsBoolean() ? YN.Y : YN.N);
            importPayment.setChannel(JsonElemnetNullCheck(response.get("channel")));
            importPayment.setCurrency(JsonElemnetNullCheck(response.get("currency")));
            importPayment.setCustomData(JsonElemnetNullCheck(response.get("custom_data")));
            importPayment.setEmbPgProvider(JsonElemnetNullCheck(response.get("emb_pg_provider")));
            importPayment.setEscrow(response.get("escrow").getAsBoolean() ? YN.Y : YN.N);
            importPayment.setFailedAt(response.get("failed_at").getAsInt());
            importPayment.setFailReason(JsonElemnetNullCheck(response.get("fail_reason")));
            importPayment.setImpUid(JsonElemnetNullCheck(response.get("imp_uid")));
            importPayment.setName(JsonElemnetNullCheck(response.get("name")));
            importPayment.setPaidAt(response.get("paid_at").getAsInt());
            importPayment.setPayMethod(JsonElemnetNullCheck(response.get("pay_method")));
            importPayment.setPgId(JsonElemnetNullCheck(response.get("pg_id")));
            importPayment.setPgProvider(JsonElemnetNullCheck(response.get("pg_provider")));
            importPayment.setPgTid(JsonElemnetNullCheck(response.get("pg_tid")));
            importPayment.setReceiptUrl(JsonElemnetNullCheck(response.get("receipt_url")));
            importPayment.setStartedAt(response.get("started_at").getAsInt());
            importPayment.setStatus(JsonElemnetNullCheck(response.get("status")));
            importPayment.setUserAgent(JsonElemnetNullCheck(response.get("user_agent")));
            importPayment.setVbankCode(JsonElemnetNullCheck(response.get("vbank_code")));
            importPayment.setVbankDate(response.get("vbank_date").getAsInt());
            importPayment.setVbankHolder(JsonElemnetNullCheck(response.get("vbank_holder")));
            importPayment.setVbankIssuedAt(response.get("vbank_issued_at").getAsInt());
            importPayment.setVbankName(JsonElemnetNullCheck(response.get("vbank_name")));
            importPayment.setVbankNum(JsonElemnetNullCheck(response.get("vbank_num")));

            repos.save(importPayment);

            // 결제 정보 검증 --> 결제자, 가격, status = paid
            if (!StringUtils.equals(importPayment.getCustomerUid(), JsonElemnetNullCheck(response.get("customer_uid")))
                || importPayment.getAmount() != response.get("amount").getAsInt()) {

                redirectUri = redirectUri + "?success=false&msg=결제 정보가 일치하지 않습니다. - 고객 식별번호/가격";
                //                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "결제 정보가 일치하지 않습니다. - 고객 식별번호/가격");
                return redirectUri;
            }
            if (!"paid".equals(importPayment.getStatus())) {
                redirectUri = redirectUri + "?success=false&msg=결제가 되지 않았습니다.";
                //                throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "결제가 되지 않았습니다.");
                return redirectUri;
            }
        }
        {
            // 결제 정보 검증 --> 결제자, 가격, 등급(상품), status = paid
            ImportPayment importPayment = repos.findByMerchantUid(merchant_uid);

            // 신규 가입 상품
            Auth newAuth = authRepos.findByName(importPayment.getName());
            //Auth newAuth = authRepos.findByName("test상품");
            // 현재 유효한 권한
            UserAuth efectiveAuth = userAuthService.getEfectiveAuthByUserId(importPayment.getCustomerUid());
            LocalDateTime now = dateUtil.getThisTime();
            Users user = userRepos.findByUserId(importPayment.getCustomerUid());

            if (efectiveAuth == null) {
                // 유효값이 없으면 신규 권한으로 적용

                UserAuth userAuth = UserAuth.builder()
                        .userId(user.getId())
                        .authId(newAuth.getId())
                        .createdAt(now)
                        .efectiveEndAt(now.plusDays(30))
                        .efectiveStartAt(now)
                        .paymentWhat(1)
                        .price(importPayment.getAmount())
                        .build();

                userAuth.setUser(userRepos.findById(user.getId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + user.getId())));

                userAuthService.add(userAuth, null);
            } else {
                // 현재 유효상품의 유효기간이 남았을 경우, 일단 더하기 해준다.

                UserAuth userAuth = UserAuth.builder()
                        .id(efectiveAuth.getId())
                        .userId(user.getId())
                        .authId(newAuth.getId())
                        .createdAt(now)
                        .efectiveEndAt(efectiveAuth.getEfectiveEndAt().plusDays(30))
                        .efectiveStartAt(efectiveAuth.getEfectiveStartAt())
                        .paymentWhat(1)
                        .price(importPayment.getAmount())
                        .build();

                userAuth.setUser(userRepos.findById(user.getId()).orElseThrow(() -> new NotFoundException("user does not exist. input user id: " + user.getId())));

                userAuthService.edit(userAuth, null);
            }
            redirectUri = redirectUri + "?success=true&msg=결제성공";
        }

        return redirectUri;
    }

    private HttpURLConnection openConnection(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Cache-Control", "no-cache");
        con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        con.setRequestProperty("Accept", "application/json");

        return con;
    }

    private JsonObject getResponseByOpenConnection(HttpURLConnection con, String urlParameters) throws Exception {
        System.setProperty("jsse.enableSNIExtension", "false");
        con.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        wr.write(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new Exception(con.getResponseMessage());
        }
        java.io.BufferedReader in = new java.io.BufferedReader(
            new java.io.InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        con.disconnect();

        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(response.toString());
        return json.getAsJsonObject().get("response").getAsJsonObject();
    }

    public String JsonElemnetNullCheck(JsonElement ele){
        if(ele == null){
            return "";
        }

        if("null".equals(ele)){
            return "";
        }

        if(ele.isJsonNull()){
            return "";
        }

        return ele.getAsString();
    }
}
