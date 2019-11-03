package edu.uw.tcss450.tcss450_group4.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Weather implements Parcelable {


//    private final

    //TODO:
    public Weather(JSONObject data)  {
        //data.getString(key)
        //data.getString(getString(R.string.keys_json_))
    }

    protected Weather(Parcel in) {
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
        //TODO:
//        dest.writeString(mPubDate);
//        dest.writeString(mTitle);
//        dest.writeString(mUrl);
//        dest.writeString(mTeaser);
//        dest.writeString(mAuthor);
    }
}
