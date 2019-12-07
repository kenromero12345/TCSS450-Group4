package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * This class contains the message object used to display all the messages and the information of sender.
 * Created by Chinh Le on 11/1/2019.
 *
 * @author Chinh Le
 * @version Nov 1 2019
 */
public class Message implements Serializable, Parcelable {
    private String mMessage;
    private int mMemberId;
    private String mUsername;
    private String mTimeStamp;

    private String mProfileUri;

    protected Message(Parcel in) {
        mMessage = in.readString();
        mUsername = in.readString();
        mTimeStamp = in.readString();
        mMemberId = in.readInt();
        mProfileUri = in.readString();
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
        dest.writeInt(mMemberId);
        dest.writeString(mProfileUri);
    }


    public static class Builder {
        private final String mEmail;
        private final String mMessage;
        private final String mTimeStamp;
        private final int mMemberId;
        private final String mProfileUri;
        public Builder(String chatId, int memberId, String message, String timeStamp, String profileUri) {
            this.mEmail = chatId;
            this.mMessage = message;
            this.mTimeStamp = timeStamp;
            this.mMemberId = memberId;
            this.mProfileUri = profileUri;
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
        this.mProfileUri = builder.mProfileUri;
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

    public int getMemberId() {
        return mMemberId;
    }

    public String getProfileUri() { return mProfileUri; }
}