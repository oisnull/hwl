package com.hwl.beta.emotion.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmojiPageContainer {
    private String id;
	private int defaultResId;
    private int line;
    private int row;
    private int pageCount = 0;
    private boolean lastItemIsDeleteButton;
    private List<EmojiPageModel> emojiPages;

    public EmojiPageContainer(Builder builder) {
        this.pageCount = builder.pageCount;
        this.lastItemIsDeleteButton = builder.lastItemIsDeleteButton;
        this.emojiPages = builder.emojiPages;
        this.line = builder.line;
        this.row = builder.row;
        this.defaultResId = builder.defaultResId;
        this.id = UUID.randomUUID().toString();
    }

    public int getDefaultResId() {
        return defaultResId;
    }

    public int getRow() {
        return row;
    }

    public int getLine() {
        return line;
    }

    public String getId() {
        return id;
    }

    public int getPageCount() {
        return pageCount;
    }

    public boolean lastItemIsDeleteButton() {
        return lastItemIsDeleteButton;
    }

    public boolean IsShowIndicator() {
        return pageCount > 1;
    }

    public List<EmojiPageModel> getEmojiPages() {
        return emojiPages;
    }

    public static class Builder {
		private int defaultResId;
        private int line = 7;
        private int row = 3;
        private boolean lastItemIsDeleteButton = true;
        private boolean isShowTitle = false;
        private int pageCount = 0;
        private List<EmojiModel> allEmojis;
        private List<EmojiPageModel> emojiPages = new ArrayList<>();

        public Builder setAllEmojis(List<EmojiModel> allEmojis) {
            this.allEmojis = allEmojis;
            return this;
        }

        public Builder setLastItemIsDeleteButton(boolean lastItemIsDeleteButton) {
            this.lastItemIsDeleteButton = lastItemIsDeleteButton;
            return this;
        }

        public Builder setDefaultResId(int defaultResId) {
            this.defaultResId = defaultResId;
            return this;
        }

        public Builder setLine(int line) {
            this.line = line;
            return this;
        }

        public Builder setRow(int row) {
            this.row = row;
            return this;
        }

        public Builder setShowTitle(boolean isShowTitle) {
            this.isShowTitle = isShowTitle;
            return this;
        }

        public EmojiPageContainer build() {
            if (allEmojis == null || allEmojis.size() <= 0)
                return new EmojiPageContainer(this);

            int emojiCount = allEmojis.size();
            int maxCount = row * line - (lastItemIsDeleteButton ? 1 : 0);
            int start = 0;
            int end = maxCount > emojiCount ? emojiCount : maxCount;

            emojiPages.clear();
            pageCount = (int) Math.ceil((double) emojiCount / maxCount);
            for (int i = 0; i < pageCount; i++) {
                EmojiPageModel model = new EmojiPageModel();
//                model.setLine(line);
//                model.setRow(row);
                model.setShowTitle(isShowTitle);
                model.setEmojis(allEmojis.subList(start, end));
                model.setLastItemIsDeleteButton(lastItemIsDeleteButton);
                emojiPages.add(model);

                start = maxCount + i * maxCount;
                end = maxCount + (i + 1) * maxCount;
                if (end >= emojiCount) {
                    end = emojiCount;
                }
            }
            return new EmojiPageContainer(this);
        }
    }
}