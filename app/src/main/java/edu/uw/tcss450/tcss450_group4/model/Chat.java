package edu.uw.tcss450.tcss450_group4.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Chat implements Serializable, Parcelable {
    private final String mChatId;
    private final String mChatName;
    private final String mMostRecentMessage;
    private final String mTimeStamp;

    protected Chat(Parcel in) {
        mChatId = in.readString();
        mChatName = in.readString();
        mMostRecentMessage = in.readString();
        mTimeStamp = in.readString();
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mChatId);
        dest.writeString(mChatName);
        dest.writeString(mMostRecentMessage);
        dest.writeString(mTimeStamp);
    }

    public static class Builder {
        private final String mChatId;
        private final String mChatName;
        private final String mMostRecentMessage;
        private final String mTimeStamp;

        public Builder(String chatId, String chatName, String mostRecentMessage, String timeStamp) {
            this.mChatId = chatId;
            this.mChatName = chatName;
            this.mMostRecentMessage = mostRecentMessage;
            this.mTimeStamp = timeStamp;
        }

        public Chat build() {
            return new Chat (this);
        }
    }

    private Chat(final Builder builder) {
        this.mChatId = builder.mChatId;
        this.mChatName = builder.mChatName;
        this.mMostRecentMessage = builder.mMostRecentMessage;
        this.mTimeStamp = builder.mTimeStamp;
    }

    public String getChatId() { return mChatId; }

    public String getChatName() { return mChatName; }

    public String getMostRecentMessage() { return mMostRecentMessage; }

    public String getTimeStamp() { return mTimeStamp; }
}
