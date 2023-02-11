package com.my.library.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class Encrypt {
    private static String encryptWithSha512Hex(String password) {
        return DigestUtils.sha512Hex(password);
    }
}
