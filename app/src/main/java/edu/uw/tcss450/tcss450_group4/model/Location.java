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
    protected Location(Parcel in) {
        mLon = in.readDouble();
        mLat = in.readDouble();
        mName = in.readString();
        mZip = in.readInt();
    }

    /**
     *
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

    public double getLon() {
        return mLon;
    }

    private final double mLon;

    public final double getLat() {
        return mLat;
    }

    public final String getName() {
        return mName;
    }

    public final int getZip() {
        return mZip;
    }

    private double mLat;
    private final String mName;
    private int mZip;

//    public Location() {
//        mName = "";
//    }

    public Location(double tLon, double tLat, String tName) {
        mLat = tLat;
        mLon = tLon;
        mName = tName;
        mZip = -1;
    }

    public void setZip(int tZip) {
        mZip = tZip;
    }

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
}
