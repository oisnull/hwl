package com.hwl.beta.emotion.model;

import java.util.List;

public class EmojiPageModel {
//    private int line;
//    private int row;
	private boolean isShowTitle;
    private List<EmojiModel> emojis;
    private boolean lastItemIsDeleteButton;

//    public int getLine() {
//        return line;
//    }
//
//    public void setLine(int line) {
//        this.line = line;
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public void setRow(int row) {
//        this.row = row;
//    }

    public boolean getShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public List<EmojiModel> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<EmojiModel> emojis) {
        this.emojis = emojis;
    }

    public boolean isLastItemIsDeleteButton() {
        return lastItemIsDeleteButton;
    }

    public void setLastItemIsDeleteButton(boolean lastItemIsDeleteButton) {
        this.lastItemIsDeleteButton = lastItemIsDeleteButton;
    }
}