package edu.uw.tcss450.tcss450_group4.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.model.Location;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static android.graphics.Color.BLACK;
import static edu.uw.tcss450.tcss450_group4.R.color.redviolet;
import static edu.uw.tcss450.tcss450_group4.R.color.uwPurple;
import static edu.uw.tcss450.tcss450_group4.R.id.*;
import static edu.uw.tcss450.tcss450_group4.R.layout;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_10d;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_24h;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_get;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_send;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_weather;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_zip;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_coord;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_country;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_data;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_deg;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_description;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_hourly;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_humidity;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_icon;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_lat;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_lon;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_long;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_main;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_messages;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_name;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_nickname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_pressure;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_speed;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_sys;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temp;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temp_max;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temp_min;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temperature;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_weather;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_wind;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.alert;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getImgUrl;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getNewIcon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.tempFromKelvinToCelsiusString;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.tempFromKelvinToFarenheitString;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link WeatherFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link WeatherFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class WeatherFragment extends Fragment {
    private static final String TAG = "WEATHER_FRAG";
    private static final char DEGREE = (char) 0x00B0;
    private Weather mWeather;
    private Weather[] mWeathers10d;
    private Weather[] mWeathers24h;
    private View mView;
    private String mEmail;
    private String mJwToken;
//    private double mLon;
//    private double mLat;

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(layout_weather_wait).setVisibility(View.VISIBLE);
        view.findViewById(weather_saveButton).setVisibility(View.INVISIBLE);
        view.findViewById(weather_temperatureSwitch).setVisibility(View.INVISIBLE);
        initialization(view);
        setComponents();
    }


    private void initialization(@NonNull View view) {
        WeatherFragmentArgs args = null;
        if (getArguments() != null) {
            args = WeatherFragmentArgs.fromBundle(getArguments());
        }
        if (args != null) {
            mEmail = args.getEmail();
            mJwToken = args.getJwt();
            mWeather = args.getWeather();
            mWeathers10d = args.getWeathers10d();
            mWeathers24h = args.getWeathers24h();
            mWeathers24h[0].setTemp(mWeather.getTemp());

        }
        mView = view;
    }

    private void setComponents() {        
        setWeather();
        mView.findViewById(weather_getZipButton).setOnLongClickListener(v ->
                setSnackbar("Get the current weather condition and forecasts of the given zip"));
        mView.findViewById(weather_getZipButton).setOnClickListener(v -> attemptGetWeatherZip());

        mView.findViewById(weather_saveButton).setOnLongClickListener(v ->
                setSnackbar("Save the current location"));
        mView.findViewById(weather_saveButton).setOnClickListener(v -> attemptSaveWeather());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mView.findViewById(weather_saveButton)
//                    .setTooltipText("Save Weather");
//        }
        mView.findViewById(weather_getSavedWeathersButton).setOnClickListener(v ->
                gotoSavedWeatherRecyclerView());
        mView.findViewById(weather_getSavedWeathersButton).setOnLongClickListener(v ->
                setSnackbar("Get the list of saved locations"));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mView.findViewById(weather_getSavedWeathersButton)
//                    .setTooltipText("Get Saved Weathers");
//        }
        mView.findViewById(weather_temperatureSwitch).setOnClickListener(e -> switchTemperature());
        mView.findViewById(weather_forecastSwitch).setOnClickListener(e -> switchForecast());
        mView.findViewById(weather_getLocationButton).setOnClickListener(e -> gotoMap());
        mView.findViewById(weather_getLocationButton).setOnLongClickListener(v ->
                setSnackbar("Get the current weather condition and forecasts from the chosen location"));
        setHours();
        setDays();
    }

    private void gotoMap() {
        WeatherFragmentDirections.ActionNavWeatherToNavMap action =
                WeatherFragmentDirections.actionNavWeatherToNavMap(mEmail, mJwToken/*, new LatLng(mWeather.getLat(), mWeather.getLon())*/);
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(action);
    }

    private boolean setSnackbar(String tString) {
        Snackbar.make(mView, tString
                , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        return  true;
    }

    private void switchForecast() {
        if (((Switch) mView.findViewById(weather_forecastSwitch)).isChecked()) {
            ((TextView)mView.findViewById(weather_10DayForecast)).setTypeface(Typeface.DEFAULT_BOLD);
            ((TextView)mView.findViewById(weather_24HourForecast)).setTypeface(Typeface.DEFAULT);
            ((TextView)mView.findViewById(weather_10DayForecast)).setTextColor(
                    ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));
            ((TextView)mView.findViewById(weather_24HourForecast)).setTextColor(BLACK);
            Objects.requireNonNull(getActivity()).findViewById(weather_layoutHours1)
                    .setVisibility(View.GONE);
            getActivity().findViewById(weather_layoutHours2)
                    .setVisibility(View.GONE);
            getActivity().findViewById(weather_layoutHours3)
                    .setVisibility(View.GONE);
            getActivity().findViewById(weather_layoutHours4)
                    .setVisibility(View.GONE);
            getActivity().findViewById(weather_layoutDays1)
                    .setVisibility(View.VISIBLE);
            getActivity().findViewById(weather_layoutDays2)
                    .setVisibility(View.VISIBLE);
        } else {
            ((TextView)mView.findViewById(weather_24HourForecast)).setTypeface(Typeface.DEFAULT_BOLD);
            ((TextView)mView.findViewById(weather_10DayForecast)).setTypeface(Typeface.DEFAULT);
            ((TextView)mView.findViewById(weather_10DayForecast)).setTextColor(BLACK);
            ((TextView)mView.findViewById(weather_24HourForecast)).setTextColor(
                    ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));
            Objects.requireNonNull(getActivity()).findViewById(weather_layoutHours1)
                    .setVisibility(View.VISIBLE);
            getActivity().findViewById(weather_layoutHours2)
                    .setVisibility(View.VISIBLE);
            getActivity().findViewById(weather_layoutHours3)
                    .setVisibility(View.VISIBLE);
            getActivity().findViewById(weather_layoutHours4)
                    .setVisibility(View.VISIBLE);
            getActivity().findViewById(weather_layoutDays1)
                    .setVisibility(View.GONE);
            getActivity().findViewById(weather_layoutDays2)
                    .setVisibility(View.GONE);
        }
    }

    private void setHours() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                = new SimpleDateFormat("h a");//TODO
        Calendar calendar = Calendar.getInstance();

        Date today = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus1 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus2 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus3 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus4 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus5 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus6 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus7 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus8 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus9 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus10 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus11 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus12 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus13 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus14 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus15 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus16 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus17 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus18 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus19 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus20 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus21 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus22 = calendar.getTime();
        calendar.add(Calendar.HOUR, 1);
        Date todayPlus23 = calendar.getTime();

        ((TextView) mView.findViewById(weather_hour1)).setText(sdf.format(today));
        ((TextView) mView.findViewById(weather_hour2)).setText(sdf.format(todayPlus1));
        ((TextView) mView.findViewById(weather_hour3)).setText(sdf.format(todayPlus2));
        ((TextView) mView.findViewById(weather_hour4)).setText(sdf.format(todayPlus3));
        ((TextView) mView.findViewById(weather_hour5)).setText(sdf.format(todayPlus4));
        ((TextView) mView.findViewById(weather_hour6)).setText(sdf.format(todayPlus5));
        ((TextView) mView.findViewById(weather_hour7)).setText(sdf.format(todayPlus6));
        ((TextView) mView.findViewById(weather_hour8)).setText(sdf.format(todayPlus7));
        ((TextView) mView.findViewById(weather_hour9)).setText(sdf.format(todayPlus8));
        ((TextView) mView.findViewById(weather_hour10)).setText(sdf.format(todayPlus9));
        ((TextView) mView.findViewById(weather_hour11)).setText(sdf.format(todayPlus10));
        ((TextView) mView.findViewById(weather_hour12)).setText(sdf.format(todayPlus11));
        ((TextView) mView.findViewById(weather_hour13)).setText(sdf.format(todayPlus12));
        ((TextView) mView.findViewById(weather_hour14)).setText(sdf.format(todayPlus13));
        ((TextView) mView.findViewById(weather_hour15)).setText(sdf.format(todayPlus14));
        ((TextView) mView.findViewById(weather_hour16)).setText(sdf.format(todayPlus15));
        ((TextView) mView.findViewById(weather_hour17)).setText(sdf.format(todayPlus16));
        ((TextView) mView.findViewById(weather_hour18)).setText(sdf.format(todayPlus17));
        ((TextView) mView.findViewById(weather_hour19)).setText(sdf.format(todayPlus18));
        ((TextView) mView.findViewById(weather_hour20)).setText(sdf.format(todayPlus19));
        ((TextView) mView.findViewById(weather_hour21)).setText(sdf.format(todayPlus20));
        ((TextView) mView.findViewById(weather_hour22)).setText(sdf.format(todayPlus21));
        ((TextView) mView.findViewById(weather_hour23)).setText(sdf.format(todayPlus22));
        ((TextView) mView.findViewById(weather_hour24)).setText(sdf.format(todayPlus23));
    }

    private void setDays() {
        // EEE MM/dd
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                = new SimpleDateFormat("MM/dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2
                = new SimpleDateFormat("h a");
        Calendar calendar = Calendar.getInstance();

        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus2 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus3 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus4 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus5 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus6 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus7 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus8 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus9 = calendar.getTime();
        ((TextView) mView.findViewById(weather_day1)).setText(sdf.format(today));
        ((TextView) mView.findViewById(weather_day2)).setText(sdf.format(todayPlus1));
        ((TextView) mView.findViewById(weather_day3)).setText(sdf.format(todayPlus2));
        ((TextView) mView.findViewById(weather_day4)).setText(sdf.format(todayPlus3));
        ((TextView) mView.findViewById(weather_day5)).setText(sdf.format(todayPlus4));
        ((TextView) mView.findViewById(weather_day6)).setText(sdf.format(todayPlus5));
        ((TextView) mView.findViewById(weather_day7)).setText(sdf.format(todayPlus6));
        ((TextView) mView.findViewById(weather_day8)).setText(sdf.format(todayPlus7));
        ((TextView) mView.findViewById(weather_day9)).setText(sdf.format(todayPlus8));
        ((TextView) mView.findViewById(weather_day10)).setText(sdf.format(todayPlus9));
    }

    private void switchTemperature() {
        if (((Switch) mView.findViewById(weather_temperatureSwitch)).isChecked()) {
            TextView temp = mView.findViewById(weather_temperature);
            temp.setText(tempFromKelvinToFarenheitString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), redviolet));

            setTempDaysToFahrenheit();
            setTempHoursToFahrenheit();

        } else {
            TextView temp = mView.findViewById(weather_temperature);
            temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));

            setTempDaysTextToCelsius();
            setTempHoursTextToCelsius();
//            tempDay1.setText(tempFromKelvinToCelsiusString(mWeathers10d[0].getTemp()));
//            tempDay2.setText(tempFromKelvinToCelsiusString(mWeathers10d[1].getTemp()));
//            tempDay3.setText(tempFromKelvinToCelsiusString(mWeathers10d[2].getTemp()));
//            tempDay4.setText(tempFromKelvinToCelsiusString(mWeathers10d[3].getTemp()));
//            tempDay5.setText(tempFromKelvinToCelsiusString(mWeathers10d[4].getTemp()));
//            tempDay6.setText(tempFromKelvinToCelsiusString(mWeathers10d[5].getTemp()));
//            tempDay7.setText(tempFromKelvinToCelsiusString(mWeathers10d[6].getTemp()));
//            tempDay8.setText(tempFromKelvinToCelsiusString(mWeathers10d[7].getTemp()));
//            tempDay9.setText(tempFromKelvinToCelsiusString(mWeathers10d[8].getTemp()));
//            tempDay10.setText(tempFromKelvinToCelsiusString(mWeathers10d[9].getTemp()));
//
//            tempHour1.setText(tempFromKelvinToCelsiusString(mWeathers24h[0].getTemp()));
//            tempHour2.setText(tempFromKelvinToCelsiusString(mWeathers24h[1].getTemp()));
//            tempHour3.setText(tempFromKelvinToCelsiusString(mWeathers24h[2].getTemp()));
//            tempHour4.setText(tempFromKelvinToCelsiusString(mWeathers24h[3].getTemp()));
//            tempHour5.setText(tempFromKelvinToCelsiusString(mWeathers24h[4].getTemp()));
//            tempHour6.setText(tempFromKelvinToCelsiusString(mWeathers24h[5].getTemp()));
//            tempHour7.setText(tempFromKelvinToCelsiusString(mWeathers24h[6].getTemp()));
//            tempHour8.setText(tempFromKelvinToCelsiusString(mWeathers24h[7].getTemp()));
//            tempHour9.setText(tempFromKelvinToCelsiusString(mWeathers24h[8].getTemp()));
//            tempHour10.setText(tempFromKelvinToCelsiusString(mWeathers24h[9].getTemp()));
//            tempHour11.setText(tempFromKelvinToCelsiusString(mWeathers24h[10].getTemp()));
//            tempHour12.setText(tempFromKelvinToCelsiusString(mWeathers24h[11].getTemp()));
//            tempHour13.setText(tempFromKelvinToCelsiusString(mWeathers24h[12].getTemp()));
//            tempHour14.setText(tempFromKelvinToCelsiusString(mWeathers24h[13].getTemp()));
//            tempHour15.setText(tempFromKelvinToCelsiusString(mWeathers24h[14].getTemp()));
//            tempHour16.setText(tempFromKelvinToCelsiusString(mWeathers24h[15].getTemp()));
//            tempHour17.setText(tempFromKelvinToCelsiusString(mWeathers24h[16].getTemp()));
//            tempHour18.setText(tempFromKelvinToCelsiusString(mWeathers24h[17].getTemp()));
//            tempHour19.setText(tempFromKelvinToCelsiusString(mWeathers24h[18].getTemp()));
//            tempHour20.setText(tempFromKelvinToCelsiusString(mWeathers24h[19].getTemp()));
//            tempHour21.setText(tempFromKelvinToCelsiusString(mWeathers24h[20].getTemp()));
//            tempHour22.setText(tempFromKelvinToCelsiusString(mWeathers24h[21].getTemp()));
//            tempHour23.setText(tempFromKelvinToCelsiusString(mWeathers24h[22].getTemp()));
//            tempHour24.setText(tempFromKelvinToCelsiusString(mWeathers24h[23].getTemp()));
        }
    }

    private void setTempHoursToFahrenheit() {
        TextView tempHour1 = mView.findViewById(weather_hour1Temp);
        TextView tempHour2 = mView.findViewById(weather_hour2Temp);
        TextView tempHour3 = mView.findViewById(weather_hour3Temp);
        TextView tempHour4 = mView.findViewById(weather_hour4Temp);
        TextView tempHour5 = mView.findViewById(weather_hour5Temp);
        TextView tempHour6 = mView.findViewById(weather_hour6Temp);
        TextView tempHour7 = mView.findViewById(weather_hour7Temp);
        TextView tempHour8 = mView.findViewById(weather_hour8Temp);
        TextView tempHour9 = mView.findViewById(weather_hour9Temp);
        TextView tempHour10 = mView.findViewById(weather_hour10Temp);
        TextView tempHour11 = mView.findViewById(weather_hour11Temp);
        TextView tempHour12 = mView.findViewById(weather_hour12Temp);
        TextView tempHour13 = mView.findViewById(weather_hour13Temp);
        TextView tempHour14 = mView.findViewById(weather_hour14Temp);
        TextView tempHour15 = mView.findViewById(weather_hour15Temp);
        TextView tempHour16 = mView.findViewById(weather_hour16Temp);
        TextView tempHour17 = mView.findViewById(weather_hour17Temp);
        TextView tempHour18 = mView.findViewById(weather_hour18Temp);
        TextView tempHour19 = mView.findViewById(weather_hour19Temp);
        TextView tempHour20 = mView.findViewById(weather_hour20Temp);
        TextView tempHour21 = mView.findViewById(weather_hour21Temp);
        TextView tempHour22 = mView.findViewById(weather_hour22Temp);
        TextView tempHour23 = mView.findViewById(weather_hour23Temp);
        TextView tempHour24 = mView.findViewById(weather_hour24Temp);

        tempHour1.setText(tempFromKelvinToFarenheitString(mWeathers24h[0].getTemp()));
        tempHour2.setText(tempFromKelvinToFarenheitString(mWeathers24h[1].getTemp()));
        tempHour3.setText(tempFromKelvinToFarenheitString(mWeathers24h[2].getTemp()));
        tempHour4.setText(tempFromKelvinToFarenheitString(mWeathers24h[3].getTemp()));
        tempHour5.setText(tempFromKelvinToFarenheitString(mWeathers24h[4].getTemp()));
        tempHour6.setText(tempFromKelvinToFarenheitString(mWeathers24h[5].getTemp()));
        tempHour7.setText(tempFromKelvinToFarenheitString(mWeathers24h[6].getTemp()));
        tempHour8.setText(tempFromKelvinToFarenheitString(mWeathers24h[7].getTemp()));
        tempHour9.setText(tempFromKelvinToFarenheitString(mWeathers24h[8].getTemp()));
        tempHour10.setText(tempFromKelvinToFarenheitString(mWeathers24h[9].getTemp()));
        tempHour11.setText(tempFromKelvinToFarenheitString(mWeathers24h[10].getTemp()));
        tempHour12.setText(tempFromKelvinToFarenheitString(mWeathers24h[11].getTemp()));
        tempHour13.setText(tempFromKelvinToFarenheitString(mWeathers24h[12].getTemp()));
        tempHour14.setText(tempFromKelvinToFarenheitString(mWeathers24h[13].getTemp()));
        tempHour15.setText(tempFromKelvinToFarenheitString(mWeathers24h[14].getTemp()));
        tempHour16.setText(tempFromKelvinToFarenheitString(mWeathers24h[15].getTemp()));
        tempHour17.setText(tempFromKelvinToFarenheitString(mWeathers24h[16].getTemp()));
        tempHour18.setText(tempFromKelvinToFarenheitString(mWeathers24h[17].getTemp()));
        tempHour19.setText(tempFromKelvinToFarenheitString(mWeathers24h[18].getTemp()));
        tempHour20.setText(tempFromKelvinToFarenheitString(mWeathers24h[19].getTemp()));
        tempHour21.setText(tempFromKelvinToFarenheitString(mWeathers24h[20].getTemp()));
        tempHour22.setText(tempFromKelvinToFarenheitString(mWeathers24h[21].getTemp()));
        tempHour23.setText(tempFromKelvinToFarenheitString(mWeathers24h[22].getTemp()));
        tempHour24.setText(tempFromKelvinToFarenheitString(mWeathers24h[23].getTemp()));
    }

    private void setTempDaysToFahrenheit() {
        TextView tempDay1 = mView.findViewById(weather_day1Temp);
        TextView tempDay2 = mView.findViewById(weather_day2Temp);
        TextView tempDay3 = mView.findViewById(weather_day3Temp);
        TextView tempDay4 = mView.findViewById(weather_day4Temp);
        TextView tempDay5 = mView.findViewById(weather_day5Temp);
        TextView tempDay6 = mView.findViewById(weather_day6Temp);
        TextView tempDay7 = mView.findViewById(weather_day7Temp);
        TextView tempDay8 = mView.findViewById(weather_day8Temp);
        TextView tempDay9 = mView.findViewById(weather_day9Temp);
        TextView tempDay10 = mView.findViewById(weather_day10Temp);

        tempDay1.setText(tempFromKelvinToFarenheitString(mWeathers10d[0].getTemp()));
        tempDay2.setText(tempFromKelvinToFarenheitString(mWeathers10d[1].getTemp()));
        tempDay3.setText(tempFromKelvinToFarenheitString(mWeathers10d[2].getTemp()));
        tempDay4.setText(tempFromKelvinToFarenheitString(mWeathers10d[3].getTemp()));
        tempDay5.setText(tempFromKelvinToFarenheitString(mWeathers10d[4].getTemp()));
        tempDay6.setText(tempFromKelvinToFarenheitString(mWeathers10d[5].getTemp()));
        tempDay7.setText(tempFromKelvinToFarenheitString(mWeathers10d[6].getTemp()));
        tempDay8.setText(tempFromKelvinToFarenheitString(mWeathers10d[7].getTemp()));
        tempDay9.setText(tempFromKelvinToFarenheitString(mWeathers10d[8].getTemp()));
        tempDay10.setText(tempFromKelvinToFarenheitString(mWeathers10d[9].getTemp()));
    }

    //TODO
    private String attemptGetZip(double tLat, double tLon) {
        return "";
    }

    /**
     *
     */
    private void attemptSaveWeather() {
        JSONObject msg = new JSONObject();

        try {
            msg.put("email", mEmail);
            msg.put("city", mWeather.getCity());
            msg.put("country", mWeather.getCountry());
            msg.put("lat", mWeather.getLat());
            msg.put("lon", mWeather.getLon());
            msg.put("zip", attemptGetZip(mWeather.getLat(), mWeather.getLon()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_send))
                .build();

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::endOfSaveWeatherTask)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

    /**
     *
     * @param result
     */
    private void endOfSaveWeatherTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);

            if(res.has("success")  && !res.getBoolean("success")) {
                alert("Save unsuccessful", getContext());
            } else {
                alert("Save successful", getContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void gotoSavedWeatherRecyclerView() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_get))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("email", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::endOfGetSavedWeathersTask)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

    /**
     *
     */
    private void attemptGetWeatherZip() {
        boolean success = true;
            EditText et = mView.findViewById(weather_zipEditText);
            String zip = et.getText().toString().trim();

            if (zip.equals("")) {
                success = false;
                et.setError("empty!!");
        }

        if (success) {
            getWeatherZip();
        }
    }

    /**
     *
     */
    @SuppressLint("SetTextI18n")
    private void setWeather() {
        TextView cityText = mView.findViewById(weather_cityCountry);
        TextView condDescr = mView.findViewById(weather_conditonDescription);
        TextView temp = mView.findViewById(weather_temperature);
        TextView hum = mView.findViewById(weather_humidity);
        TextView press = mView.findViewById(weather_pressure);
        TextView windSpeed = mView.findViewById(weather_windSpeed);
        TextView windDeg = mView.findViewById(weather_windDegree);

        cityText.setText(mWeather.getCity() + ", " + mWeather.getCountry());
        condDescr.setText(mWeather.getMain() + "(" + mWeather.getDescription() + ")");
        temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
        hum.setText(mWeather.getHumidity() + "%");
        press.setText(mWeather.getPressure() + " hPa");
        windSpeed.setText(mWeather.getSpeed() + " mps");
        windDeg.setText("" + mWeather.getDeg() + DEGREE);

//        String opw_img_url_start = "http://openweathermap.org/img/wn/";
//        String opw_img_url_big = "@2x";
//        String img_url_end = ".png";
//        String wb_img_url_start = "https://www.weatherbit.io/static/img/icons/";
//        Picasso.get().setLoggingEnabled(true);
//        Picasso.get().load(opw_img_url_start + mWeather.getIcon() + opw_img_url_big)).into(imgView);

        setWeatherImages();

        setTempDaysTextToCelsius();

        setTempHoursTextToCelsius();
    }

    private void setTempHoursTextToCelsius() {
        TextView tempHour1 = mView.findViewById(weather_hour1Temp);
        TextView tempHour2 = mView.findViewById(weather_hour2Temp);
        TextView tempHour3 = mView.findViewById(weather_hour3Temp);
        TextView tempHour4 = mView.findViewById(weather_hour4Temp);
        TextView tempHour5 = mView.findViewById(weather_hour5Temp);
        TextView tempHour6 = mView.findViewById(weather_hour6Temp);
        TextView tempHour7 = mView.findViewById(weather_hour7Temp);
        TextView tempHour8 = mView.findViewById(weather_hour8Temp);
        TextView tempHour9 = mView.findViewById(weather_hour9Temp);
        TextView tempHour10 = mView.findViewById(weather_hour10Temp);
        TextView tempHour11 = mView.findViewById(weather_hour11Temp);
        TextView tempHour12 = mView.findViewById(weather_hour12Temp);
        TextView tempHour13 = mView.findViewById(weather_hour13Temp);
        TextView tempHour14 = mView.findViewById(weather_hour14Temp);
        TextView tempHour15 = mView.findViewById(weather_hour15Temp);
        TextView tempHour16 = mView.findViewById(weather_hour16Temp);
        TextView tempHour17 = mView.findViewById(weather_hour17Temp);
        TextView tempHour18 = mView.findViewById(weather_hour18Temp);
        TextView tempHour19 = mView.findViewById(weather_hour19Temp);
        TextView tempHour20 = mView.findViewById(weather_hour20Temp);
        TextView tempHour21 = mView.findViewById(weather_hour21Temp);
        TextView tempHour22 = mView.findViewById(weather_hour22Temp);
        TextView tempHour23 = mView.findViewById(weather_hour23Temp);
        TextView tempHour24 = mView.findViewById(weather_hour24Temp);

        tempHour1.setText(tempFromKelvinToCelsiusString(mWeathers24h[0].getTemp()));
        tempHour2.setText(tempFromKelvinToCelsiusString(mWeathers24h[1].getTemp()));
        tempHour3.setText(tempFromKelvinToCelsiusString(mWeathers24h[2].getTemp()));
        tempHour4.setText(tempFromKelvinToCelsiusString(mWeathers24h[3].getTemp()));
        tempHour5.setText(tempFromKelvinToCelsiusString(mWeathers24h[4].getTemp()));
        tempHour6.setText(tempFromKelvinToCelsiusString(mWeathers24h[5].getTemp()));
        tempHour7.setText(tempFromKelvinToCelsiusString(mWeathers24h[6].getTemp()));
        tempHour8.setText(tempFromKelvinToCelsiusString(mWeathers24h[7].getTemp()));
        tempHour9.setText(tempFromKelvinToCelsiusString(mWeathers24h[8].getTemp()));
        tempHour10.setText(tempFromKelvinToCelsiusString(mWeathers24h[9].getTemp()));
        tempHour11.setText(tempFromKelvinToCelsiusString(mWeathers24h[10].getTemp()));
        tempHour12.setText(tempFromKelvinToCelsiusString(mWeathers24h[11].getTemp()));
        tempHour13.setText(tempFromKelvinToCelsiusString(mWeathers24h[12].getTemp()));
        tempHour14.setText(tempFromKelvinToCelsiusString(mWeathers24h[13].getTemp()));
        tempHour15.setText(tempFromKelvinToCelsiusString(mWeathers24h[14].getTemp()));
        tempHour16.setText(tempFromKelvinToCelsiusString(mWeathers24h[15].getTemp()));
        tempHour17.setText(tempFromKelvinToCelsiusString(mWeathers24h[16].getTemp()));
        tempHour18.setText(tempFromKelvinToCelsiusString(mWeathers24h[17].getTemp()));
        tempHour19.setText(tempFromKelvinToCelsiusString(mWeathers24h[18].getTemp()));
        tempHour20.setText(tempFromKelvinToCelsiusString(mWeathers24h[19].getTemp()));
        tempHour21.setText(tempFromKelvinToCelsiusString(mWeathers24h[20].getTemp()));
        tempHour22.setText(tempFromKelvinToCelsiusString(mWeathers24h[21].getTemp()));
        tempHour23.setText(tempFromKelvinToCelsiusString(mWeathers24h[22].getTemp()));
        tempHour24.setText(tempFromKelvinToCelsiusString(mWeathers24h[23].getTemp()));
    }

    private void setTempDaysTextToCelsius() {
        TextView tempDay1 = mView.findViewById(weather_day1Temp);
        TextView tempDay2 = mView.findViewById(weather_day2Temp);
        TextView tempDay3 = mView.findViewById(weather_day3Temp);
        TextView tempDay4 = mView.findViewById(weather_day4Temp);
        TextView tempDay5 = mView.findViewById(weather_day5Temp);
        TextView tempDay6 = mView.findViewById(weather_day6Temp);
        TextView tempDay7 = mView.findViewById(weather_day7Temp);
        TextView tempDay8 = mView.findViewById(weather_day8Temp);
        TextView tempDay9 = mView.findViewById(weather_day9Temp);
        TextView tempDay10 = mView.findViewById(weather_day10Temp);

        tempDay1.setText(tempFromKelvinToCelsiusString(mWeathers10d[0].getTemp()));
        tempDay2.setText(tempFromKelvinToCelsiusString(mWeathers10d[1].getTemp()));
        tempDay3.setText(tempFromKelvinToCelsiusString(mWeathers10d[2].getTemp()));
        tempDay4.setText(tempFromKelvinToCelsiusString(mWeathers10d[3].getTemp()));
        tempDay5.setText(tempFromKelvinToCelsiusString(mWeathers10d[4].getTemp()));
        tempDay6.setText(tempFromKelvinToCelsiusString(mWeathers10d[5].getTemp()));
        tempDay7.setText(tempFromKelvinToCelsiusString(mWeathers10d[6].getTemp()));
        tempDay8.setText(tempFromKelvinToCelsiusString(mWeathers10d[7].getTemp()));
        tempDay9.setText(tempFromKelvinToCelsiusString(mWeathers10d[8].getTemp()));
        tempDay10.setText(tempFromKelvinToCelsiusString(mWeathers10d[9].getTemp()));
    }

    private void setWeatherImages() {
        ImageView imgView = mView.findViewById(weather_conditionIcon);
        Picasso.get().load(getImgUrl(mWeather.getIcon())).into(imgView);

        ImageView imgViewDay1 = mView.findViewById(weather_Day1Img);
        ImageView imgViewDay2 = mView.findViewById(weather_Day2Img);
        ImageView imgViewDay3 = mView.findViewById(weather_Day3Img);
        ImageView imgViewDay4 = mView.findViewById(weather_Day4Img);
        ImageView imgViewDay5 = mView.findViewById(weather_Day5Img);
        ImageView imgViewDay6 = mView.findViewById(weather_Day6Img);
        ImageView imgViewDay7 = mView.findViewById(weather_Day7Img);
        ImageView imgViewDay8 = mView.findViewById(weather_Day8Img);
        ImageView imgViewDay9 = mView.findViewById(weather_Day9Img);
        ImageView imgViewDay10 = mView.findViewById(weather_Day10Img);

        Picasso.get().load(getImgUrl(mWeathers10d[0].getIcon()))
                .into(imgViewDay1);
        Picasso.get().load(getImgUrl(mWeathers10d[1].getIcon()))
                .into(imgViewDay2);
        Picasso.get().load(getImgUrl(mWeathers10d[2].getIcon()))
                .into(imgViewDay3);
        Picasso.get().load(getImgUrl(mWeathers10d[3].getIcon()))
                .into(imgViewDay4);
        Picasso.get().load(getImgUrl(mWeathers10d[4].getIcon()))
                .into(imgViewDay5);
        Picasso.get().load(getImgUrl(mWeathers10d[5].getIcon()))
                .into(imgViewDay6);
        Picasso.get().load(getImgUrl(mWeathers10d[6].getIcon()))
                .into(imgViewDay7);
        Picasso.get().load(getImgUrl(mWeathers10d[7].getIcon()))
                .into(imgViewDay8);
        Picasso.get().load(getImgUrl(mWeathers10d[8].getIcon()))
                .into(imgViewDay9);
        Picasso.get().load(getImgUrl(mWeathers10d[9].getIcon()))
                .into(imgViewDay10);

        ImageView imgViewHour1 = mView.findViewById(weather_Hour1Img);
        ImageView imgViewHour2 = mView.findViewById(weather_Hour2Img);
        ImageView imgViewHour3 = mView.findViewById(weather_Hour3Img);
        ImageView imgViewHour4 = mView.findViewById(weather_Hour4Img);
        ImageView imgViewHour5 = mView.findViewById(weather_Hour5Img);
        ImageView imgViewHour6 = mView.findViewById(weather_Hour6Img);
        ImageView imgViewHour7 = mView.findViewById(weather_Hour7Img);
        ImageView imgViewHour8 = mView.findViewById(weather_Hour8Img);
        ImageView imgViewHour9 = mView.findViewById(weather_Hour9Img);
        ImageView imgViewHour10 = mView.findViewById(weather_Hour10Img);
        ImageView imgViewHour11 = mView.findViewById(weather_Hour11Img);
        ImageView imgViewHour12 = mView.findViewById(weather_Hour12Img);
        ImageView imgViewHour13 = mView.findViewById(weather_Hour13Img);
        ImageView imgViewHour14 = mView.findViewById(weather_Hour14Img);
        ImageView imgViewHour15 = mView.findViewById(weather_Hour15Img);
        ImageView imgViewHour16 = mView.findViewById(weather_Hour16Img);
        ImageView imgViewHour17 = mView.findViewById(weather_Hour17Img);
        ImageView imgViewHour18 = mView.findViewById(weather_Hour18Img);
        ImageView imgViewHour19 = mView.findViewById(weather_Hour19Img);
        ImageView imgViewHour20 = mView.findViewById(weather_Hour20Img);
        ImageView imgViewHour21 = mView.findViewById(weather_Hour21Img);
        ImageView imgViewHour22 = mView.findViewById(weather_Hour22Img);
        ImageView imgViewHour23 = mView.findViewById(weather_Hour23Img);
        ImageView imgViewHour24 = mView.findViewById(weather_Hour24Img);


        Picasso.get().load(getImgUrl(mWeathers24h[0].getIcon()))
                .into(imgViewHour1);
        Picasso.get().load(getImgUrl(mWeathers24h[1].getIcon()))
                .into(imgViewHour2);
        Picasso.get().load(getImgUrl(mWeathers24h[2].getIcon()))
                .into(imgViewHour3);
        Picasso.get().load(getImgUrl(mWeathers24h[3].getIcon()))
                .into(imgViewHour4);
        Picasso.get().load(getImgUrl(mWeathers24h[4].getIcon()))
                .into(imgViewHour5);
        Picasso.get().load(getImgUrl(mWeathers24h[5].getIcon()))
                .into(imgViewHour6);
        Picasso.get().load(getImgUrl(mWeathers24h[6].getIcon()))
                .into(imgViewHour7);
        Picasso.get().load(getImgUrl(mWeathers24h[7].getIcon()))
                .into(imgViewHour8);
        Picasso.get().load(getImgUrl(mWeathers24h[8].getIcon()))
                .into(imgViewHour9);
        Picasso.get().load(getImgUrl(mWeathers24h[9].getIcon()))
                .into(imgViewHour10);
        Picasso.get().load(getImgUrl(mWeathers24h[10].getIcon()))
                .into(imgViewHour11);
        Picasso.get().load(getImgUrl(mWeathers24h[11].getIcon()))
                .into(imgViewHour12);
        Picasso.get().load(getImgUrl(mWeathers24h[12].getIcon()))
                .into(imgViewHour13);
        Picasso.get().load(getImgUrl(mWeathers24h[13].getIcon()))
                .into(imgViewHour14);
        Picasso.get().load(getImgUrl(mWeathers24h[14].getIcon()))
                .into(imgViewHour15);
        Picasso.get().load(getImgUrl(mWeathers24h[15].getIcon()))
                .into(imgViewHour16);
        Picasso.get().load(getImgUrl(mWeathers24h[16].getIcon()))
                .into(imgViewHour17);
        Picasso.get().load(getImgUrl(mWeathers24h[17].getIcon()))
                .into(imgViewHour18);
        Picasso.get().load(getImgUrl(mWeathers24h[18].getIcon()))
                .into(imgViewHour19);
        Picasso.get().load(getImgUrl(mWeathers24h[19].getIcon()))
                .into(imgViewHour20);
        Picasso.get().load(getImgUrl(mWeathers24h[20].getIcon()))
                .into(imgViewHour21);
        Picasso.get().load(getImgUrl(mWeathers24h[21].getIcon()))
                .into(imgViewHour22);
        Picasso.get().load(getImgUrl(mWeathers24h[22].getIcon()))
                .into(imgViewHour23);
        Picasso.get().load(getImgUrl(mWeathers24h[23].getIcon()))
                .into(imgViewHour24, new Callback() {
                    @Override
                    public void onSuccess() {
                        mView.findViewById(layout_weather_wait).setVisibility(View.GONE);
                        mView.findViewById(weather_saveButton).setVisibility(View.VISIBLE);
                        mView.findViewById(weather_temperatureSwitch).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

//    private String getImgUrl(String tIcon) {
//        if (tIcon.equals("50d")) {
//            return "http://openweathermap.org/img/wn/" + tIcon + "@2x.png";
//        } else {
//            return "https://www.weatherbit.io/static/img/icons/" + tIcon + ".png";
//        }
//    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(layout.fragment_weather, container, false);
    }

    /**
     *
     */
    private void getWeatherZip() {
        String zip = ((EditText)mView.findViewById(weather_zipEditText)).getText().toString().trim();

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_zip))
                .build();

        Uri uri2 = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_zip))
                .appendPath(getString(ep_10d))
                .build();

        Uri uri3 = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_zip))
                .appendPath(getString(ep_24h))
                .build();

        JSONObject msg = new JSONObject();
        try {
            msg.put("zip", zip);
        } catch (JSONException e) {
            Log.wtf("zip", "Error creating JSON: " + e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::endOfGetWeatherTask)
                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();

        new SendPostAsyncTask.Builder(uri2.toString(), msg)
                .onPostExecute(this::endOfGetWeathers10dTask)
                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();

        new SendPostAsyncTask.Builder(uri3.toString(), msg)
                .onPostExecute(this::endOfGetWeathers24hTask)
                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

    private void endOfGetWeathers10dTask(String result) {
        try {
            boolean hasData = false;
            JSONObject root = new JSONObject(result);
            if (root.has(getString(keys_json_data))) {
                hasData = true;
            } else {
                Log.e("ERROR!", "No data");
            }

            if (hasData) {
                JSONArray dataJArray = root.getJSONArray(
                        getString(keys_json_data));

                for (int i = 0; i < 10; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    JSONObject weatherJObject = dataJSONObject.getJSONObject(
                            getString(keys_json_weather));
                    Weather weather = new Weather(getNewIcon(weatherJObject.getString(getString(keys_json_icon)))
                            , dataJSONObject.getDouble(getString(keys_json_temp)) +  273.15 );
                    mWeathers10d[i] = weather;
                }

            } else {
                alert("Can't load 24-h weathers", getContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void endOfGetWeathers24hTask(final String result) {
        try {
            boolean hasHourly = false;
            JSONObject root = new JSONObject(result);
            if (root.has(getString(keys_json_hourly))) {
                hasHourly = true;
            } else {
                Log.e("ERROR!", "No hourly");
            }

            if (hasHourly) {
                JSONObject hourlyJObject = root.getJSONObject(
                        getString(keys_json_hourly));
                JSONArray dataJArray = hourlyJObject.getJSONArray(
                        getString(keys_json_data));

                for (int i = 0; i < 24; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    Weather weather = new Weather(getNewIcon(dataJSONObject.getString(
                            getString(keys_json_icon)))
                            , ((dataJSONObject.getDouble(getString(keys_json_temperature))
                            - 32) * 5 / 9) + 273.15);
                    mWeathers24h[i] = weather;
                }
                setWeather();
            } else {
                alert("Can't load 24-h forecast", getContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
            alert("Can't load 24-h forecast", getContext());
        }
    }

    /**
     *
     * @param result
     */
    private void endOfGetSavedWeathersTask(final String result) {
//        Log.d("GETSAVED", "success");
        Log.d(TAG, result);
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.get("success").equals("false")) {
                alert("getting weathers failed", getContext());
            }
            JSONArray messages = root.getJSONArray(getString(keys_json_messages));

            Location[] locations = new Location[messages.length()];
            for(int i = 0; i < locations.length; i++) {
                JSONObject message = messages.getJSONObject(i);
                locations[i] = new Location(message.getDouble(getString(keys_json_long))
                    , message.getDouble(getString(keys_json_lat))
                    , message.getString(getString(keys_json_nickname)));
            }

            WeatherFragmentDirections.ActionNavWeatherToNavLocations action =
                    WeatherFragmentDirections.actionNavWeatherToNavLocations(
                            locations, mEmail, mJwToken);
            Navigation.findNavController(Objects.requireNonNull(getView())).navigate(action);
//            final Bundle args = new Bundle();
//            args.putSerializable(getString(R.string.key_blog_post_view), theBlogPost);
//            Navigation.findNavController(getView())
//                    .navigate(R.id.action_nav_weather_to_nav_locations, args);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param result
     */
    private void endOfGetWeatherTask(final String result) {
        try {
            boolean hasWeather = false;
            boolean hasMain = false;
            boolean hasWind = false;
            boolean hasCoord = false;
            boolean hasSys = false;
            boolean hasName = false;
            JSONObject root = new JSONObject(result);
            if (root.has(getString(keys_json_weather))) {
                hasWeather = true;
            } else {
                Log.e("ERROR!", "No weather");
            }
            if (root.has(getString(keys_json_main))) {
                hasMain = true;
            } else {
                Log.e("ERROR!", "No main");
            }
            if (root.has(getString(keys_json_wind))) {
                hasWind = true;
            } else {
                Log.e("ERROR!", "No wind");
            }
            if (root.has(getString(keys_json_coord))) {
                hasCoord = true;
            } else {
                Log.e("ERROR!", "No coord");
            }
            if (root.has(getString(keys_json_name))) {
                hasName = true;
            } else {
                Log.e("ERROR!", "No name");
            }
            if (root.has(getString(keys_json_sys))) {
                hasSys = true;
            } else {
                Log.e("ERROR!", "No sys");
            }

            if (hasCoord && hasMain && hasName && hasSys && hasWeather && hasWind) {
                JSONArray weatherJArray = root.getJSONArray(
                        getString(keys_json_weather));
                JSONObject mainJObject = root.getJSONObject(
                        getString(keys_json_main));
                String nameString = root.getString(
                        getString(keys_json_name));
                JSONObject sysJObject = root.getJSONObject(
                        getString(keys_json_sys));
                JSONObject coordJObject = root.getJSONObject(
                        getString(keys_json_coord));
                JSONObject windJObject = root.getJSONObject(
                        getString(keys_json_wind));

                JSONObject weatherJObject = weatherJArray.getJSONObject(0);

                mWeather = new Weather(
                        weatherJObject.getString(getString(
                                keys_json_description))
                        , getNewIcon(weatherJObject.getString(getString(
                        keys_json_icon)))
                        , coordJObject.getDouble(getString(
                        keys_json_lon))
                        , coordJObject.getDouble(getString(
                        keys_json_lat))
                        , mainJObject.getDouble(getString(
                        keys_json_temp))
                        , mainJObject.getInt(getString(
                        keys_json_pressure))
                        , mainJObject.getInt(getString(
                        keys_json_humidity))
                        , mainJObject.getDouble(getString(
                        keys_json_temp_min))
                        , mainJObject.getDouble(getString(
                        keys_json_temp_max))
                        , windJObject.getDouble(getString(
                        keys_json_speed))
                        , nameString);
                        /*, mWeather.getJwt()*/

                if (windJObject.has(getString(keys_json_deg))) {
                    mWeather.setDeg(windJObject.getDouble(getString(keys_json_deg)));
                }

                if (sysJObject.has(getString(keys_json_country))) {
                    mWeather.setCountry(sysJObject.getString(getString(
                            keys_json_country)));
                }

                if (weatherJObject.has(getString(keys_json_main))) {
                    mWeather.setMain(weatherJObject.getString(getString(keys_json_main)));
                }

            } else {
//                alert("Not a valid zip code");
                ((EditText) mView.findViewById(weather_zipEditText))
                    .setError("Not a valid zip code");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }
}