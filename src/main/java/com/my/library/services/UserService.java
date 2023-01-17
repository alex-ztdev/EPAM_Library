package com.my.library.services;

import org.apache.commons.codec.digest.DigestUtils;

public class UserService {
    public static void main(String[] args) {
        String sha256hex = DigestUtils.sha512Hex("originalString");
        System.out.println(sha256hex);
    }
}
