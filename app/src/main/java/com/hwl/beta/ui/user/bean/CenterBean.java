package com.hwl.beta.ui.user.bean;


/**
 * Created by Administrator on 2018/4/1.
 */

public class CenterBean {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
//        notifyPropertyChanged(BR.symbol);
    }
}
