package edu.uw.tcss450.tcss450_group4.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Chat implements Serializable, Parcelable {

    private final String mChatName;
    private final String mMostRecentMessage;

    protected Chat(Parcel in) {
        mChatName = in.readString();
        mMostRecentMessage = in.readString();
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
        dest.writeString(mChatName);
        dest.writeString(mMostRecentMessage);
    }

    public static class Builder {
        private final String mChatName;
        private final String mMostRecentMessage;

        public Builder(String chatName, String mostRecentMessage) {
            this.mChatName = chatName;
            this.mMostRecentMessage = mostRecentMessage;
        }

        public Chat build() {
            return new Chat (this);
        }
    }

    private Chat(final Builder builder) {
        this.mChatName = builder.mChatName;
        this.mMostRecentMessage = builder.mMostRecentMessage;
    }

    public String getChatName() { return mChatName; }

    public String getMostRecentMessage() { return mMostRecentMessage; }
}
