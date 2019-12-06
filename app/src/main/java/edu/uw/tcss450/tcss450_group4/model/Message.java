package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Serializable, Parcelable {
    private String mMessage;
    private String mMemberId;
    private String mUsername;
    private String mTimeStamp;
//    private String mUserName;
//    private long mCreatedAt;
//    private String mProfileUrl;

    protected Message(Parcel in) {
        mMessage = in.readString();
        mUsername = in.readString();
        mTimeStamp = in.readString();
        mMemberId = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessage);
        dest.writeString(mUsername);
        dest.writeString(mTimeStamp);
        dest.writeString(mMemberId);
    }


    public static class Builder {
        private final String mEmail;
        private final String mMessage;
        private final String mTimeStamp;
        private final String mMemberId;
        public Builder(String chatId, String memberId, String message, String timeStamp) {
            this.mEmail = chatId;
            this.mMessage = message;
            this.mTimeStamp = timeStamp;
            this.mMemberId = memberId;
        }

        public Message build() {
            return new Message (this);
        }
    }

    private Message(final Builder builder) {
        this.mUsername = builder.mEmail;
        this.mMessage = builder.mMessage;
        this.mTimeStamp = builder.mTimeStamp;
        this.mMemberId = builder.mMemberId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }


    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String userName) {
        this.mUsername = userName;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.mTimeStamp = timeStamp;
    }

    public String getMemberId() {
        return mMemberId;
    }
}