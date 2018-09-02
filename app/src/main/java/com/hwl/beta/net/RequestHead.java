package com.hwl.beta.net;

/**
 * Created by Administrator on 2018/1/14.
 */

public class RequestHead {
    public String Version;
    public String Timestamp;
    public String Token;
    public String ClientIp;
    public String SessionId;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getClientIp() {
        return ClientIp;
    }

    public void setClientIp(String clientIp) {
        ClientIp = clientIp;
    }

    public String getSessionId() {
        return SessionId;
    }

    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }
}
