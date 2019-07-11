package com.hwl.beta.location;

public interface IHWLLoactionListener {
    void onSuccess(LocationModel model);

    void onFailure(String message);
}
