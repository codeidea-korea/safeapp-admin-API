package com.binoofactory.cornsqure.web.service.cmmn.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.binoofactory.cornsqure.web.service.cmmn.DirectSendAPIService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class DirectSendAPIServiceImpl implements DirectSendAPIService {

    @Value("${api.directsend.url.mail}")
    public String apiMailUri;

    @Value("${api.directsend.url.sms}")
    public String apiSmsUri;

    @Value("${api.directsend.phone}")
    public String senderPhone;

    @Value("${api.directsend.mail}")
    public String senderMailAddress;

    @Value("${api.directsend.id}")
    public String username;

    @Value("${api.directsend.key}")
    public String userkey;

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
        return json.getAsJsonObject();
    }

    @Override
    public boolean sendSMS(String phoneNo, Map<String, String> bodyMap)
        throws Exception {

        HttpURLConnection con = openConnection(apiSmsUri);

        String title = bodyMap.get("title");
        String message = bodyMap.get("message");
        // "[$NAME]님 알림 문자 입니다. 전화번호 : [$MOBILE] 비고1 : [$NOTE1] 비고2 : [$NOTE2] 비고3 : [$NOTE3] 비고4 : [$NOTE4] 비고5 : [$NOTE5] ";            //필수입력
        String sender = senderPhone; //필수입력
        String username = this.username; //필수입력
        String key = userkey; //필수입력

        //수신자 정보 추가 - 필수 입력(주소록 미사용시), 치환문자 미사용시 치환문자 데이터를 입력하지 않고 사용할수 있습니다.
        //치환문자 미사용시 {\"mobile\":\"01000000001\"} 번호만 입력 해주시기 바랍니다.
//        String receiver = "{\"name\": \"" + bodyMap.get("name") + "\", \"mobile\":\"" + bodyMap.get("phoneNo") + "\"}";
        String receiver = "{"
                + setJsonKeyValue("name", bodyMap.get("name")) + ", "
                + setJsonKeyValue("mobile", bodyMap.get("phoneNo"))
                + "}";
        receiver = "[" + receiver + "]";

        /* 여기까지만 수정해주시기 바랍니다. */
        String urlParameters = "\"title\":\"" + title + "\" "
            + ", \"message\":\"" + message + "\" "
            + ", \"sender\":\"" + sender + "\" "
            + ", \"username\":\"" + username + "\" "
            + ", \"receiver\":" + receiver
            + ", \"key\":\"" + key + "\" "
            + ", \"type\":\"" + "java" + "\" ";
        urlParameters = "{" + urlParameters + "}"; //JSON 데이터

        JsonObject response = getResponseByOpenConnection(con, urlParameters);
        /*
            status code
            0   : 정상발송 (성공코드는 다이렉트센드 DB서버에 정상수신됨을 뜻하며 발송성공(실패)의 결과는 발송완료 이후 확인 가능합니다.)
            100 : POST validation 실패
            101 : sender 유효한 번호가 아님
            102 : recipient 유효한 번호가 아님
            103 : 회원정보가 일치하지 않음
            104 : 받는 사람이 없습니다
            105 : message length = 0, message length >= 2000, title >= 20
            106 : message validation 실패
            107 : 이미지 업로드 실패
            108 : 이미지 갯수 초과
            109 : return_url이 유효하지 않습니다
            110 : 이미지 용량 300kb 초과
            111 : 이미지 확장자 오류
            112 : euckr 인코딩 에러 발생
            114 : 예약정보가 유효하지 않습니다.
            200 : 동일 예약시간으로는 200회 이상 API 호출을 할 수 없습니다.
            201 : 분당 300회 이상 API 호출을 할 수 없습니다.
            205 : 잔액부족
            999 : Internal Error.
         */
        if (response.get("status").getAsInt() != 0) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, response.get("msg").getAsString());
        }
        return true;
    }

    @Override
    public boolean sendMail(String mailAddress, Map<String, String> bodyMap) throws Exception {

        HttpURLConnection con = openConnection(apiMailUri);

        // 여기서부터 수정해주시기 바랍니다.
        String subject = bodyMap.get("subject"); //필수입력(템플릿 사용시 23 line 설명 참조)
        String body = bodyMap.get("body"); //필수입력, 템플릿 사용시 빈값을 입력 하시기 바랍니다. 예시) String body = "";
        String sender = this.senderMailAddress; //필수입력
        String sender_name = "컨스퀘어";
        String username = this.username; //필수입력 
        String key = userkey; //필수입력 

        //수신자 정보 추가 - 필수 입력(주소록 미사용시), 치환문자 미사용시 치환문자 데이터를 입력하지 않고 사용할수 있습니다.
        //치환문자 미사용시 {\"email\":\"aaaa@naver.com\"} 이메일만 입력 해주시기 바랍니다.
        String receiver = "{\"name\": \"" + bodyMap.get("name") + "\", \"email\":\"" + bodyMap.get("mailAddress")
            + "\"}";
        receiver = "[" + receiver + "]";

        /* 여기까지 수정해주시기 바랍니다. */
        String urlParameters = "\"subject\":\"" + subject + "\" "
            + ", \"body\":\"" + body + "\" "
            + ", \"sender\":\"" + sender + "\" "
            + ", \"sender_name\":\"" + sender_name + "\" "
            + ", \"username\":\"" + username + "\" "
            + ", \"receiver\":" + receiver
            + ", \"key\":\"" + key + "\" ";
        urlParameters = "{" + urlParameters + "}"; //JSON 데이터

        JsonObject response = getResponseByOpenConnection(con, urlParameters);
        /*
            status code
            1 : 성공
            100-1 : 파라미터 오류
            100-2 : 예약 목록 조회 오류
         */
        if (response.get("status").getAsInt() > 1) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, response.get("msg").getAsString());
        }
        return true;
    }

    private String setJsonKeyValue(String key, String value){
        return "\"" + key + "\": \"" + value + "\"";
    }
}
