package com.my.library.services.impl;

import com.my.library.dao.UserDAO;
import com.my.library.dao.impl.UserDaoImpl;
import org.apache.commons.codec.digest.DigestUtils;

public class UserService {
    private UserDAO userDAO = UserDaoImpl.getInstance();

    UserService() {

    }

//    public static void main(String[] args) {
//        String sha256hex = DigestUtils.sha512Hex("admin");
//        System.out.println(sha256hex);
//    }
}
