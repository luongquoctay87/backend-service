package vn.tayjava.service;

import vn.tayjava.controller.request.SignInRequest;
import vn.tayjava.controller.response.TokenResponse;

public interface AuthenticationService {

    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String request);
}
