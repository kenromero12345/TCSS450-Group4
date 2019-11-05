package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Weather implements Serializable, Parcelable {


    public String getDescription() {
        return mDescription;
    }

    private final String mDescription;
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
    public Weather(String tDescription/*, float tLongitude, float tLatitude*/)  {
        mDescription = tDescription;
//        mLongitude = tLongitude;
//        mLatitude = tLatitude;
    }

    protected Weather(Parcel in) {
        mDescription = in.readString();
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
//        dest.writeFloat(mLongitude);
//        dest.writeFloat(mLatitude);
        //TODO:
//        dest.writeString(mPubDate);
//        dest.writeString(mTitle);
//        dest.writeString(mUrl);
//        dest.writeString(mTeaser);
//        dest.writeString(mAuthor);
    }

    /**
     * Get all of the fields in a single JSON object. Note, if no values were provided for the
     * optional fields via the Builder, the JSON object will include the empty string for those
     * fields.
     *
     * Keys: username, password, first, last, email
     *
     * @return all of the fields in a single JSON object
     */
    public JSONObject asJSONObject() {
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put("description", getDescription());
//            msg.put("long", mLongitude);
//            msg.put("lat", mLongitude);
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }
}
