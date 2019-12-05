package edu.uw.tcss450.tcss450_group4.ui;


import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.LocationViewModel;
import edu.uw.tcss450.tcss450_group4.model.Weather;

import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_country;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_data;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_deg;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_description;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_hourly;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_humidity;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_icon;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_latitude;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_longitude;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_main;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_name;
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
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getJsonObjectLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getNewIcon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather10dLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather24hLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeatherCurrentLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.sendPostAsyncTaskHelper;

/**
 * Fragment for displaying a map and choosing a location from it
 * A simple {@link Fragment} subclass.
 * @author Ken Gil Romero kgmr@uw.edu
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    /**
     * the google map
     */
    private GoogleMap mMap;
    /**
     * for getting this email's location
     */
    private String mEmail;
    /**
     * for the authorization
     */
    private String mJwToken;
    /**
     * stores the current weather condition
     */
    private Weather mWeather;

    /**
     * stores locations that are saved
     */
    private edu.uw.tcss450.tcss450_group4.model.Location[] mLocations;

    /**
     * stores the 10day forecast
     */
    private Weather[] mWeathers10d;
//    private static final String TAG = "WEATHER_FRAG";
    /**
     * stores the latitude that was clicked
     */
    private double mLat;
    /**
     * stores the longitude that was clicked
     */
    private double mLon;

//    public MapFragment() {
//        // Required empty public constructor
//    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /**
     * initialization of some fields
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapFragmentArgs args = MapFragmentArgs.fromBundle(
                Objects.requireNonNull(getArguments()));
        mEmail =  args.getEmail();
        mJwToken = args.getJwt();
        mLocations = args.getLocations();
    }

    /**
     * sync map
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.d("map", "start");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    /**
     * when map is ready
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        //Go grab a reference to the ViewModel.
        LocationViewModel model =  LocationViewModel.getFactory().create(LocationViewModel.class);
        Location l = model.getCurrentLocation().getValue();

//        Log.d("lat", "" +  l.getLatitude());

        // Add a marker in the current device location and move the camera
        LatLng current = new LatLng(Objects.requireNonNull(l).getLatitude(), l.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));

        for (edu.uw.tcss450.tcss450_group4.model.Location loc : mLocations) {
            LatLng latLng = new LatLng(loc.getLat(), loc.getLon());
            mMap.addMarker(new MarkerOptions().position(latLng).title(loc.getName()));
        }
        //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));

        //Add a observer to the ViewModel. MainActivity is listening to changes to the device
        //location. It reports those changes to the ViewModel. This is an observer on
        //the ViewModel and will act on those changes.
        model.getCurrentLocation().observe(this, location -> {
            final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
            //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
        });

        mMap.setOnMapClickListener(this);
    }

    /**
     * when the map is clicked
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("LAT/LONG", latLng.toString());
//        Marker marker = mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("New Marker"));
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Do you want to get the location's weather?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                (dialog, which) -> {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (!addresses.isEmpty()) {
                            displayWeather(latLng.latitude, latLng.longitude);
                        } else {
                            alert("The location doesn't have an address",getContext());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));

    }

    /**
     * display weather after clicking the map
     * @param tLat
     * @param tLon
     */
    private void displayWeather(double tLat, double tLon) {
        mLat = tLat;
        mLon = tLon;
        Uri uri = getUriWeatherCurrentLatLon(getContext());

        Uri uri2 = getUriWeather10dLatLon(getContext());

        Uri uri3 = getUriWeather24hLatLon(getContext());
//        Uri uri = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(ep_base_url))
//                .appendPath(getString(ep_weather))
//                .appendPath(getString(ep_latLon))
//                .build();
//
//        Uri uri2 = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(ep_base_url))
//                .appendPath(getString(ep_weather))
//                .appendPath(getString(ep_latLon))
//                .appendPath(getString(ep_10d))
//                .build();
//
//        Uri uri3 = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(ep_base_url))
//                .appendPath(getString(ep_weather))
//                .appendPath(getString(ep_latLon))
//                .appendPath(getString(ep_24h))
//                .build();

        JSONObject msg = getJsonObjectLatLon(tLat, tLon);

//        new SendPostAsyncTask.Builder(uri.toString(), msg)
//                .onPostExecute(this::handleWeatherGetOnPostExecute)
//                .onCancelled(error -> Log.e(TAG, error))
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();
//
//        new SendPostAsyncTask.Builder(uri2.toString(), msg)
//                .onPostExecute(this::handleWeather10dGetOnPostExecute)
//                .onCancelled(error -> Log.e(TAG, error))
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();
//
//        new SendPostAsyncTask.Builder(uriWeather24h.toString(), msg)
//                .onPostExecute(this::handleWeather24hGetOnPostExecute)
//                .onCancelled(error -> Log.e(TAG, error))
//                .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                .build().execute();
        sendPostAsyncTaskHelper(uri, msg, this::handleWeatherGetOnPostExecute, mJwToken);
        sendPostAsyncTaskHelper(uri2, msg, this::handleWeather10dGetOnPostExecute, mJwToken);
        sendPostAsyncTaskHelper(uri3, msg, this::handleWeather24hGetOnPostExecute, mJwToken);
    }

    /**
     * at post execute for getting 24 hour forecast
     * @param result
     */
    private void handleWeather24hGetOnPostExecute(final String result) {
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
                mWeather.setLat(root.getDouble(getString(keys_json_latitude)));
                mWeather.setLon(root.getDouble(getString(keys_json_longitude)));
                mWeather.setTimezoneID(root.getString(getString(keys_json_timezone)));
                Weather[] weathers = new Weather[24];
                for (int i = 0; i < 24; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    Weather weather = new Weather(getNewIcon(dataJSONObject.getString(getString(
                            keys_json_icon)))
                            , ((dataJSONObject.getDouble(getString(keys_json_temperature))
                            - 32) * 5 / 9) + 273.15);
                    weathers[i] = weather;
                }

                MapFragmentArgs args = null;
                if (getArguments() != null) {
                    args = MapFragmentArgs.fromBundle(getArguments());
                }
                MobileNavigationDirections.ActionGlobalNavWeather directions
                        = WeatherFragmentDirections.actionGlobalNavWeather(mJwToken, mEmail
                        , mWeather, mWeathers10d, weathers, args.getWeatherHome()
                        , args.getWeathersHome10d(), args.getWeathersHome24h());

                Navigation.findNavController(Objects.requireNonNull(getView()))
                        .navigate(directions);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Can't load current 24-h forecast");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     * at post execute for getting 10 day forecast
     * @param result
     */
    private void handleWeather10dGetOnPostExecute(final String result) {
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
//                mWeather.setLat(root.getDouble(getString(keys_json_lat)));
//                mWeather.setLon(root.getDouble(getString(keys_json_lon)));
                Weather[] weathers = new Weather[10];
                for (int i = 0; i < 10; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    JSONObject weatherJObject = dataJSONObject.getJSONObject(
                            getString(keys_json_weather));
                    Weather weather = new Weather(weatherJObject.getString(getString(keys_json_icon))
                            , dataJSONObject.getDouble(
                            getString(keys_json_temp)) +  273.15 );
                    weathers[i] = weather;
                }
                mWeathers10d = weathers;
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("can't load saved weathers");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     * at post execute for getting current weather condition
     * @param result
     */
    private void handleWeatherGetOnPostExecute(final String result) {
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
                Weather weather = new Weather(
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
                /*, mJwToken*/

                if (windJObject.has(getString(keys_json_deg))) {
                    weather.setDeg(windJObject.getDouble(getString(keys_json_deg)));
                }

                if (sysJObject.has(getString(keys_json_country))) {
                    weather.setCountry(sysJObject.getString(getString(
                            keys_json_country)));
                }

                if (weatherJObject.has(getString(keys_json_main))) {
                    weather.setMain(weatherJObject.getString(getString(keys_json_main)));
                }

                mWeather = weather;

//                MobileNavigationDirections.ActionGlobalNavWeather directions
//                        = WeatherFragmentDirections.actionGlobalNavWeather(weather);
//                directions.setEmail(mEmail);
//                directions.setJwt(mJwToken);
//
//                Navigation.findNavController(getView())
//                        .navigate(directions);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("can't load weather");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }
}
