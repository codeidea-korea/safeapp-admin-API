package com.binoofactory.cornsqure.utils;

import java.nio.charset.Charset;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class PaymentUserKeyUtil {
    
    public class UserKey {
        public long userSeq; 
        public String userMail;
        
        public UserKey(long userSeq, String userMail) {
            this.userSeq = userSeq;
            this.userMail = userMail;
        }
    }
    
    public String encode(long userSeq, String userMail) {
        StringBuilder stb = new StringBuilder();
        stb.append(userSeq);
        stb.append("|");
        stb.append(userMail);
        
        return Base64.getEncoder().encodeToString(stb.toString().getBytes());
    }
    
    public UserKey decode(String userKey) {
        byte[] decodeBytes = Base64.getDecoder().decode(userKey);
        String decodeKey = new String(decodeBytes, Charset.defaultCharset());
        String[] keys = decodeKey.split("|");
        
        return new UserKey(Long.parseLong(keys[0]), keys[1]);
    }
}
