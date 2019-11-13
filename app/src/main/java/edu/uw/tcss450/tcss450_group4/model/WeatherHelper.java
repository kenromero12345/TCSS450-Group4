package edu.uw.tcss450.tcss450_group4.model;

import android.app.AlertDialog;
import android.content.Context;

public class WeatherHelper {
    private static final char DEGREE = (char) 0x00B0;
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
        }
        return "c04d"; //sleet, cloudy, wind     would just be here
    }

    public static String tempFromKelvinToFarenheitString(double tTemp) {
        return String.valueOf(Math.round(((tTemp - 273.15) * 9 / 5) + 32)) +
                DEGREE + "F";
    }

    public static String tempFromKelvinToCelsiusString(double tTemp) {
        return String.valueOf(Math.round(tTemp - 273.15)) +
                DEGREE + "C";
    }

    public static String getImgUrl(String tIcon) {
        if (tIcon.equals("50d")) {
            return "http://openweathermap.org/img/wn/" + tIcon + "@2x.png";
        } else {
            return "https://www.weatherbit.io/static/img/icons/" + tIcon + ".png";
        }
    }

    public static void alert(String s, Context c) {
        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(s);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
