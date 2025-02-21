package com.example.eroom.domain.auth.security;

public class OAuth2RedirectException extends RuntimeException {
    private final String redirectUrl;

    public OAuth2RedirectException(String redirectUrl) {
        super("OAuth2 Redirect Exception");
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
