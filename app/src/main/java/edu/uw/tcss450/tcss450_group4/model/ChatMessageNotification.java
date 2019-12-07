package edu.uw.tcss450.tcss450_group4.model;

import java.io.Serializable;

/**
 * Notification object that is used to notify fragments of new messages received
 * @author Abraham Lee abe2016@uw.edu
 */
public class ChatMessageNotification implements Serializable {

    private final String mMessage;
    private final String mSender;
    private final String mChatId;


    /**
     * Helper class for building ChatMessageNotification.
     *
     */
    public static class Builder {
        private final String message;
        private final String sender;
        private final String chatId;


        /**
         * Constructs a new Builder.
         *
         * No validation is performed.
         *
         * @param sender the sender
         * @param message the message
         * @param chatId the chatId
         */
        public Builder(String sender, String message, String chatId) {
            this.message = message;
            this.sender = sender;
            this.chatId = chatId;
        }

        /**
         * Builds notification object
         * @return ChatMessageNotification
         */
        public ChatMessageNotification build() {
            return new ChatMessageNotification(this);
        }
    }

    /**
     * Construct a ChatMessageNotification internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    private ChatMessageNotification(final Builder builder) {
        mMessage = builder.message;
        mSender = builder.sender;
        mChatId = builder.chatId;
    }

    /**
     * Gets message
     * @return message
     */
    public String getMessage() {
        return mMessage;
    }

    /**
     * Gets sender
     * @return sender
     */
    public String getSender() {
        return mSender;
    }

    /**
     * Gets chatId
     * @return chatId
     */
    public String getChatId() { return mChatId; }
}
