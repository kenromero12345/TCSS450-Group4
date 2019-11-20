package edu.uw.tcss450.tcss450_group4.model;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static edu.uw.tcss450.tcss450_group4.R.string.ep_10d;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_24h;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_latLon;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_weather;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_zip;

/**
 * Class that helps weather have less redundancy
 * @author Ken Gil Romero kgmr@uw.edu
 */
public class WeatherHelper {
    /**
     * char for degree symbol
     */
    private static final char DEGREE = (char) 0x00B0;

    /**
     * tags for weather for log
     */
    private static final String TAG = "WEATHER_FRAG";

    /**
     * @param tIcon open weather map and dark sky icon
     * @return the weather bit icon except fog(openweathermap)
     */
    public static String getNewIcon(String tIcon){
        switch (tIcon) {
            case "01d":
            case "clear-day":
                return "c01d";
            case "01n":
            case "clear-night":
                return "c01n";
            case "02d":
            case "partly-cloudy-day":
            case "03d":
                return "c02d";
            case "02n":
            case "partly-cloudy-night":
            case "03n":
                return "c02n";
            case "04d":
                return "c03d";
            case "04n":
                return "c03n";
            case "09d":
                return "r05d";
            case "09n":
                return "r05n";
            case "10d":
            case "rain":
            case "10n":
                return "r02d";
            case "thunderstorm":
            case "11d":
                return "t04d";
            case "11n":
                return "t04n";
            case "13d":
            case "snow":
            case "hail":
                return "s02d";
            case "13n":
                return "s02n";
            case "50d":
                return "a01d";
            case "50n":
                return "a01n";
            case "sleet":
                /* || tIcon == "wind"*/

                return "s05d";
            case "fog":
                return "50d";
//            case "fogd":
//                return "a05d";
//            case "fogn":
//                return "a05n";
//                return "50d";
        }
        return "c04d"; //sleet, cloudy, wind     would just be here
    }

    /**
     * @param tTemp kelvin to be converted to string Fahrenheit
     * @return the Fahrenheit temp
     */
    public static String tempFromKelvinToFahrenheitString(double tTemp) {
        return String.valueOf(Math.round(((tTemp - 273.15) * 9 / 5) + 32)) +
                DEGREE + "F";
    }

    /**
     *
     * @param tTemp kelvin to be converted to string Celsius
     * @return the Celsius temp
     */
    public static String tempFromKelvinToCelsiusString(double tTemp) {
        return String.valueOf(Math.round(tTemp - 273.15)) +
                DEGREE + "C";
    }

    /**
     *
     * @param tIcon the icon weatherbit except fog(openweathermap)
     * @return the url
     */
    public static String getImgUrl(String tIcon) {
        if (tIcon.equals("50d")) {
            return "http://openweathermap.org/img/wn/" + tIcon + "@2x.png";
        } else {
            return "https://www.weatherbit.io/static/img/icons/" + tIcon + ".png";
        }
    }

    /**
     * displays the alert dialog
     * @param tS the given string to show
     * @param tC the given context needed to show the alert
     */
    public static void alert(String tS, Context tC) {
        AlertDialog alertDialog = new AlertDialog.Builder(tC).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(tS);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    /**
     *
     * @param uri the uri to hit
     * @param msg the message to send the uri
     * @param val the method at post execute
     * @param jwToken the given token
     */
    public static void sendPostAsyncTaskHelper(Uri uri, JSONObject msg, Consumer<String> val, String jwToken) {
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(val)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", jwToken) //add the JWT as a header
                .build().execute();
    }

    /**
     *
     * @param tLat the given latitude
     * @param tLon the given longitude
     * @return a json object message
     */
    public static JSONObject getJsonObjectLatLon(double tLat, double tLon) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("lon", tLon);
            msg.put("lat", tLat);
        } catch (JSONException e) {
            Log.wtf("LONG/LAT", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    public static JSONObject getJsonObjectZip(int tZip) {
        JSONObject msg = new JSONObject();
        try {
            msg.put("zip", tZip);
        } catch (JSONException e) {
            Log.wtf("zip", "Error creating JSON: " + e.getMessage());
        }
        return msg;
    }

    /**
     *
     * @param context the given context
     * @return the uri for the current weather
     */
    public static Uri getUriWeatherCurrentLatLon(Context context) {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(context.getString(ep_base_url))
                .appendPath(context.getString(ep_weather))
                .appendPath(context.getString(ep_latLon))
                .build();
    }

    /**
     *
     * @param context the given context
     * @return the uri for the 10d weather
     */
    public static Uri getUriWeather10dLatLon(Context context) {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(context.getString(ep_base_url))
                .appendPath(context.getString(ep_weather))
                .appendPath(context.getString(ep_latLon))
                .appendPath(context.getString(ep_10d))
                .build();
    }

    /**
     *
     * @param context the given context
     * @return the uri for the 24h weather
     */
    public static Uri getUriWeather24hLatLon(Context context) {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(context.getString(ep_base_url))
                .appendPath(context.getString(ep_weather))
                .appendPath(context.getString(ep_latLon))
                .appendPath(context.getString(ep_24h))
                .build();
    }

    /**
     *
     * @param context the given context
     * @return the uri for the current weather
     */
    public static Uri getUriWeatherCurrentZip(Context context) {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(context.getString(ep_base_url))
                .appendPath(context.getString(ep_weather))
                .appendPath(context.getString(ep_zip))
                .build();
    }

    /**
     *
     * @param context the given context
     * @return the uri for the 10d weather
     */
    public static Uri getUriWeather10dZip(Context context) {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(context.getString(ep_base_url))
                .appendPath(context.getString(ep_weather))
                .appendPath(context.getString(ep_zip))
                .appendPath(context.getString(ep_10d))
                .build();
    }

    /**
     *
     * @param context the given context
     * @return the uri for the 24h weather
     */
    public static Uri getUriWeather24hZip(Context context) {
        return new Uri.Builder()
                .scheme("https")
                .appendPath(context.getString(ep_base_url))
                .appendPath(context.getString(ep_weather))
                .appendPath(context.getString(ep_zip))
                .appendPath(context.getString(ep_24h))
                .build();
    }
}
