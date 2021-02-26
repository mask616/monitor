package com.tceasy.monitor.notifier;

import java.util.List;

public class TextMessage {

    private String msgtype;
    private TextBean text;
    private AtBean at;

    public static AtBean buildAtBean(List<String> atMobiles, boolean isAtAll) {
        return new AtBean(isAtAll, atMobiles);
    }

    public static TextBean buildTextBean(String content) {
        return new TextMessage.TextBean(content);
    }

    public String getMsgtype() {
        return this.msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public TextBean getText() {
        return this.text;
    }

    public void setText(TextMessage.TextBean text) {
        this.text = text;
    }

    public AtBean getAt() {
        return this.at;
    }

    public void setAt(TextMessage.AtBean at) {
        this.at = at;
    }

    public static TextMessageBuilder builder() {
        return new TextMessage.TextMessageBuilder();
    }


    public String toString() {
        return "TextMessage(msgtype=" + this.getMsgtype() + ", text=" + this.getText() + ", at=" + this.getAt() + ")";
    }

    public TextMessage(final String msgtype, final TextBean text, final AtBean at) {
        this.msgtype = msgtype;
        this.text = text;
        this.at = at;
    }

    public static class TextMessageBuilder {
        private String msgtype;
        private TextBean text;
        private AtBean at;

        TextMessageBuilder() {
        }

        public TextMessageBuilder msgtype(final String msgtype) {
            this.msgtype = msgtype;
            return this;
        }

        public TextMessageBuilder text(final TextBean text) {
            this.text = text;
            return this;
        }

        public TextMessageBuilder at(final AtBean at) {
            this.at = at;
            return this;
        }

        public TextMessage build() {
            return new TextMessage(this.msgtype, this.text, this.at);
        }

        public String toString() {
            return "TextMessage.TextMessageBuilder(msgtype=" + this.msgtype + ", text=" + this.text + ", at=" + this.at + ")";
        }
    }

    public static class AtBean {
        private boolean isAtAll;
        private List<String> atMobiles;

        public AtBean(boolean isAtAll, List<String> atMobiles) {
            this.isAtAll = isAtAll;
            this.atMobiles = atMobiles;
        }

        public boolean isIsAtAll() {
            return this.isAtAll;
        }

        public void setIsAtAll(boolean isAtAll) {
            this.isAtAll = isAtAll;
        }

        public List<String> getAtMobiles() {
            return this.atMobiles;
        }

        public void setAtMobiles(List<String> atMobiles) {
            this.atMobiles = atMobiles;
        }

        @Override
        public String toString() {
            return "AtBean{" +
                    "isAtAll=" + isAtAll +
                    ", atMobiles=" + atMobiles +
                    '}';
        }
    }

    public static class TextBean {
        private String content;

        public TextBean(String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "TextBean{" +
                    "content='" + content + '\'' +
                    '}';
        }
    }
}
