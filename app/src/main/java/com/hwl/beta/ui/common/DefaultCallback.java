package com.hwl.beta.ui.common;

public abstract class DefaultCallback<TSuccess, TError> {

    public abstract void success(TSuccess successMessage);

    public abstract void error(TError errorMessage);

    public void relogin() {

    }
}
