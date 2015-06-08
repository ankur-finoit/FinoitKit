package com.finoit.net;

@SuppressWarnings("serial")
public class HttpResponseException extends Exception {
    private String mResponseMessage;
    private int mResponseCode;

    public HttpResponseException(int code, String message) {
        super(String.valueOf(code) + ": " + message);
        mResponseCode = code;
        mResponseMessage = message;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }
    
}
