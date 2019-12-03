package edu.uw.tcss450.tcss450_group4.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.model.Location;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.model.WeatherHelper;

import static edu.uw.tcss450.tcss450_group4.R.layout;
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
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getNewIcon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather10dLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather24hLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeatherCurrentLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.sendPostAsyncTaskHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 * @author Ken Gil Romero kgmr@uw.edu
 */
public class LocationsFragment extends Fragment {
//    //the tag f
//    private static final String TAG = "WEATHER_FRAG";
    //the lst of location
    private List<Location> mLocations;
    //the email
    private String mEmail;
    //the jwtoken for authrization
    private String mJwToken;
    // the current weather
    private Weather mWeather;
    /**
     * the 10 day weather
     */
    private Weather[] mWeathers10d;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LocationsFragment newInstance(int columnCount) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_locations_list, container
                , false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyLocationsRecyclerViewAdapter(mLocations
                    , this::getWeather));
        }
        return view;
    }

    /**
     * the lifecycle on create
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationsFragmentArgs args = LocationsFragmentArgs.fromBundle(
                Objects.requireNonNull(getArguments()));
        mLocations = new ArrayList<>(Arrays.asList(args.getLocations()));
        mEmail =  args.getEmail();
        mJwToken = args.getJwt();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Location item);
    }

    /**
     * get the weather of the location
     * @param tLocation
     */
    private void getWeather(final Location tLocation) {
        if (!tLocation.getName().equals("No Locations")) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Do you want to get the location's weather?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    (dialog, which) -> {
                        displayWeather(tLocation);
                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        } else {
            alert("Cannot display any weather", getContext());
        }
    }

    /**
     * displays the weather location
     * @param tLocation the given location
     */
    private void displayWeather(final Location tLocation) {
        //TODO use zip
        Uri uri = getUriWeatherCurrentLatLon(Objects.requireNonNull(getContext()));

        Uri uri2 = getUriWeather10dLatLon(getContext());

        Uri uri3 = getUriWeather24hLatLon(getContext());

        JSONObject msg = WeatherHelper.getJsonObjectLatLon(tLocation.getLat(), tLocation.getLon());

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
     * handling the 24h weather result given
     * @param result the given result
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
            mWeather.setLat(root.getDouble(getString(keys_json_latitude)));
            mWeather.setLon(root.getDouble(getString(keys_json_longitude)));
            mWeather.setTimezoneID(root.getString(getString(keys_json_timezone)));
            if (hasHourly) {
                JSONObject hourlyJObject = root.getJSONObject(
                        getString(keys_json_hourly));
                JSONArray dataJArray = hourlyJObject.getJSONArray(
                        getString(keys_json_data));

                Weather[] weathers = new Weather[24];
                for (int i = 0; i < 24; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    Weather weather = new Weather(getNewIcon(dataJSONObject.getString(getString(
                            keys_json_icon)))
                            , ((dataJSONObject.getDouble(getString(keys_json_temperature))
                            - 32) * 5 / 9) + 273.15);
                    weathers[i] = weather;
                }
                LocationsFragmentArgs args = null;
                if (getArguments() != null) {
                    args = LocationsFragmentArgs.fromBundle(getArguments());
                }
                MobileNavigationDirections.ActionGlobalNavWeather directions
                        = WeatherFragmentDirections.actionGlobalNavWeather(mJwToken, mEmail
                                , mWeather, mWeathers10d, weathers, Objects.requireNonNull(args).getWeatherHome()
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
     * handling the 10d weather result given
     * @param result the given result
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
     * handling the weather result given
     * @param result the given result
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


