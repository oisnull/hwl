package com.hwl.beta.emotion.model;

public class EmojiModel {
    public int source;
    public int res;
    public String key;

    public EmojiModel(String key, int res) {
        this.res = res;
        this.key = key;
    }
}