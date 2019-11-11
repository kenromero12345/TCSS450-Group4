package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ConnectionItem implements Serializable, Parcelable {

    private final String mName;
    private final String mUserName;

    protected ConnectionItem(Parcel in) {
        mName = in.readString();
        mUserName = in.readString();
    }

    public static final Creator<ConnectionItem> CREATOR = new Creator<ConnectionItem>() {


        @Override
        public ConnectionItem createFromParcel(Parcel in) {
            return new ConnectionItem(in);
        }

        @Override
        public ConnectionItem[] newArray(int size) {
            return new ConnectionItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mUserName);
    }

    public static class Builder {
        private String mName = "";
        private String mUserName = "";

        public Builder(String name, String userName){
            mName = name;
            mUserName = userName;
        }

        public ConnectionItem build() {return new ConnectionItem(this); }


    }

    private ConnectionItem(final ConnectionItem.Builder builder){
        this.mName = builder.mName;
        this.mUserName = builder.mUserName;
    }

    public String getFirstName(){
        return mName;
    }

    public String getUserName(){
        return mUserName;
    }
}

