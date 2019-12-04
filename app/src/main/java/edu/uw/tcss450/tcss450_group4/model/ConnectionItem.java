package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ConnectionItem implements Serializable, Parcelable {


    private final String mContactId;
    private final String mContactName;
    private final String mContactLastName;
    private final String mContactUserName;
    private final String mContactImage;
    private int mVerified;

    public ConnectionItem(int tContactId, String tContactName, String tContactLastName, String tContactUsername,
                          String tContactImage){
        mContactId = Integer.toString(tContactId);
        mContactName = tContactName;
        mContactLastName = tContactLastName;
        mContactUserName = tContactUsername;
        mContactImage = tContactImage;


    }

    public ConnectionItem(int tContactId, String tContactName, String tContactLastName, String tContactUsername,
                          int tVerfied, String tContactImage){
        mContactId = Integer.toString(tContactId);
        mContactName = tContactName;
        mContactLastName = tContactLastName;
        mContactUserName = tContactUsername;
        mVerified = tVerfied;
        mContactImage = tContactImage;
    }




    protected ConnectionItem(Parcel in) {
        mContactId = in.readString();
        mContactName = in.readString();
        mContactLastName = in.readString();
        mContactUserName = in.readString();
        mVerified = in.readInt();
        mContactImage = in.readString();
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
        dest.writeString(mContactId);
        dest.writeString(mContactName);
        dest.writeString(mContactLastName);
        dest.writeString(mContactUserName);
        dest.writeInt(mVerified);
        dest.writeString(mContactImage);
    }

//    public static class Builder {
//        private String mName = "";
//        private String mUserName = "";
//
//        public Builder(String name, String userName){
//            mName = name;
//            mUserName = userName;
//        }
//
//        public ConnectionItem build() {return new ConnectionItem(this); }
//
//
//    }

//    private ConnectionItem(final ConnectionItem.Builder builder){
//        this.mName = builder.mName;
//        this.mUserName = builder.mUserName;
//    }

    public String getContactId() { return mContactId; }

    public String getFirstName(){
        return mContactName;
    }

    public String getLastName(){
        return mContactLastName;
    }

    public String getContactUserName() { return mContactUserName; }

    public int getVerified() { return mVerified; }

    public String getContactImage() { return mContactImage; }

}

