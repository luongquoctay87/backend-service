package vn.tayjava.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    private String username;
    private String password;
    private String platform; // web, mobile, tablet
    private String deviceToken; // for push notify
    private String versionApp;
}
