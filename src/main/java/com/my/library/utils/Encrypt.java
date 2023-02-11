package com.my.library.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Encrypt {
    public static String encryptWithSha512Hex(String password) {
        return DigestUtils.sha512Hex(password);
    }
}
