package com.ccz.modules.common.action;

import com.ccz.modules.common.utils.Crypto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SCodeManager {

    private static Map<String, String> scodeToBase64 = new ConcurrentHashMap<>();
    private static Map<String, String> base64ToScode = new ConcurrentHashMap<>();

    public static String getBase64(String scode) {
        if(scodeToBase64.containsKey(scode))
            return scodeToBase64.get(scode);
        String base64 = Crypto.RC4Cipher.getInst().encStr(scode);
        scodeToBase64.put(scode, base64);
        base64ToScode.put(base64, scode);
        return base64;
    }

    public static String getScode(String base64) {
        if(base64ToScode.containsKey(base64))
            return base64ToScode.get(base64);
        String scode = Crypto.RC4Cipher.getInst().decStr(base64);
        scodeToBase64.put(scode, base64);
        base64ToScode.put(base64, scode);
        return scode;
    }

}
