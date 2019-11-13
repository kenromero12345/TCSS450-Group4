package edu.uw.tcss450.tcss450_group4.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.HomeActivityArgs;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.model.WeatherHelper;

import static edu.uw.tcss450.tcss450_group4.R.id.layout_weather_wait;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_cityCountry;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_conditionIcon;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_conditonDescription;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_forecastSwitch;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_getLocationButton;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_getSavedWeathersButton;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_getZipButton;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_humidity;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_pressure;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_saveButton;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_temperature;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_temperatureSwitch;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_windDegree;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_windSpeed;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.*;
import static edu.uw.tcss450.tcss450_group4.R.color.redviolet;
import static edu.uw.tcss450.tcss450_group4.R.color.uwMetallicGold;
import static edu.uw.tcss450.tcss450_group4.R.color.uwPurple;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View mView;
    private Weather mWeather;
    private static final char DEGREE = (char) 0x00B0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.findViewById(layout_weather_wait).setVisibility(View.VISIBLE);
        initialization(view);
    }

    private void initialization(@NonNull View view) {
        mView = view;
        HomeActivityArgs args = HomeActivityArgs.fromBundle(getArguments());
        String s = getArguments().getString("String");
//        setComponents();
//        HomeFragmentArgs args = null;
//        if (getArguments() != null) {
////            args = HomeFragmentArgs.fromBundle(getArguments());
//        }
//        if (args != null) {
//            mWeather = args.getWeather();
////        setComponents();
//        }
    }

    private void setComponents() {
        setWeather();
        mView.findViewById(weather_temperatureSwitch).setOnClickListener(e -> switchTemperature());
    }

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

        ImageView imgView = mView.findViewById(weather_conditionIcon);
        Picasso.get().load(getImgUrl(mWeather.getIcon())).into(imgView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mView.findViewById(layout_weather_wait).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    private void switchTemperature() {
        if (((Switch) mView.findViewById(weather_temperatureSwitch)).isChecked()) {
            TextView temp = mView.findViewById(weather_temperature);
            temp.setText(tempFromKelvinToFarenheitString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), redviolet));

        } else {
            TextView temp = mView.findViewById(weather_temperature);
            temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));
        }
    }

}
