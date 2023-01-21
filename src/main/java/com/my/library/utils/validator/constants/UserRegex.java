package com.my.library.utils.validator.constants;

public enum UserRegex {
    LOGIN("^\\w{3,30}$"),
    PASSWORD("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$"), //Minimum 5 characters, at least one letter and one number, no special chars
    EMAIL("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"),
    NAME("");

    private final String regex;

    UserRegex(String regex){
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
