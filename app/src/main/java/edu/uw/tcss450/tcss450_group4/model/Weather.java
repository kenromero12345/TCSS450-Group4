package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 *
 */
public class Weather implements Serializable, Parcelable {

    //TODO:low temp
    //TODO:high temp
    public String getDescription() {
        return mDescription;
    }

    private String mDescription;
    private String mMain;

    public String getMain() {
        return mMain;
    }

    public String getIcon() {
        return mIcon;
    }

    private final String mIcon;

    public void setLon(double mLon) {
        this.mLon = mLon;
    }

    private double mLon;

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    private double mLat;
    private double mTemp;
    private int mPressure;
    private int mHumidity;
    private double mTemp_min;
    private double mTemp_max;
    private double mSpeed;
    private double mDeg;
    private long mTimezone;
    private String mTimezoneID;

    public String getZip() {
        return mZip;
    }

    private String mZip;
    private String mState;

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    private String mCity;
    private String mCountry;

//    public String getJwt() {
//        return mJwt;
//    }

//    private final String mJwt;
//    private final String mSunrise;
//    private final String mSunset;

    //lon, lat from coord, temp, pressure, humidity, temp_min, temp_max from main
    // , speed, deg from wind, timezone, name, country, sunrise, sunset from sys
//
//    public float getLongitude() {
//        return mLongitude;
//    }

//    private final float mLongitude;
//
//    public float getLatitude() {
//        return mLatitude;
//    }
//
//    private final float mLatitude;

    //TODO:
    public Weather(String tDescription, String tIcon, double tTemp
            , int tPressure, int tHumidity, double tTemp_min
            , double tTemp_max, double tSpeed, long tTimezone, String tCity
            /*, String tSunrise, String tSunset*//* , String tJwt*/)  {
        mDescription = tDescription;
        mIcon = tIcon;
        mTemp = tTemp;
        mPressure = tPressure;
        mHumidity = tHumidity;
        mTemp_min = tTemp_min;
        mTemp_max = tTemp_max;
        mSpeed = tSpeed;
        mTimezone = tTimezone;
        mCity = tCity;
//        mMain = tMain;
//        mSunrise = tSunrise;
//        mSunset = tSunset;
//        mJwt = tJwt;
    }

    public Weather(String tIcon, double tTemp) {
        mIcon = tIcon;
        mTemp = tTemp;
    }
    public void setTemp(double tTemp) {
        mTemp = tTemp;
    }
    public void setMain(String tMain) {
        mMain = tMain;
    }

    public void setCountry(String tCountry) {
        mCountry = tCountry;
    }

    public void setDeg(double tDeg) {
        mDeg = tDeg;
    }

    public void setZip(String tZip) {
        mZip = tZip;
    }

    protected Weather(Parcel in) {
        mMain = in.readString();
        mDescription = in.readString();
        mIcon = in.readString();
        mLon = in.readDouble();
        mLat = in.readDouble();
        mTemp = in.readDouble();
        mPressure = in.readInt();
        mHumidity = in.readInt();
        mTemp_min = in.readDouble();
        mTemp_max = in.readDouble();
        mSpeed = in.readDouble();
        mDeg = in.readDouble();
        mTimezone = in.readLong();
        mCity = in.readString();
        mCountry = in.readString();
        mZip = in.readString();
        mState = in.readString();
        mTimezoneID = in.readString();
//        mJwt = in.readString();
//        mSunrise = in.readString();
//        mSunset = in.readString();
//        mLongitude = in.readFloat();
//        mLatitude = in.readFloat();
        //TODO:
//        mPubDate = in.readString();
//        mTitle = in.readString();
//        mUrl = in.readString();
//        mTeaser = in.readString();
//        mAuthor = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDescription);
        dest.writeString(mIcon);
        dest.writeString(mCity);
        dest.writeString(mCountry);
        dest.writeDouble(mDeg);
        dest.writeInt(mHumidity);
        dest.writeDouble(mLat);
        dest.writeDouble(mLon);
        dest.writeDouble(mPressure);
        dest.writeDouble(mSpeed);
        dest.writeDouble(mTemp);
        dest.writeDouble(mTemp_max);
        dest.writeDouble(mTemp_min);
        dest.writeString(mMain);
        dest.writeLong(mTimezone);
        dest.writeString(mZip);
        dest.writeString(mState);
        dest.writeString(mTimezoneID);
//        dest.writeString(mJwt);
//        dest.writeFloat(mLongitude);
//        dest.writeFloat(mLatitude);
    }

    public double getLon() {
        return mLon;
    }

    public double getLat() {
        return mLat;
    }

    public double getTemp() {
        return mTemp;
    }

    public double getPressure() {
        return mPressure;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public double getTemp_min() {
        return mTemp_min;
    }

    public double getTemp_max() {
        return mTemp_max;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public double getDeg() {
        return mDeg;
    }

    public long getTimezone() {
        return mTimezone;
    }

    public String getCity() {
        return mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getState() {
        return mState;
    }

    public void setState(String mState) {
        this.mState = mState;
    }

    public String getTimezoneID() {
        return mTimezoneID;
    }

    public void setTimezoneID(String mTimezoneID) {
        this.mTimezoneID = mTimezoneID;
    }

//    public String getSunrise() {
//        return mSunrise;
//    }

//    public String getSunset() {
//        return mSunset;
//    }
}
