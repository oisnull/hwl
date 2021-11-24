package com.hwl.beta.ui.user.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Administrator on 2018/4/1.
 */

public class CenterBean extends BaseObservable {
    private String headImage;
    private String name;
    private String symbol;

    public CenterBean() {
    }

    public CenterBean(String headImage, String name, String symbol) {
        this.headImage = headImage;
        this.name = name;
        this.symbol = symbol;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
//        notifyPropertyChanged(BR.symbol);
    }
}
