package edu.uw.tcss450.tcss450_group4;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.model.Chat;
import edu.uw.tcss450.tcss450_group4.model.ChatMessageNotification;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.ConnectionRequestNotification;
import edu.uw.tcss450.tcss450_group4.model.Credentials;
import edu.uw.tcss450.tcss450_group4.model.LocationViewModel;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.ui.ChatFragmentDirections;
import edu.uw.tcss450.tcss450_group4.ui.ConnectionGUIFragmentDirections;
import edu.uw.tcss450.tcss450_group4.ui.HomeFragment;
import edu.uw.tcss450.tcss450_group4.ui.HomeFragmentDirections;
import edu.uw.tcss450.tcss450_group4.ui.WeatherFragmentDirections;
import edu.uw.tcss450.tcss450_group4.utils.PushReceiver;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;
import me.pushy.sdk.Pushy;

import static edu.uw.tcss450.tcss450_group4.R.id;
import static edu.uw.tcss450.tcss450_group4.R.id.action_logout;
import static edu.uw.tcss450.tcss450_group4.R.id.activity_loading;
import static edu.uw.tcss450.tcss450_group4.R.id.drawer_layout;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_chat_list;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_connectionGUI;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_home;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_host_fragment;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_logout;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_view;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_view_chat;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_weather;
import static edu.uw.tcss450.tcss450_group4.R.layout;
import static edu.uw.tcss450.tcss450_group4.R.navigation;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_chats;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_connection;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_getall;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_connections;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_firstname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_image;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_lastname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_memberid;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_username;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_country;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_data;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_deg;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_description;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_hourly;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_humidity;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_icon;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_latitude;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_login_success;
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
import static edu.uw.tcss450.tcss450_group4.R.string.keys_prefs_email;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_prefs_password;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_shared_prefs;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.alert;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getJsonObjectLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getNewIcon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather10dLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeather24hLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getUriWeatherCurrentLatLon;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.sendPostAsyncTaskHelper;

public class HomeActivity extends AppCompatActivity {
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
//    private static final String TAG = "WEATHER_FRAG";
    // the jw token for authorization
    private String mJwToken;
    private Credentials mCredentials;
    // the email given
    private String mEmail;
    private int mMemberId;
    private String mProfileURI;
    private ChatMessageNotification mChatMessage;
    private AppBarConfiguration mAppBarConfiguration;
    // the weather given
    private Weather mWeather;
    //the weather 10d given
    private Weather[] mWeathers10d;
    //the weather 24h given
    private Weather[] mWeathers24h;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    // the location of the mobile device
    private Location mLocations;
    // the location request support
    private LocationRequest mLocationRequest;
//    private double mLat;
//    private double mLon;
    // flag if weather is updated
    private boolean mUpdateWeather;

    private ColorFilter mDefault;
    private HomePushMessageReceiver mPushMessageReciever;

    private ConnectionItem[] mConnectionItems;

    private ConnectionRequestNotification mConnectionRequest;
//    private boolean mGoToConnection;
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

    /**
     * what happens on onResume lifecycle
     */
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        if (mPushMessageReciever == null) {
            mPushMessageReciever = new HomePushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReciever, iFilter);
//        if (getIntent().getExtras().containsKey("type")) {
//            String msg = getIntent().getExtras().getString("message");
//            String sender = getIntent().getExtras().getString("sender");
//            String memberId = getIntent().getExtras().getString("memberId");
//            mChatMessage =
//                    new ChatMessageNotification.Builder(sender, msg, memberId).build();
//            gotoChat();
//        }
    }

    /**
     * what happens on onpause lifecycle
     */
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        if (mPushMessageReciever != null){
            unregisterReceiver(mPushMessageReciever);
        }

    }


    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;

    /**
     * what happens on oncreate lifecycle
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pushy.listen(this);

//        gotoConnection();
        checkLocationPermission();
        setContentView(layout.activity_home);

        HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
        mJwToken = args.getJwt();
        mCredentials = args.getCredentials();
        mEmail = args.getCredentials().getEmail();
        mMemberId = args.getMemberId();
        mProfileURI = args.getProfileuri();
        mChatMessage = args.getChatMessage();
        mConnectionRequest = args.getConnectionRequest();


        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
//                findViewById(activity_loading).setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                findViewById(activity_loading).setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        NavigationView navigationView = findViewById(nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                nav_home, nav_connectionGUI, nav_chat_list, nav_weather, nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this
                , nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController
                , mAppBarConfiguration);

        Log.e("CHAT HOME", mChatMessage + "");

//        if (mChatMessage != null) {
//            Bundle bundle = getIntent().getExtras();
//            bundle.putSerializable("type", mChatMessage);
//            Log.e("BUNDLE", bundle + "");
//            Navigation.findNavController(this, R.id.nav_host_fragment)
//                    .setGraph(navigation.mobile_navigation, bundle);
//        } else if (mConnectionRequest != null) {
//            Bundle bundle = getIntent().getExtras();
//            bundle.putSerializable("type", mChatMessage);
//            Log.e("BUNDLE", bundle + "");
//            Navigation.findNavController(this, R.id.nav_host_fragment)
//                    .setGraph(navigation.mobile_navigation, bundle);
//        } else {
            navController.setGraph(navigation.mobile_navigation, getIntent().getExtras());
//        }

        NavigationUI.setupWithNavController(navigationView, navController);
//        if (getIntent().getExtras() != null) {


            View header = navigationView.getHeaderView(0);
            ImageView profileHome = header.findViewById(id.imageView_home_profile);
            TextView nameHome = header.findViewById(id.textView_home_name);
            nameHome.setText(mCredentials.getFirstName() + " " + mCredentials.getLastName());
            TextView usernameHome = header.findViewById(id.textView_home_username);
            usernameHome.setText(mCredentials.getUsername());
            String cleanImage = mProfileURI.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
            byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileHome.setImageBitmap(decodedByte);
//            Log.e("BITMAP", decodedByte.toString());
//        }
//        navigationView.setNavigationItemSelectedListener(this::onNavigationSelected);
//        if (args.getChatMessage() != null) {
//            gotoChat();
//        } else {
            navigationView.setNavigationItemSelectedListener(this::onNavigationSelected);
//        }
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
        mDefault = toolbar.getNavigationIcon().getColorFilter();
        createLocationRequest();
    }


    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_connectiongui_list, container, false);
//
//        // Set the adapter
//
//        return view;
//    }


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

    /**
     * what happens when a menuitem is clicked
     * @param menuItem the given menuitem to be clicked
     * @return true if a menuitem is selected
     */
    private boolean onNavigationSelected(final MenuItem menuItem) {
        NavController navController =
                Navigation.findNavController(this, nav_host_fragment);
        /* Use the ViewModel's factory method to gain access to the ViewModel */
        LocationViewModel model =
                LocationViewModel.getFactory().create(LocationViewModel.class);
        switch (menuItem.getItemId()) {
            case nav_home:
//                if(Objects.requireNonNull(navController.getCurrentDestination()).getId() != nav_weather) {
//                    if (mWeather != null) {
                        MobileNavigationDirections.ActionGlobalNavHome directions
                                = HomeFragmentDirections.actionGlobalNavHome();
                        directions.setWeather(mWeather);
                        directions.setMemberId(mMemberId);
                        directions.setJwt(mJwToken);
                        directions.setChatMessage(mChatMessage);
                        directions.setConnectionRequest(mConnectionRequest);
//                        directions.setConnectionItems(mConnectionItems);
                        navController.navigate(directions);
                        mChatMessage = null;
                        mConnectionRequest = null;
//                    }
//                    mGoToConnection = false;
//                }

                break;
            case nav_chat_list:
                // We've clicked on chat, reset the hamburger icon color
                ((Toolbar) findViewById(R.id.toolbar)).getNavigationIcon().setColorFilter(mDefault);
                gotoChat();
                break;
            case nav_connectionGUI:
//                mGoToConnection = true;
                gotoConnection();
                break;
            case nav_weather:
//                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != nav_weather) {
//                    MobileNavigationDirections.ActionGlobalNavWeather directions2
//                            = WeatherFragmentDirections.actionGlobalNavWeather(mJwToken, mEmail,
//                            mWeather, mWeathers10d, mWeathers24h);
//
//                    navController.navigate(directions2);
//                }
                clickWeather(navController);

                break;
            case nav_logout:
                logout();
                break;

        }
        //Close the drawer
        ((DrawerLayout) findViewById(drawer_layout)).closeDrawers();
        return true;
    }

    private void gotoChat() {
        JSONObject memberId = new JSONObject();
        try {
            memberId.put("memberId", mMemberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri uriChats = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_chats))
                .build();
        new SendPostAsyncTask.Builder(uriChats.toString(), memberId)
                .onPostExecute(this::handleChatsGetOnPostExecute)
                .addHeaderField("authorization", mJwToken)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    /**
     * when the weather is clicked
     * @param navController the navigation helper
     */
    private void clickWeather(NavController navController) {
//        ((DrawerLayout) findViewById(drawer_layout)).openDrawer();
//        Runnable r = () -> {
//        ((DrawerLayout) findViewById(drawer_layout)).closeDrawers();
//        };
//        final Thread t = new Thread(r);
//        t.start();
//        if(Objects.requireNonNull(navController.getCurrentDestination()).getId() == nav_home){
//            findViewById(layout_weather_wait).setVisibility(View.VISIBLE);
//        }
        findViewById(activity_loading).setVisibility(View.VISIBLE);

        mUpdateWeather = true;
        Log.d("weather", "update");
        Location location = Objects.requireNonNull(LocationViewModel.getFactory()
                .create(LocationViewModel.class)
                .getCurrentLocation().getValue());
        BigDecimal newLat = new BigDecimal(location.getLatitude())
                .setScale(4, BigDecimal.ROUND_DOWN);
        BigDecimal oldLat = new BigDecimal(mLocations.getLatitude())
                .setScale(4, BigDecimal.ROUND_DOWN);
        BigDecimal newLon = new BigDecimal(location.getLongitude())
                .setScale(4, BigDecimal.ROUND_DOWN);
        BigDecimal oldLon = new BigDecimal(mLocations.getLongitude())
                .setScale(4, BigDecimal.ROUND_DOWN);
        if (newLat.equals(oldLat) && newLon.equals(oldLon)) {
            MobileNavigationDirections.ActionGlobalNavWeather directions
                    = WeatherFragmentDirections.actionGlobalNavWeather
                    (mJwToken, mEmail,mWeather, mWeathers10d, mWeathers24h, mWeather
                            , mWeathers10d.clone(), mWeathers24h.clone());
            navController.navigate(directions);
        } else {
            getWeather(location);
        }
    }

    private void handleErrorsInTask(final String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    private void handleChatsGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean(getString(keys_json_login_success))) {
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
                directions.setMemberId(mMemberId);
                Log.e("MESSAGE", "homeactivity " + mMemberId);
                directions.setJwt(mJwToken);
//                directions.setChats(chats);
//                directions.setEmail(mEmail);
                directions.setChatMessage(mChatMessage);
                Navigation.findNavController(this, nav_host_fragment)
                        .navigate(directions);
                mChatMessage = null;
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

    /**
     * check if user has already allowed the use of Locations
     */
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
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_connection))
                .appendPath(getString(ep_getall))
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
        DateFormat daysFormat = new SimpleDateFormat("MMM dd yyyy hh:mm a");
        //DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        try {
            date = format.parse(timestamp);
            a = daysFormat.format(date.getTime());
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
            if (root.has(getString(keys_json_connection_connections))){
                hasConnection = true;
            } else {
                Log.e("ERROR!", "No connection");
            }


            if (hasConnection){
                JSONArray connectionJArray = root.getJSONArray(
                        getString(keys_json_connection_connections));
//                ConnectionItem[] conItem = new ConnectionItem[connectionJArray.length()];
                mConnectionItems = new ConnectionItem[connectionJArray.length()];
                for(int i = 0; i < connectionJArray.length(); i++){
                    JSONObject jsonConnection = connectionJArray.getJSONObject(i);
                    mConnectionItems[i] = new ConnectionItem(
                            jsonConnection.getInt(
                                    getString(keys_json_connection_memberid))
                            , jsonConnection.getString(
                            getString(keys_json_connection_firstname))
                            , jsonConnection.getString(
                            getString(keys_json_connection_lastname))
                            ,jsonConnection.getString(
                            getString(keys_json_connection_username)),
                            jsonConnection.getString(
                                    getString(keys_json_connection_image)));
                }
//                Log.e("profile pic", conItem[0].getContactImage());



//                if (mGoToConnection) {
                    MobileNavigationDirections.ActionGlobalNavConnectionGUI directions
                            = ConnectionGUIFragmentDirections.actionGlobalNavConnectionGUI(mConnectionItems);
                    directions.setJwt(mJwToken);
                    directions.setMemberid(mMemberId);
                    directions.setConnectionRequest(mConnectionRequest);
                    Navigation.findNavController(this, nav_host_fragment)
                            .navigate(directions);
                    mConnectionRequest = null;
//                }
//                else {
//                    MobileNavigationDirections.ActionGlobalNavHome directions
//                            = MobileNavigationDirections.actionGlobalNavHome();
//                    directions.set
//                    directions.setConnectionItems(mConnectionItems);
//                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * getting the weather
     * @param location the location given
     */
    private void getWeather(Location location) {
//        mLon = location.getLongitude();
//        mLat = location.getLatitude();

        Uri uri = getUriWeatherCurrentLatLon(this);

        Uri uri2 = getUriWeather10dLatLon(this);

        Uri uri3 = getUriWeather24hLatLon(this);

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

        JSONObject msg = getJsonObjectLatLon(location.getLatitude(), location.getLongitude());

//        JSONObject msg = new JSONObject();
//        try {
//            msg.put("lon", mLon);
//            msg.put("lat", mLat);
//        } catch (JSONException e) {
//            Log.wtf("LONG/LAT", "Error creating JSON: " + e.getMessage());
//        }
//        new SendPostAsyncTask.Builder(uri.toString(), msg)
//                .onProgressUpdate(((DrawerLayout) findViewById(drawer_layout)).closeDrawers())
//                .onCancelled(error -> Log.e("W", error))
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
//                    String icon = "";
//                    if (dataJSONObject.getString(getString(keys_json_icon)).equals("fog")) {
//                        if () {
//                            icon = getNewIcon("fogd");
//                        } else {
//                            icon = getNewIcon("fogn");
//                        }
//                    } else {
//                        icon = getNewIcon(dataJSONObject.getString(getString(keys_json_icon)));
//                    }
                    Weather weather = new Weather(getNewIcon(dataJSONObject.getString(getString(keys_json_icon)))//icon
                            , ((dataJSONObject.getDouble(getString(keys_json_temperature))
                            - 32) * 5 / 9) + 273.15);
                    weathers[i] = weather;
                    Log.d("weather" + i, "" + weather.getTemp());
                }

                mWeathers24h = weathers;

                if (mUpdateWeather) {
                    MobileNavigationDirections.ActionGlobalNavWeather directions2
                            = WeatherFragmentDirections.actionGlobalNavWeather
                            (mJwToken, mEmail,mWeather, mWeathers10d, mWeathers24h, mWeather
                                    , mWeathers10d.clone(), mWeathers24h.clone());

                    NavController navController =
                            Navigation.findNavController(this, nav_host_fragment);
                    navController.navigate(directions2);
                } else {
                    MobileNavigationDirections.ActionGlobalNavHome directions
                            = WeatherFragmentDirections.actionGlobalNavHome();
                    directions.setWeather(mWeather);
                    directions.setMemberId(mMemberId);
                    directions.setJwt(mJwToken);
                    directions.setChatMessage(mChatMessage);
                    directions.setConnectionRequest(mConnectionRequest);
//                    Log.e("DEBUG", mConnectionItems.toString());
//                    directions.setConnectionItems(mConnectionItems);
                    Navigation.findNavController(this, nav_host_fragment).navigate(directions);
                    mChatMessage = null;
                    mConnectionRequest = null;
                }

            } else {
                alert("Can't load current 24-h forecast", this);
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
                alert("Can't load current weather", this);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     * logout from the app
     */
    private void logout() {
        new DeleteTokenAsyncTask(this).execute();
//        SharedPreferences prefs =
//                getSharedPreferences(
//                        getString(keys_shared_prefs),
//                        Context.MODE_PRIVATE);
//        //remove the saved credentials from StoredPrefs
//        prefs.edit().remove(getString(keys_prefs_password)).apply();
//        prefs.edit().remove(getString(keys_prefs_email)).apply();
//
//        //close the app
//        //finishAndRemoveTask();
//
//        //or close this activity and bring back the Login
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
//        //End this Activity and remove it from the Activity back stack.
//        finish();
    }

    /**
     * logout and quit the app
     */
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

    /**
     * the option's item clicked
     * @param item from the option
     * @return a bolean if a optiona item is clicked
     */
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


    /**
     * requests a permission
     * @param requestCode the code for requests
     * @param permissions the permision string lisr
     * @param grantResults the grant results
     */
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

    /**
     * requests a location
     */
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
//                                gotoConnection();
                                getWeather(location);
                            }
                        }
                    });
        }
    }

    // Deleting the Pushy device token must be done asynchronously. Good thing
    // we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {

        private HomeActivity mHomeActivity;

        public DeleteTokenAsyncTask(HomeActivity homeActivity) {
            mHomeActivity = homeActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //since we are already doing stuff in the background, go ahead
            //and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);

            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();

            //unregister the device from the Pushy servers
            Pushy.unregister(HomeActivity.this);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //close the app
//            finishAndRemoveTask();

            //or close this activity and bring back the Login
            Intent i = new Intent(mHomeActivity, MainActivity.class);
            startActivity(i);
            //Ends this Activity and removes it from the Activity back stack.
            finish();
        }
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class HomePushMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(HomeActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            if (nd.getId() == nav_home) {
                MobileNavigationDirections.ActionGlobalNavHome directions
                        = HomeFragmentDirections.actionGlobalNavHome();
                directions.setWeather(mWeather);
                directions.setMemberId(mMemberId);
                directions.setJwt(mJwToken);
//                        directions.setConnectionItems(mConnectionItems);
                nc.navigate(directions);
            } else if (nd.getId() == nav_chat_list) {
                gotoChat();
            } else if (nd.getId() != nav_chat_list) {
                if (intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {
                    String sender = intent.getStringExtra("SENDER");
                    String messageText = intent.getStringExtra("MESSAGE");
                    //change the hamburger icon to red alerting the user of the notification
                    ((Toolbar) findViewById(R.id.toolbar)).getNavigationIcon()
                            .setColorFilter(getColor(R.color.uwPurple), PorterDuff.Mode.SRC_IN);
                    Log.d("HOME", sender + ": " + messageText);
                }
            }
        }
    }

}
