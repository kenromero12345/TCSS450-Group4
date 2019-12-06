package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionItem implements Serializable, Parcelable {


    /**
     * Instance field for all the required things for a connection item.
     */
    private final String mContactId;
    private final String mContactName;
    private final String mContactLastName;
    private final String mContactUserName;
    private final String mContactImage;
    private int mVerified;

    /**
     * Constructor for a connection item.
     * @param tContactId The id of the contact.
     * @param tContactName The contact name.
     * @param tContactLastName The contact last name.
     * @param tContactUsername The contact user name.
     * @param tContactImage The contact image.
     */
    public ConnectionItem(int tContactId, String tContactName, String tContactLastName, String tContactUsername,
                          String tContactImage){
        mContactId = Integer.toString(tContactId);
        mContactName = tContactName;
        mContactLastName = tContactLastName;
        mContactUserName = tContactUsername;
        mContactImage = tContactImage;


    }

    /**
     *
     * @param tContactId The id of the contact.
     * @param tContactName The contact name.
     * @param tContactLastName The contact last name.
     * @param tContactUsername The contact user name.
     * @param tVerfied The verification of the user.
     * @param tContactImage The contact image.
     */
    public ConnectionItem(int tContactId, String tContactName, String tContactLastName, String tContactUsername,
                          int tVerfied, String tContactImage){
        mContactId = Integer.toString(tContactId);
        mContactName = tContactName;
        mContactLastName = tContactLastName;
        mContactUserName = tContactUsername;
        mVerified = tVerfied;
        mContactImage = tContactImage;
    }


    /**
     * Method to read the information.
     * @param in the Parcel.
     */
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


    /**
     * Get the id.
     * @return the contact id.
     */
    public String getContactId() { return mContactId; }

    /**
     * Get the first name.
     * @return the first name.
     */
    public String getFirstName(){
        return mContactName;
    }

    /**
     * Get the last name.
     * @return the last name.
     */
    public String getLastName(){
        return mContactLastName;
    }

    /**
     * Get the user name.
     * @return the user name.
     */
    public String getContactUserName() { return mContactUserName; }

    /**
     * Get the verification of the user.
     * @return the verification.
     */
    public int getVerified() { return mVerified; }

    /**
     * Get the contact image.
     * @return the image.
     */
    public String getContactImage() { return mContactImage; }

}

