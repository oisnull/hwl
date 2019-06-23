package com.hwl.beta.emotion.model;

public class EmojiModel {
    public int source;
    public int res;
    public String key;
    public String title;

    public EmojiModel(String key, int res) {
        this.res = res;
        this.key = key;
    }

    public EmojiModel(String key, int res, String title) {
        this.res = res;
        this.key = key;
        this.title = title;
    }

    public EmojiModel(int source, String key, int res, String title) {
        this.source = source;
        this.res = res;
        this.key = key;
        this.title = title;
    }
}