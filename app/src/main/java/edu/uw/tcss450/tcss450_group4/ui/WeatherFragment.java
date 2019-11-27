package edu.uw.tcss450.tcss450_group4.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import edu.uw.tcss450.tcss450_group4.model.Location;
import edu.uw.tcss450.tcss450_group4.model.State;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.model.WeatherHelper;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static android.graphics.Color.BLACK;
import static edu.uw.tcss450.tcss450_group4.R.anim;
import static edu.uw.tcss450.tcss450_group4.R.color.redviolet;
import static edu.uw.tcss450.tcss450_group4.R.color.uwPurple;
import static edu.uw.tcss450.tcss450_group4.R.id.*;
import static edu.uw.tcss450.tcss450_group4.R.layout;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_get;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_rows;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_send;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_weather;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_count;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_country;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_data;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_deg;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_description;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_hourly;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_humidity;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_icon;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_lat;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_latitude;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_long;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_longitude;
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
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_timezone;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_weather;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_wind;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.alert;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getImgUrl;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getNewIcon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather10dZip;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather24hZip;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeatherCurrentZip;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.sendPostAsyncTaskHelper;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.tempFromKelvinToCelsiusString;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.tempFromKelvinToFahrenheitString;

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
    private Weather mHomeWeather;
    private Weather[] mHomeWeathers10d;
    private Weather[] mHomeWeathers24h;
    private View mView;
    private String mEmail;
    private String mJwToken;
    private FloatingActionButton mFab_main, mFab_getSavedWeather, mFab_gotoLocations
            , mFab_saveWeather, mFab_getCurrentLocationWeather;
    private Animation mFab_open, mFab_close, mFab_clock, mFab_anticlock;
    private boolean mIsOpen;
    private boolean isCountryNull;

    private int mTempLocationsCount;
    private int mLocationsCount;
    private boolean mRowsUpdated;
    private boolean mIfWeatherSuccess;
//    private int mTempLocationsNum;
//    private boolean mSuccessSave;
//    private int mCount;
//    private boolean mAlertSave;

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialization(view);
        setComponents();
    }

    /**
     *
     * @param view
     */
    private void initialization(@NonNull View view) {

        WeatherFragmentArgs args = null;
        if (getArguments() != null) {
            args = WeatherFragmentArgs.fromBundle(getArguments());
        }
        if (args != null) {
            if (mWeather == null) {
                mEmail = args.getEmail();
                mJwToken = args.getJwt();
                mWeather = args.getWeather();
                mWeathers10d = args.getWeathers10d();
                mWeathers24h = args.getWeathers24h();
                mHomeWeather = args.getWeatherHome();
                mHomeWeathers10d = args.getWeathersHome10d();
                mHomeWeathers24h = args.getWeathersHome24h();
            }
        }
        mView = view;
        if (!mRowsUpdated) {
            getRowsWeather();
        }
    }

    /**
     * set components actions
     */
    private void setComponents() {
        //TODO don't need to always do, just once
        mView.findViewById(layout_weather_clickable).setOnClickListener(e -> toggleMainFab());
        View zipView = mView.findViewById(weather_zipEditText);
        zipView.setOnKeyListener((v, keyCode, event) ->
                zipKeyListener(v, keyCode));
//        zipView.setOnClickListener(e ->
//                setToast("Get the current weather condition and forecasts of the given zip code"));
        zipView.setOnLongClickListener(e ->
                setToast("Get the current weather condition and forecasts of the given zip code"));
        zipView.setOnClickListener(e -> ifFabOpenCloseIt());
        zipView.setOnFocusChangeListener((v, hasFocus) -> ifFabOpenCloseIt());
//        mView.findViewById(weather_getZipButton).setOnLongClickListener(v ->
//                setToast("Get the current weather condition and forecasts of the given zip code"));
//        mView.findViewById(weather_getZipButton).setOnClickListener(v -> attemptGetWeatherZip());

        mFab_saveWeather = mView.findViewById(weather_saveButton);
        mFab_saveWeather.setOnLongClickListener(v ->
                setToast("Save the current location"));
        mFab_saveWeather.setOnClickListener(v -> attemptSaveWeather());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mView.findViewById(weather_saveButton)
//                    .setTooltipText("Save Weather");
//        }
        mFab_getSavedWeather = mView.findViewById(weather_getSavedWeathersButton);
        mFab_getSavedWeather.setOnClickListener(v ->
                gotoSavedWeatherRecyclerView());
        mFab_getSavedWeather.setOnLongClickListener(v ->
                setToast("Get the list of saved locations"));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mView.findViewById(weather_getSavedWeathersButton)
//                    .setTooltipText("Get Saved Weathers");
//        }
        ((Switch)mView.findViewById(weather_temperatureSwitch)).setOnCheckedChangeListener(
                (buttonView, isChecked) -> setTemperature());
        ((Switch)mView.findViewById(weather_forecastSwitch)).setOnCheckedChangeListener(
                (buttonView, isChecked) -> switchForecast());
        mFab_gotoLocations = mView.findViewById(weather_getLocationButton);
        mFab_gotoLocations.setOnClickListener(e -> gotoMap());
        mFab_gotoLocations.setOnLongClickListener(v ->
                setToast("Get the current weather condition and forecasts from the chosen location"));
        mFab_main = mView.findViewById(weather_mainFab);
        mFab_main.setOnClickListener(v -> toggleMainFab());
        mFab_main.setOnLongClickListener(v -> setToast("Actions"));
        mFab_getCurrentLocationWeather = mView.findViewById(weather_getCurrentLocationWeatherButton);
        mFab_getCurrentLocationWeather.setOnClickListener(e -> getCurrentLocationWeather());
        mFab_getCurrentLocationWeather.setOnLongClickListener(v ->
                setToast("Get the current weather condition and forecasts from your current location"));
        setWeather();
    }

    public boolean zipKeyListener(View tView, int tKey) {
        if (tKey == KeyEvent.KEYCODE_ENTER || tKey == KeyEvent.KEYCODE_DPAD_CENTER) {
            attemptGetWeatherZip();
            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(tView.getWindowToken(), 0);
            return true;
        } else {
            ifFabOpenCloseIt();
        }
        return false;
    }

    /**
     * get current location weather and set the view
     */
    private void getCurrentLocationWeather() {
        mWeather = mHomeWeather;
        mWeathers10d = mHomeWeathers10d.clone();
        mWeathers24h = mHomeWeathers24h.clone();

        setWeather();
    }

    /**
     * toggle the main fab
     */
    @SuppressLint("RestrictedApi")
    private void toggleMainFab() {
        if (mIsOpen) {
            mFab_gotoLocations.setVisibility(View.GONE);
            mFab_saveWeather.setVisibility(View.GONE);
            mFab_getSavedWeather.setVisibility(View.GONE);
            mFab_getCurrentLocationWeather.setVisibility(View.GONE);
            mFab_gotoLocations.startAnimation(mFab_close);
            mFab_saveWeather.startAnimation(mFab_close);
            mFab_getSavedWeather.startAnimation(mFab_close);
            mFab_getCurrentLocationWeather.startAnimation(mFab_close);
            mFab_main.startAnimation(mFab_anticlock);
            mIsOpen = false;
            mView.findViewById(layout_weather_clickable).setVisibility(View.GONE);
        } else {
            mFab_gotoLocations.setVisibility(View.VISIBLE);
            mFab_saveWeather.setVisibility(View.VISIBLE);
            mFab_getSavedWeather.setVisibility(View.VISIBLE);
            mFab_getCurrentLocationWeather.setVisibility(View.VISIBLE);
            mFab_gotoLocations.startAnimation(mFab_open);
            mFab_saveWeather.startAnimation(mFab_open);
            mFab_getSavedWeather.startAnimation(mFab_open);
            mFab_getCurrentLocationWeather.startAnimation(mFab_open);
            mFab_main.startAnimation(mFab_clock);
            mIsOpen = true;
            mView.findViewById(layout_weather_clickable).setVisibility(View.VISIBLE);
        }
    }

    /**
     * When map fab is clicked, go to the map
     */
    private void gotoMap() {
        ifFabOpenCloseIt();
        WeatherFragmentDirections.ActionNavWeatherToNavMap action =
                WeatherFragmentDirections.actionNavWeatherToNavMap(mEmail, mJwToken, mHomeWeather
                        , mHomeWeathers10d, mHomeWeathers24h);
        Navigation.findNavController(Objects.requireNonNull(getView())).navigate(action);
    }

    /**
     *
     * @param tString the toast to display
     * @return true for long clicks
     */
    private boolean setToast(String tString) {
        Toast.makeText(getContext(),
                tString,
                Toast.LENGTH_LONG).show();
        return  true;
    }

    /**
     * switch forecast from 10 days to 24 hours
     */
    private void switchForecast() {
        ifFabOpenCloseIt();
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

    private void ifFabOpenCloseIt() {
        if (mIsOpen) {
            toggleMainFab();
        }
    }

//    /**
//     *
//     * @param tTimeZone the given timezone
//     * @return the calendar from the given timezone + the location's timezone difference with UTC
//     */
//    private Calendar getNewTimzone(TimeZone tTimeZone) {
//        Calendar calendar = Calendar.getInstance(tTimeZone);
//        Log.d("UTCbefore", calendar.getTime().toString());
//        calendar.setTimeInMillis(calendar.getTimeInMillis() + (mWeather.getTimezone() * 1000));
//        Log.d("UTCafter", calendar.getTime().toString());
//        return calendar;
//    }

    /**
     * set hours components
     */
    private void setHours() {
//        TimeZone utc = TimeZone.getTimeZone("UTC");
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
//                = new SimpleDateFormat("h a");
//        sdf.setTimeZone(utc);
//        Calendar calendar = getNewTimzone(utc);
//        calendar.setTimeZone();//TODO timezone?
        TimeZone timeZone = TimeZone.getTimeZone(mWeather.getTimezoneID());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                = new SimpleDateFormat("h a");
        sdf.setTimeZone(timeZone);
        Calendar calendar = Calendar.getInstance(timeZone);

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

    /**
     * set days components
     */
    private void setDays() {
        // EEE MM/dd
//        TimeZone utc = TimeZone.getTimeZone("UTC");
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
//                = new SimpleDateFormat("MM/dd");
//        Calendar calendar = getNewTimzone(utc);
//        calendar.setTimeZone();

        TimeZone timeZone = TimeZone.getTimeZone(mWeather.getTimezoneID());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                = new SimpleDateFormat("MM/dd");
        sdf.setTimeZone(timeZone);
        Calendar calendar = Calendar.getInstance(timeZone);
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

    private void setTemperature() {
        ifFabOpenCloseIt();
        TextView temp = mView.findViewById(weather_temperature);
        if (((Switch) mView.findViewById(weather_temperatureSwitch)).isChecked()) {
            temp.setText(tempFromKelvinToFahrenheitString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), redviolet));

            setTempDaysTextToFahrenheit();
            setTempHoursTextToFahrenheit();

        } else {
            temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));

            setTempDaysTextToCelsius();
            setTempHoursTextToCelsius();
        }
    }

    private void setTempHoursTextToFahrenheit() {
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

        tempHour1.setText(tempFromKelvinToFahrenheitString(mWeathers24h[0].getTemp()));
        tempHour2.setText(tempFromKelvinToFahrenheitString(mWeathers24h[1].getTemp()));
        tempHour3.setText(tempFromKelvinToFahrenheitString(mWeathers24h[2].getTemp()));
        tempHour4.setText(tempFromKelvinToFahrenheitString(mWeathers24h[3].getTemp()));
        tempHour5.setText(tempFromKelvinToFahrenheitString(mWeathers24h[4].getTemp()));
        tempHour6.setText(tempFromKelvinToFahrenheitString(mWeathers24h[5].getTemp()));
        tempHour7.setText(tempFromKelvinToFahrenheitString(mWeathers24h[6].getTemp()));
        tempHour8.setText(tempFromKelvinToFahrenheitString(mWeathers24h[7].getTemp()));
        tempHour9.setText(tempFromKelvinToFahrenheitString(mWeathers24h[8].getTemp()));
        tempHour10.setText(tempFromKelvinToFahrenheitString(mWeathers24h[9].getTemp()));
        tempHour11.setText(tempFromKelvinToFahrenheitString(mWeathers24h[10].getTemp()));
        tempHour12.setText(tempFromKelvinToFahrenheitString(mWeathers24h[11].getTemp()));
        tempHour13.setText(tempFromKelvinToFahrenheitString(mWeathers24h[12].getTemp()));
        tempHour14.setText(tempFromKelvinToFahrenheitString(mWeathers24h[13].getTemp()));
        tempHour15.setText(tempFromKelvinToFahrenheitString(mWeathers24h[14].getTemp()));
        tempHour16.setText(tempFromKelvinToFahrenheitString(mWeathers24h[15].getTemp()));
        tempHour17.setText(tempFromKelvinToFahrenheitString(mWeathers24h[16].getTemp()));
        tempHour18.setText(tempFromKelvinToFahrenheitString(mWeathers24h[17].getTemp()));
        tempHour19.setText(tempFromKelvinToFahrenheitString(mWeathers24h[18].getTemp()));
        tempHour20.setText(tempFromKelvinToFahrenheitString(mWeathers24h[19].getTemp()));
        tempHour21.setText(tempFromKelvinToFahrenheitString(mWeathers24h[20].getTemp()));
        tempHour22.setText(tempFromKelvinToFahrenheitString(mWeathers24h[21].getTemp()));
        tempHour23.setText(tempFromKelvinToFahrenheitString(mWeathers24h[22].getTemp()));
        tempHour24.setText(tempFromKelvinToFahrenheitString(mWeathers24h[23].getTemp()));
    }

    private void setTempDaysTextToFahrenheit() {
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

        tempDay1.setText(tempFromKelvinToFahrenheitString(mWeathers10d[0].getTemp()));
        tempDay2.setText(tempFromKelvinToFahrenheitString(mWeathers10d[1].getTemp()));
        tempDay3.setText(tempFromKelvinToFahrenheitString(mWeathers10d[2].getTemp()));
        tempDay4.setText(tempFromKelvinToFahrenheitString(mWeathers10d[3].getTemp()));
        tempDay5.setText(tempFromKelvinToFahrenheitString(mWeathers10d[4].getTemp()));
        tempDay6.setText(tempFromKelvinToFahrenheitString(mWeathers10d[5].getTemp()));
        tempDay7.setText(tempFromKelvinToFahrenheitString(mWeathers10d[6].getTemp()));
        tempDay8.setText(tempFromKelvinToFahrenheitString(mWeathers10d[7].getTemp()));
        tempDay9.setText(tempFromKelvinToFahrenheitString(mWeathers10d[8].getTemp()));
        tempDay10.setText(tempFromKelvinToFahrenheitString(mWeathers10d[9].getTemp()));
    }

//    //TODO
//    private String attemptGetZip(double tLat, double tLon) {
//        return "";
//    }

    /**
     *
     */
    private void attemptSaveWeather() {
        JSONObject msg = new JSONObject();

        try {
            msg.put("email", mEmail);
            if (mWeather.getCity() != null) {
                msg.put("city", mWeather.getCity());
            }
            if (mWeather.getState() == null) {
                msg.put("country", mWeather.getCountry());
            } else {
                msg.put("country", mWeather.getState() + ", " + mWeather.getCountry());
            }
            msg.put("lat", mWeather.getLat());
            msg.put("lon", mWeather.getLon());
            msg.put("zip", mWeather.getZip());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_send))
                .build();

        sendPostAsyncTaskHelper(uri, msg, this::endOfSaveWeatherTask, mJwToken);
    }

    private void getRowsWeather() {
        Uri uri = new Uri.Builder()
            .scheme("https")
            .appendPath(getString(ep_base_url))
            .appendPath(getString(ep_weather))
            .appendPath(getString(ep_get))
            .appendPath(getString(ep_rows))
            .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("email", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
            .onPostExecute(this::endOfGetRowsWeather)
                .onCancelled(error -> Log.e(TAG, error))
            .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    private void endOfGetRowsWeather(final String result) {
        try {
            Log.d(TAG, result);
            JSONObject root = new JSONObject(result);

            mLocationsCount = root.getInt(getString(keys_json_count));
            if (!mRowsUpdated) {
                mTempLocationsCount = mLocationsCount;
                mRowsUpdated = true;
            } else {
                if (mLocationsCount == mTempLocationsCount + 1) {
                    alert("Save successful", getContext());
                    mTempLocationsCount++;
                } else if (mLocationsCount == mTempLocationsCount) {
                    alert("That location has already been saved", getContext());
                } else if (mTempLocationsCount != 0) {
                    alert("Unknown if location is saved. Problem occurred\n" +
                            "Past location count is " + mTempLocationsCount +
                            " and new location count is " + mLocationsCount, getContext());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                Log.d("savefailed", result);
            } else {
                getRowsWeather();
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

        sendPostAsyncTaskHelper(uri, msg, this::endOfGetSavedWeathersTask, mJwToken);
//        new SendPostAsyncTask.Builder(uri.toString(), msg)
//                .onPostExecute(this::endOfGetSavedWeathersTask)
//                .onCancelled(error -> Log.e(TAG, error))
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();
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
    private void setWeather() {
        mWeathers24h[0].setTemp(mWeather.getTemp());
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(mWeather.getLat(), mWeather.getLon(), 1);
            mWeather.setZip(addresses.get(0).getPostalCode());
            mWeather.setCity(addresses.get(0).getLocality());
            mWeather.setState(addresses.get(0).getAdminArea());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        mView.findViewById(layout_weather_wait).setVisibility(View.VISIBLE);
//        mView.findViewById(weather_saveButton).setVisibility(View.INVISIBLE); TODO check
        mView.findViewById(weather_temperatureSwitch).setVisibility(View.GONE);
        setHours();
        setDays();
        TextView cityText = mView.findViewById(weather_cityCountry);
        TextView condDescr = mView.findViewById(weather_conditonDescription);
        TextView temp = mView.findViewById(weather_temperature);
        TextView hum = mView.findViewById(weather_humidity);
        TextView press = mView.findViewById(weather_pressure);
        TextView windSpeed = mView.findViewById(weather_windSpeed);
        TextView windDeg = mView.findViewById(weather_windDegree);

        if (mWeather.getCountry() == null) {
            mWeather.setCountry("" + mWeather.getLat());
            mWeather.setCity("" + mWeather.getLon());
        }

        if (mWeather.getState() == null) {
            if(mWeather.getCity() == null) {
                cityText.setText(String.format("%s", mWeather.getCountry()));
            } else {
                cityText.setText(String.format("%s, %s", mWeather.getCity(), mWeather.getCountry()));
            }
        } else {
            if (State.valueOfName(mWeather.getState()) != State.UNKNOWN) {
                mWeather.setState(State.valueOfName(mWeather.getState()).getAbbreviation());
            }
            if (mWeather.getCity() == null) {
                cityText.setText(String.format("%s, %s"
                        , mWeather.getState()
                        , mWeather.getCountry()));
            } else {
                cityText.setText(String.format("%s, %s, %s", mWeather.getCity()
                        , mWeather.getState()
                        , mWeather.getCountry()));
            }
        }
        condDescr.setText(String.format("%s(%s)", mWeather.getMain(), mWeather.getDescription()));
        temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
        hum.setText(String.format("%s%%", mWeather.getHumidity()));
        press.setText(String.format("%s hPa", mWeather.getPressure()));
        windSpeed.setText(String.format("%s mps", mWeather.getSpeed()));
        windDeg.setText(String.format("%s%s", mWeather.getDeg(), DEGREE));

        setWeatherImages();
        setTemperature();//needed instead of below because of issue when
        //on Fahrenheit then get weather from zip
//        setTempDaysTextToCelsius();
//
//        setTempHoursTextToCelsius();
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

    //TODO
    private void setWeatherImages() {
        ImageView imgView = mView.findViewById(weather_conditionIcon);
        Picasso.get().load(getImgUrl(mWeather.getIcon())).resize(120, 120)
                .into(imgView);

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

        Picasso.get().load(getImgUrl(mWeathers10d[0].getIcon())).resize(120, 120)
                .into(imgViewDay1);
        Picasso.get().load(getImgUrl(mWeathers10d[1].getIcon())).resize(120, 120)
                .into(imgViewDay2);
        Picasso.get().load(getImgUrl(mWeathers10d[2].getIcon())).resize(120, 120)
                .into(imgViewDay3);
        Picasso.get().load(getImgUrl(mWeathers10d[3].getIcon())).resize(120, 120)
                .into(imgViewDay4);
        Picasso.get().load(getImgUrl(mWeathers10d[4].getIcon())).resize(120, 120)
                .into(imgViewDay5);
        Picasso.get().load(getImgUrl(mWeathers10d[5].getIcon())).resize(120, 120)
                .into(imgViewDay6);
        Picasso.get().load(getImgUrl(mWeathers10d[6].getIcon())).resize(120, 120)
                .into(imgViewDay7);
        Picasso.get().load(getImgUrl(mWeathers10d[7].getIcon())).resize(120, 120)
                .into(imgViewDay8);
        Picasso.get().load(getImgUrl(mWeathers10d[8].getIcon())).resize(120, 120)
                .into(imgViewDay9);
        Picasso.get().load(getImgUrl(mWeathers10d[9].getIcon())).resize(120, 120)
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


        Picasso.get().load(getImgUrl(mWeathers24h[0].getIcon())).resize(120, 120)
                .into(imgViewHour1);
        Picasso.get().load(getImgUrl(mWeathers24h[1].getIcon())).resize(120, 120)
                .into(imgViewHour2);
        Picasso.get().load(getImgUrl(mWeathers24h[2].getIcon())).resize(120, 120)
                .into(imgViewHour3);
        Picasso.get().load(getImgUrl(mWeathers24h[3].getIcon())).resize(120, 120)
                .into(imgViewHour4);
        Picasso.get().load(getImgUrl(mWeathers24h[4].getIcon())).resize(120, 120)
                .into(imgViewHour5);
        Picasso.get().load(getImgUrl(mWeathers24h[5].getIcon())).resize(120, 120)
                .into(imgViewHour6);
        Picasso.get().load(getImgUrl(mWeathers24h[6].getIcon())).resize(120, 120)
                .into(imgViewHour7);
        Picasso.get().load(getImgUrl(mWeathers24h[7].getIcon())).resize(120, 120)
                .into(imgViewHour8);
        Picasso.get().load(getImgUrl(mWeathers24h[8].getIcon())).resize(120, 120)
                .into(imgViewHour9);
        Picasso.get().load(getImgUrl(mWeathers24h[9].getIcon())).resize(120, 120)
                .into(imgViewHour10);
        Picasso.get().load(getImgUrl(mWeathers24h[10].getIcon())).resize(120, 120)
                .into(imgViewHour11);
        Picasso.get().load(getImgUrl(mWeathers24h[11].getIcon())).resize(120, 120)
                .into(imgViewHour12);
        Picasso.get().load(getImgUrl(mWeathers24h[12].getIcon())).resize(120, 120)
                .into(imgViewHour13);
        Picasso.get().load(getImgUrl(mWeathers24h[13].getIcon())).resize(120, 120)
                .into(imgViewHour14);
        Picasso.get().load(getImgUrl(mWeathers24h[14].getIcon())).resize(120, 120)
                .into(imgViewHour15);
        Picasso.get().load(getImgUrl(mWeathers24h[15].getIcon())).resize(120, 120)
                .into(imgViewHour16);
        Picasso.get().load(getImgUrl(mWeathers24h[16].getIcon())).resize(120, 120)
                .into(imgViewHour17);
        Picasso.get().load(getImgUrl(mWeathers24h[17].getIcon())).resize(120, 120)
                .into(imgViewHour18);
        Picasso.get().load(getImgUrl(mWeathers24h[18].getIcon())).resize(120, 120)
                .into(imgViewHour19);
        Picasso.get().load(getImgUrl(mWeathers24h[19].getIcon())).resize(120, 120)
                .into(imgViewHour20);
        Picasso.get().load(getImgUrl(mWeathers24h[20].getIcon())).resize(120, 120)
                .into(imgViewHour21);
        Picasso.get().load(getImgUrl(mWeathers24h[21].getIcon())).resize(120, 120)
                .into(imgViewHour22);
        Picasso.get().load(getImgUrl(mWeathers24h[22].getIcon())).resize(120, 120)
                .into(imgViewHour23);
        Picasso.get().load(getImgUrl(mWeathers24h[23].getIcon())).resize(120, 120)
                .into(imgViewHour24, new Callback() {
                    @Override
                    public void onSuccess() {
                        mView.findViewById(layout_weather_wait).setVisibility(View.GONE);
                        mView.findViewById(weather_mainFab).setVisibility(View.VISIBLE);
                        mView.findViewById(weather_temperatureSwitch).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        mView.findViewById(layout_weather_wait).setVisibility(View.GONE);
                        mView.findViewById(weather_mainFab).setVisibility(View.VISIBLE);
                        mView.findViewById(weather_temperatureSwitch).setVisibility(View.VISIBLE);
                        alert("There are some images that were not able to load", getContext());
                    }
                });
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFab_close = AnimationUtils.loadAnimation(getContext(), anim.fab_close);
        mFab_open = AnimationUtils.loadAnimation(getContext(), anim.fab_open);
        mFab_clock = AnimationUtils.loadAnimation(getContext(), anim.fab_rotate_clock);
        mFab_anticlock = AnimationUtils.loadAnimation(getContext(), anim.fab_rotate_anticlock);
//        getRowsWeather();
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
//        Uri uri = getUriWeatherCurrentLatLon(getContext());
//
//        Uri uri2 = getUriWeather10dLatLon(getContext());
//
//        Uri uri3 = getUriWeather24hLatLon(getContext());
//        Uri uri = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(ep_base_url))
//                .appendPath(getString(ep_weather))
//                .appendPath(getString(ep_zip))
//                .build();
//
//        Uri uri2 = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(ep_base_url))
//                .appendPath(getString(ep_weather))
//                .appendPath(getString(ep_zip))
//                .appendPath(getString(ep_10d))
//                .build();
//
//        Uri uri3 = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(ep_base_url))
//                .appendPath(getString(ep_weather))
//                .appendPath(getString(ep_zip))
//                .appendPath(getString(ep_24h))
//                .build();

        Uri uri = getUriWeatherCurrentZip(getContext());

        Uri uri2 = getUriWeather10dZip(getContext());

        Uri uri3 = getUriWeather24hZip(getContext());

        JSONObject msg = WeatherHelper.getJsonObjectZip(zip);

//        new SendPostAsyncTask.Builder(uri.toString(), msg)
//                .onPostExecute(this::endOfGetWeatherTask)
//                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
//                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();
//
//        new SendPostAsyncTask.Builder(uri2.toString(), msg)
//                .onPostExecute(this::endOfGetWeathers10dTask)
//                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
//                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();
//
//        new SendPostAsyncTask.Builder(uriWeather24h.toString(), msg)
//                .onPostExecute(this::endOfGetWeathers24hTask)
//                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
//                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();



        sendPostAsyncTaskHelper(uri, msg, this::endOfGetWeatherTask, mJwToken);
        sendPostAsyncTaskHelper(uri2, msg, this::endOfGetWeathers10dTask, mJwToken);
        sendPostAsyncTaskHelper(uri3, msg, this::endOfGetWeathers24hTask, mJwToken);
    }

    /**
     *
     * @param result
     */
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


                Weather[] weathers = new Weather[10];
                for (int i = 0; i < 10; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    JSONObject weatherJObject = dataJSONObject.getJSONObject(
                            getString(keys_json_weather));
                    Weather weather = new Weather(getNewIcon(weatherJObject.getString(getString(keys_json_icon)))
                            , dataJSONObject.getDouble(getString(keys_json_temp)) +  273.15 );
                    weathers[i] = weather;
                }
                if (mIfWeatherSuccess) {
                    mWeathers10d = weathers;
                }

            } else {
                alert("Can't load 24-h weathers", getContext());
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     *
     * @param result
     */
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

                Weather[] weathers = new Weather[24];
                for (int i = 0; i < 24; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    Weather weather = new Weather(getNewIcon(dataJSONObject.getString(
                            getString(keys_json_icon)))
                            , ((dataJSONObject.getDouble(getString(keys_json_temperature))
                            - 32) * 5 / 9) + 273.15);
                    weathers[i] = weather;
                }
                if (mIfWeatherSuccess) {
                    mWeather.setLat(root.getDouble(getString(keys_json_latitude)));
                    mWeather.setLon(root.getDouble(getString(keys_json_longitude)));
                    mWeather.setTimezoneID(root.getString(getString(keys_json_timezone)));
                    mWeathers24h = weathers;
                    setWeather();
                    mIfWeatherSuccess = false;
                }
//                mWeather.setZip();
//                setWeather();
//                if (!mAlertSave) {
//                    attemptSaveWeather();
//                }
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

            ifFabOpenCloseIt();

            WeatherFragmentDirections.ActionNavWeatherToNavLocations action =
                    WeatherFragmentDirections.actionNavWeatherToNavLocations(
                            locations, mEmail, mJwToken, mHomeWeather, mHomeWeathers10d
                            , mHomeWeathers24h);
            Navigation.findNavController(Objects.requireNonNull(mView)).navigate(action);
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
            boolean hasSys = false;
            boolean hasName = false;
            boolean hasTimezone = false;
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
            if (root.has(getString(keys_json_timezone))) {
                hasTimezone = true;
            } else {
                Log.e("ERROR!", "No timezone");
            }

            if (hasMain && hasName && hasSys && hasWeather && hasWind && hasTimezone) {
                mIfWeatherSuccess = true;
                JSONArray weatherJArray = root.getJSONArray(
                        getString(keys_json_weather));
                JSONObject mainJObject = root.getJSONObject(
                        getString(keys_json_main));
                String nameString = root.getString(
                        getString(keys_json_name));
                JSONObject sysJObject = root.getJSONObject(
                        getString(keys_json_sys));
                JSONObject windJObject = root.getJSONObject(
                        getString(keys_json_wind));
                long timezoneJObject = root.getLong(getString(keys_json_timezone));

                JSONObject weatherJObject = weatherJArray.getJSONObject(0);
                mWeather = new Weather(
                        weatherJObject.getString(getString(
                                keys_json_description))
                        , getNewIcon(weatherJObject.getString(getString(
                        keys_json_icon)))
                        , mainJObject.getDouble(getString(
                        keys_json_temp))
                        ,  mainJObject.getInt(getString(
                        keys_json_pressure))
                        , mainJObject.getInt(getString(
                        keys_json_humidity))
                        , mainJObject.getDouble(getString(
                        keys_json_temp_min))
                        , mainJObject.getDouble(getString(
                        keys_json_temp_max))
                        , windJObject.getDouble(getString(
                        keys_json_speed))
                        , timezoneJObject
                        , nameString
                );
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
//                TODO alert but has a boolean to chekck if current weather, 10d, 24 are not working
                // so only 1 will show up
//                alert("Not a valid zip code", getContext());
                ((EditText) mView.findViewById(weather_zipEditText))
                    .setError("Not a valid zip code");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }
}