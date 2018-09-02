package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */

public class SetUserSymbolRequest {
    private long UserId;
    private String Symbol;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }
}
