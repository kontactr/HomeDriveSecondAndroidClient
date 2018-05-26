package com.example.yash.homedrivesecond.Login;

/**
 * Created by Yash on 04-02-2018.
 */

public  class LoginBean {

    private String Key;
    private String Value;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public LoginBean(String key, String value) {

        Key = key;
        Value = value;
    }
}
