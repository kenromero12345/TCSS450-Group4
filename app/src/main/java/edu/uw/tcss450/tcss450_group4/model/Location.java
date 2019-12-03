package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Location class that includes information about latitude, longitude, name(city, country),
 * and zip
 * @author Ken Gil Romero kgmr@uw.edu
 */
public class Location implements Parcelable, Serializable {

    //overidden method of parceable
    protected Location(Parcel in) {
        mLon = in.readDouble();
        mLat = in.readDouble();
        mName = in.readString();
        mZip = in.readInt();
    }

    /**
     * overriden static of parceable
     */
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    /**
     * the lon of the location
     */
    private final double mLon;

    // the lat of the location
    private final double mLat;

    // the name of the location
    private final String mName;

    // the zip of the location
    private int mZip;

//    public Location() {
//        mName = "";
//    }

    /**
     * constructor of the location
     * @param tLon the given lon
     * @param tLat the given lat
     * @param tName the given name of the location
     */
    public Location(double tLon, double tLat, String tName) {
        mLat = tLat;
        mLon = tLon;
        mName = tName;
        mZip = -1;
    }

//    public void setZip(int tZip) {
//        mZip = tZip;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLon);
        dest.writeDouble(mLat);
        dest.writeString(mName);
        dest.writeInt(mZip);
    }

    /**
     *
     * @return the lat of the location
     */
    public final double getLat() {
        return mLat;
    }

    /**
     *
     * @return the name of the location
     */
    public final String getName() {
        return mName;
    }

//    /**
//     *
//     * @return
//     */
//    public final int getZip() {
//        return mZip;
//    }

    /**
     *
     * @return the lon of the location
     */
    public double getLon() {
    return mLon;
}
}
