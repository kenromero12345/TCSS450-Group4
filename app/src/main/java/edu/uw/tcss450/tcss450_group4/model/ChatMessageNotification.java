package edu.uw.tcss450.tcss450_group4.model;

import java.io.Serializable;

public class ChatMessageNotification implements Serializable {

    private final String mMessage;
    private final String mSender;
    private final String mChatId;


    public static class Builder {
        private final String message;
        private final String sender;
        private final String chatId;

        public Builder(String sender, String message, String chatId) {
            this.message = message;
            this.sender = sender;
            this.chatId = chatId;
        }

        public ChatMessageNotification build() {
            return new ChatMessageNotification(this);
        }
    }

    private ChatMessageNotification(final Builder builder) {
        mMessage = builder.message;
        mSender = builder.sender;
        mChatId = builder.chatId;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSender() {
        return mSender;
    }

    public String getChatId() { return mChatId; }
}
