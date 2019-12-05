package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Weather class
 * @author Ken Gil Romero kgmr@uw.edu
 */
public class Weather implements Serializable, Parcelable {

    // the description the weather
    private String mDescription;

    //the main description of the weather
    private String mMain;

    /**
     * the icon of the weather
     */
    private final String mIcon;

    // the lon of the weather
    private double mLon;

    // the lat of the weather
    private double mLat;

    // the temp of the weather
    private double mTemp;

    // the pressure of the weather
    private int mPressure;

    // the humidity of the weather
    private int mHumidity;

    // the temp min of the weather
    private double mTemp_min;

    // the temp max of the weather
    private double mTemp_max;

    // the speed of the weather
    private double mSpeed;

    // the deg of the weather
    private double mDeg;

    // the timezone diff at UTC of the weather's location
    private long mTimezone;

    // the timezonid of the weather's location
    private String mTimezoneID;

    // the zip of the weather's location
    private String mZip;

    //the state of the weather's location
    private String mState;

    // the city of the weather's location
    private String mCity;

    // the country of the weather's country
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

    /**
     * constructor for weather
     * @param tDescription the given description of the weather
     * @param tIcon the icon of the weather
     * @param tTemp the temp of the weather
     * @param tPressure the pressure of the weather
     * @param tHumidity the humidity of the weather
     * @param tTemp_min the temp min of the weather
     * @param tTemp_max the temp max of the weather
     * @param tSpeed the speed of the veather
     * @param tTimezone the timezone of the weather's location
     * @param tCity the city of the weather's locaiton
     */
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

    /**
     * short constructor for the weather
     * @param tIcon given icon of the weather
     * @param tTemp given temp of the weather
     */
    public Weather(String tIcon, double tTemp) {
        mIcon = tIcon;
        mTemp = tTemp;
    }

    /**
     * sets the temp of the weather
     * @param tTemp temp given
     */
    public void setTemp(double tTemp) {
        mTemp = tTemp;
    }

    /**
     * sets the main description of the weather
     * @param tMain the given main description of a weather
     */
    public void setMain(String tMain) {
        mMain = tMain;
    }

    /**
     * sets the country of the weather's location
     * @param tCountry the given country of the location
     */
    public void setCountry(String tCountry) {
        mCountry = tCountry;
    }

    /**
     * sets the degree of the  weather
     * @param tDeg the given weather of the weather
     */
    public void setDeg(double tDeg) {
        mDeg = tDeg;
    }

    /**
     * sets the zip of the weather's location
     * @param tZip the given zip
     */
    public void setZip(String tZip) {
        mZip = tZip;
    }

    /**
     * parceable method
     * @param in the parcel
     */
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

    /**
     * the  given parceable static
     */
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

    /**
     *
     * @return the lon of the weather
     */
    public double getLon() {
        return mLon;
    }

    /**
     *
     * @return the lat of the weather
     */
    public double getLat() {
        return mLat;
    }

    /**
     *
     * @return the temp of the weather
     */
    public double getTemp() {
        return mTemp;
    }

    /**
     *
     * @return the pressure of the weather
     */
    public double getPressure() {
        return mPressure;
    }

    /**
     *
     * @return the humidity of the weather
     */
    public double getHumidity() {
        return mHumidity;
    }

    /**
     *
     * @return temp min of the weather
     */
    public double getTemp_min() {
        return mTemp_min;
    }

    /**
     *
     * @return the temp max of the weather
     */
    public double getTemp_max() {
        return mTemp_max;
    }

    /**
     *
     * @return the speed of the weather
     */
    public double getSpeed() {
        return mSpeed;
    }

    /**
     *
     * @return deg of the weather
     */
    public double getDeg() {
        return mDeg;
    }

    /**
     *
     * @return the timezone of the weather's location
     */
    public long getTimezone() {
        return mTimezone;
    }

    /**
     *
     * @return the city of the weather's location
     */
    public String getCity() {
        return mCity;
    }

    /**
     *
     * @return the country of the weather's location
     */
    public String getCountry() {
        return mCountry;
    }

    /**
     *
     * @return the state of the weather's location
     */
    public String getState() {
        return mState;
    }

    /**
     * given state of the weather's location
     * @param tState the state of a location
     */
    public void setState(String tState) {
        mState = tState;
    }

    /**
     *
     * @return the timezoneid of the weather's location
     */
    public String getTimezoneID() {
        return mTimezoneID;
    }

    /**
     * sets the timezoneid of the weather's location
     * @param mTimezoneID the given timezoneid
     */
    public void setTimezoneID(String mTimezoneID) {
        this.mTimezoneID = mTimezoneID;
    }

//    public String getSunrise() {
//        return mSunrise;
//    }

//    public String getSunset() {
//        return mSunset;
//    }

    /**
     *
     * @return the description of the weather
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     *
     * @return the main of the weather
     */
    public String getMain() {
        return mMain;
    }

    /**
     *
     * @return the icon of the weather
     */
    public String getIcon() {
        return mIcon;
    }

    /**
     * sets the weather lon
     * @param tLon given lon
     */
    public void setLon(double tLon) {
        mLon = tLon;
    }

    /**
     * sets the weather lat
     * @param tLat given lat
     */
    public void setLat(double tLat) {
        mLat = tLat;
    }

    /**
     * @return the zip of the weather
     */
    public String getZip() {
        return mZip;
    }

    /**
     * sets the city of the weather
     * @param tCity the given city
     */
    public void setCity(String tCity) {
        mCity = tCity;
    }
}
