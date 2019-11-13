package edu.uw.tcss450.tcss450_group4;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Objects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import edu.uw.tcss450.tcss450_group4.model.Chat;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.LocationViewModel;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.ui.ChatFragmentDirections;
import edu.uw.tcss450.tcss450_group4.ui.ConnectionGUIFragmentDirections;
import edu.uw.tcss450.tcss450_group4.ui.WeatherFragmentDirections;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static edu.uw.tcss450.tcss450_group4.R.id;
import static edu.uw.tcss450.tcss450_group4.R.id.action_logout;
import static edu.uw.tcss450.tcss450_group4.R.id.drawer_layout;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_chat;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_chat_list;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_connectionGUI;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_home;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_host_fragment;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_logout;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_view;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_weather;
import static edu.uw.tcss450.tcss450_group4.R.layout;
import static edu.uw.tcss450.tcss450_group4.R.navigation;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_10d;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_24h;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_latLon;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_weather;
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
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_main;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_name;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_pressure;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_speed;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_sys;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temp;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temp_max;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temp_min;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_temperature;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_weather;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_wind;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_prefs_email;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_prefs_password;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_shared_prefs;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.alert;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getNewIcon;

public class HomeActivity extends AppCompatActivity {
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private static final String TAG = "WEATHER_FRAG";
    private String mJwToken;
    private String mEmail;
    private int mMemberId;
    private AppBarConfiguration mAppBarConfiguration;
    private Weather mWeather;
    private Weather[] mWeathers10d;
    private Weather[] mWeathers24h;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLocations;
    private LocationRequest mLocationRequest;
//    private View mView;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3600000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
    }

        /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();
        setContentView(layout.activity_home);
        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(drawer_layout);
        NavigationView navigationView = findViewById(nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                nav_home, nav_connectionGUI, nav_chat, nav_weather, nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this
                , nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController
                , mAppBarConfiguration);


//        getIntent().getExtras().putString("Weather", "");
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("weather", weather);
////        getIntent().getExtras().putString("String", "String text");

//        navController.setGraph(navigation.mobile_navigation, getIntent().getExtras());
//        Bundle bundle = new Bundle();
//        bundle.putDouble(getString(keys_json_lat), location.getLatitude());
//        bundle.putDouble(getString(keys_json_lon), location.getLongitude());
        navController.setGraph(navigation.mobile_navigation, getIntent().getExtras());
        NavigationUI.setupWithNavController(navigationView, navController);
        if (getIntent().getExtras() != null) {
            HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
            mJwToken = args.getJwt();
            mEmail = args.getCredentials().getEmail();
            mMemberId = args.getMemberId();
        }
        navigationView.setNavigationItemSelectedListener(this::onNavigationSelected);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Use the ViewModel's factory method to gain access to the ViewModel
                    LocationViewModel model =
                            LocationViewModel.getFactory().create(LocationViewModel.class);
                    model.changeLocation(location);

                    Log.d("LOCATION UPDATE!", location.toString());
                }
            }
        };
        createLocationRequest();
    }

    /**
     * Create and configure a Location Request used when retrieving location updates
     */
    private void createLocationRequest() {
        mLocationRequest = LocationRequest.create();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this
                , nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean onNavigationSelected(final MenuItem menuItem) {
        NavController navController =
                Navigation.findNavController(this, nav_host_fragment);
        switch (menuItem.getItemId()) {
            case nav_home:
                if(Objects.requireNonNull(navController.getCurrentDestination()).getId() != nav_weather) {
                    if (mWeather != null) {
                        MobileNavigationDirections.ActionGlobalNavHome directions
                                = WeatherFragmentDirections.actionGlobalNavHome();
                        directions.setWeather(mWeather);
                        navController.navigate(directions);
                    }
                }

                break;
            case nav_chat_list:
                JSONObject memberId = new JSONObject();
                try {
                    memberId.put("memberId", mMemberId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Uri uriChats = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_chats))
                        .build();
                new SendPostAsyncTask.Builder(uriChats.toString(), memberId)
                        .onPostExecute(this::handleChatsGetOnPostExecute)
                        .addHeaderField("authorization", mJwToken)
                        .onCancelled(this::handleErrorsInTask)
                        .build().execute();
//                navController.navigate(R.id.nav_chat_list);
                break;
            case nav_connectionGUI:
                gotoConnection();
                break;
            case nav_weather:
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != nav_weather) {
                    MobileNavigationDirections.ActionGlobalNavWeather directions2
                            = WeatherFragmentDirections.actionGlobalNavWeather(mJwToken, mEmail,
                            mWeather, mWeathers10d, mWeathers24h);

                    navController.navigate(directions2);
                }
                break;
            case nav_logout:
                logout();

        }
        //Close the drawer
        ((DrawerLayout) findViewById(drawer_layout)).closeDrawers();
        return true;
    }

    private void handleErrorsInTask(final String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    private void handleChatsGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean(getString(R.string.keys_json_login_success))) {
                JSONArray data = root.getJSONArray("names");
//                if (response.has(getString(R.string.keys_json_chats_data))) {
//                    JSONArray data = response.getJSONArray(getString(R.string.keys_json_chats_data));
                Chat[] chats = new Chat[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonChatLists = data.getJSONObject(i);

                    String recentMessage = jsonChatLists.getString("message");
                    if (recentMessage != "null") {
                        chats[i] = (new Chat.Builder(jsonChatLists.getString("chatid"),
                                jsonChatLists.getString("name"),
                                jsonChatLists.getString("message"),
                                convertTimeStampToDate(jsonChatLists.getString("timestamp")))
                                .build());
                    } else {
                        chats[i] = (new Chat.Builder(jsonChatLists.getString("chatid"),
                                jsonChatLists.getString("name"),
                                "",
                                "")
                                .build());
                    }
                }
                MobileNavigationDirections.ActionGlobalNavChatList directions
                        = ChatFragmentDirections.actionGlobalNavChatList(chats);
                Navigation.findNavController(this, nav_host_fragment)
                        .navigate(directions);
//                }    else {
//                    Log.e("ERROR!", "No data array");
//                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }


    private void checkLocationPermission() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);

        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
        }
    }

    private void gotoConnection() {
        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_getall))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberId", mMemberId);
        } catch (JSONException e) {
            Log.wtf("MEMBERID", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriConnection.toString(), msgBody)
                .onPostExecute(this::handleConnectionOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();

    }

    private String convertTimeStampToDate(String timestamp) {
        Date date = new Date();
        String a = "";
        //Date showTime = new Date();
        //Date showDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        //DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        try {
            date = format.parse(timestamp);
            a = timeFormat.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return a;
    }

    private void handleConnectionOnPostExecute(final String result) {
        //parse JSON
        try {
            boolean hasConnection = false;
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_connection_connections))){
                hasConnection = true;
            } else {
                Log.e("ERROR!", "No connection");
            }

            if (hasConnection){
                JSONArray connectionJArray = root.getJSONArray(
                        getString(R.string.keys_json_connection_connections));
                ConnectionItem[] conItem = new ConnectionItem[connectionJArray.length()];
                for(int i = 0; i < connectionJArray.length(); i++){
                    JSONObject jsonConnection = connectionJArray.getJSONObject(i);
                    conItem[i] = new ConnectionItem(
                            jsonConnection.getInt(
                                    getString(R.string.keys_json_connection_memberid))
                            , jsonConnection.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , jsonConnection.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,jsonConnection.getString(
                            getString(R.string.keys_json_connection_username)));
                }

                MobileNavigationDirections.ActionGlobalNavConnectionGUI directions
                        = ConnectionGUIFragmentDirections.actionGlobalNavConnectionGUI(conItem);
                directions.setJwt(mJwToken);
                directions.setMemberid(mMemberId);

                Navigation.findNavController(this, nav_host_fragment)
                        .navigate(directions);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWeather(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_latLon))
                .build();

        Uri uri2 = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_latLon))
                .appendPath(getString(ep_10d))
                .build();

        Uri uri3 = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_weather))
                .appendPath(getString(ep_latLon))
                .appendPath(getString(ep_24h))
                .build();

        JSONObject msg = new JSONObject();
        try {
            msg.put("lon", longitude);
            msg.put("lat", latitude);
        } catch (JSONException e) {
            Log.wtf("LONG/LAT", "Error creating JSON: " + e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleWeatherGetOnPostExecute)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();

        new SendPostAsyncTask.Builder(uri2.toString(), msg)
                .onPostExecute(this::handleWeather10dGetOnPostExecute)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();

        new SendPostAsyncTask.Builder(uri3.toString(), msg)
                .onPostExecute(this::handleWeather24hGetOnPostExecute)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

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

                Weather[] weathers = new Weather[24];
                for (int i = 0; i < 24; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    Weather weather = new Weather(getNewIcon(dataJSONObject.getString(
                            getString(keys_json_icon)))
                            , ((dataJSONObject.getDouble(getString(keys_json_temperature))
                            - 32) * 5 / 9) + 273.15);
                    weathers[i] = weather;
                    Log.d("weather" + i, "" + weather.getTemp());
                }

                mWeathers24h = weathers;

            } else {
                alert("Can't load current 24-h forecast", this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

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

                Weather[] weathers = new Weather[10];
                for (int i = 0; i < 10; i++) {
                    JSONObject dataJSONObject = dataJArray.getJSONObject(i);
                    JSONObject weatherJObject = dataJSONObject.getJSONObject(
                            getString(keys_json_weather));
                    Weather weather = new Weather(weatherJObject.getString(getString(keys_json_icon))
                            , dataJSONObject.getDouble(getString(keys_json_temp)) +  273.15 );
                    weathers[i] = weather;
                }

                mWeathers10d = weathers;
            } else {
                alert("Can't load current 10-day forecast", this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void handleWeatherGetOnPostExecute(final String result) {
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
                Weather weather = new Weather(
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
                        , nameString
                        );

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
                MobileNavigationDirections.ActionGlobalNavHome directions
                        = WeatherFragmentDirections.actionGlobalNavHome();
                directions.setWeather(mWeather);
                Navigation.findNavController(this,  nav_host_fragment).navigate(directions);
            } else {
                alert("Can't load current weather", this);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void logout() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(keys_prefs_password)).apply();
        prefs.edit().remove(getString(keys_prefs_email)).apply();

        //close the app
        //finishAndRemoveTask();

        //or close this activity and bring back the Login
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }

    private void logoutAndFinish() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(keys_prefs_password)).apply();
        prefs.edit().remove(getString(keys_prefs_email)).apply();

        //close the app
        finishAndRemoveTask();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_LOCATIONS) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // locations-related task you need to do.
                requestLocation();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                //Shut down the app. In production release, you would let the user
                //know why the app is shutting down...maybe ask for permission again?

                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("You can't use the app without your location.\n" +
                        "Do you want to give us your location?\n" +
                        "If you say no, the app will close.");//TODO
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        (dialog, which) -> {
                            dialog.dismiss();
                            logoutAndFinish();
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        (dialog, which) -> {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                                            , Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_LOCATIONS);
                        });
                alertDialog.show();
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("REQUEST LOCATION", "User did NOT allow permission to request location!");
//            is this needed TODO
//            finishAndRemoveTask();
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("LOCATION", location.toString());
                            if (mLocations == null || mLocations.getLatitude() == location.getLatitude()
                                    && mLocations.getLongitude() == location.getLongitude()) {
                                mLocations = location;
                                getWeather(location);
                            }
                        }
                    });
        }
    }
}
