package edu.uw.tcss450.tcss450_group4;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.ui.WeatherFragmentDirections;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "WEATHER_FRAG";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String mJwToken;
    private String mEmail;
    private AppBarConfiguration mAppBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_connections, R.id.nav_chat, R.id.nav_weather)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.setGraph(R.navigation.mobile_navigation, getIntent().getExtras());

        HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
        mJwToken = args.getJwt();
        mEmail = args.getCredentials().getEmail();
        navigationView.setNavigationItemSelectedListener(this::onNavigationSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private boolean onNavigationSelected(final MenuItem menuItem) {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                break;
            case R.id.nav_chat:
                navController.navigate(R.id.nav_chat);
                break;
            case R.id.nav_connections:
                navController.navigate(R.id.nav_connections);
                break;
            case R.id.nav_weather:
                gotoWeather();
                break;
        }
        //Close the drawer
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();
        return true;
    }

    private void gotoWeather() {
        double longitude = 0;
        double latitude = 0;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_weather))
                .appendPath(getString(R.string.ep_latLon))
                .build();

        JSONObject msg = new JSONObject();
        try {
            msg.put("lon",/* round(*/longitude/*,2)*/);
            msg.put("lat",/* round(*/latitude/*,2)*/);
        } catch (JSONException e) {
            Log.wtf("LONG/LAT", "Error creating JSON: " + e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleWeatherGetOnPostExecute)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
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
            if (root.has(getString(R.string.keys_json_weather))) {
                hasWeather = true;
            } else {
                Log.e("ERROR!", "No weather");
            }
            if (root.has(getString(R.string.keys_json_main))) {
                hasMain = true;
            } else {
                Log.e("ERROR!", "No main");
            }
            if (root.has(getString(R.string.keys_json_wind))) {
                hasWind = true;
            } else {
                Log.e("ERROR!", "No wind");
            }
            if (root.has(getString(R.string.keys_json_coord))) {
                hasCoord = true;
            } else {
                Log.e("ERROR!", "No coord");
            }
            if (root.has(getString(R.string.keys_json_name))) {
                hasName = true;
            } else {
                Log.e("ERROR!", "No name");
            }
            if (root.has(getString(R.string.keys_json_sys))) {
                hasSys = true;
            } else {
                Log.e("ERROR!", "No sys");
            }

            if (hasCoord && hasMain && hasName && hasSys && hasWeather && hasWind) {
                JSONArray weatherJArray = root.getJSONArray(
                        getString(R.string.keys_json_weather));
                JSONObject mainJObject = root.getJSONObject(
                        getString(R.string.keys_json_main));
                String nameString = root.getString(
                        getString(R.string.keys_json_name));
                JSONObject sysJObject = root.getJSONObject(
                        getString(R.string.keys_json_sys));
                JSONObject coordJObject = root.getJSONObject(
                        getString(R.string.keys_json_coord));
                JSONObject windJObject = root.getJSONObject(
                        getString(R.string.keys_json_wind));

                JSONObject weatherJObject = weatherJArray.getJSONObject(0);
                Weather weather = new Weather(
                    weatherJObject.getString(getString(
                                R.string.keys_json_description))
                        , weatherJObject.getString(getString(
                                R.string.keys_json_icon))
                        , coordJObject.getDouble(getString(
                                R.string.keys_json_lon))
                        , coordJObject.getDouble(getString(
                                R.string.keys_json_lat))
                        , mainJObject.getDouble(getString(
                                R.string.keys_json_temp))
                        ,  mainJObject.getInt(getString(
                                R.string.keys_json_pressure))
                        , mainJObject.getInt(getString(
                                R.string.keys_json_humidity))
                        , mainJObject.getDouble(getString(
                                R.string.keys_json_temp_min))
                        , mainJObject.getDouble(getString(
                                R.string.keys_json_temp_max))
                        , windJObject.getDouble(getString(
                                R.string.keys_json_speed))
                        , windJObject.getInt(getString(
                                R.string.keys_json_deg))
                        , nameString
                        , sysJObject.getString(getString(
                                R.string.keys_json_country))
                        /*, mJwToken*/);

                MobileNavigationDirections.ActionGlobalNavWeather directions
                        = WeatherFragmentDirections.actionGlobalNavWeather(weather);
                directions.setEmail(mEmail);
                directions.setJwt(mJwToken);

                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(directions);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    private void logout() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();

        //close the app
        finishAndRemoveTask();

        //or close this activity and bring back the Login
        //Intent i = new Intent(this, MainActivity.class);
        //startActivity(i);
        //End this Activity and remove it from the Activity back stack.
        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = BigDecimal.valueOf(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
}
