package edu.uw.tcss450.tcss450_group4;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.ui.ConnectionGUIFragment;
import edu.uw.tcss450.tcss450_group4.ui.ConnectionGUIFragmentDirections;
import edu.uw.tcss450.tcss450_group4.ui.WeatherFragmentDirections;
import edu.uw.tcss450.tcss450_group4.utils.GetAsyncTask;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

public class HomeActivity extends AppCompatActivity {
    private String mJwToken;

    private int mMemberId;

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
                R.id.nav_home, R.id.nav_chat, R.id.nav_connectionGUI, R.id.nav_weather )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.setGraph(R.navigation.mobile_navigation, getIntent().getExtras());
        HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
        mJwToken = args.getJwt();
        mMemberId = args.getMemberId();
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
            case R.id.nav_connectionGUI:
                gotoConnection();
//                Uri uriC = new Uri.Builder()
//                        .scheme("https")
//                        .appendPath(getString(R.string.ep_base_url))
//                        .appendPath(getString(R.string.ep_connection))
//                        .appendPath(getString(R.string.ep_getall))
//                        .build();
//                new GetAsyncTask.Builder(uriC.toString())
//                        .onPostExecute(this::handleConnectionOnPostExecute)
//                        .addHeaderField("authorization", mJwToken) //add the JWT as a header
//                        .build().execute();
//                navController.navigate(R.id.nav_connectionGUI);
                break;
            case R.id.nav_weather:
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        //TODO:below
                        .appendPath(getString(R.string.ep_weather))
//                        .appendPath(getString(R.string.ep_blog))
//                        .appendPath(getString(R.string.ep_get))
                        .build();

                new GetAsyncTask.Builder(uri.toString())
                        .onPostExecute(this::handleWeatherGetOnPostExecute)
                        .addHeaderField("authorization", mJwToken) //add the JWT as a header
                        .build().execute();
                //TODO: remove below
                navController.navigate(R.id.nav_weather);
                break;
        }
        //Close the drawer
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();
        return true;
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
                                    getString(R.string.keys_json_connection_memeberid))
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

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        JSONArray connectionJArray = root.getJSONArray();
//        try {
//            JSONObject root = new JSONObject(result);
//            if (root.has(getString(R.string.keys_json_connection_response))) {
//                JSONObject response = root.getJSONObject(
//                        getString(R.string.keys_json_connection_response));
//                if (response.has(getString(R.string.keys_json_connection_data))) {
//                    JSONArray data = response.getJSONArray(
//                            getString(R.string.keys_json_connection_data));
//                    ConnectionItem[] connection = new ConnectionItem[data.length()];
//                    for(int i = 0; i < data.length(); i++) {
//                        JSONObject jsonConnection = data.getJSONObject(i);
//
//                        connection[i] = (new ConnectionItem.Builder(
//                                jsonConnection.getString(
//                                        getString(R.string.keys_json_connection_firstname)),
//                                jsonConnection.getString(
//                                        getString(R.string.keys_json_connection_username)))
//                                .build());
//                    }
//                    MobileNavigationDirections.ActionGlobalNavConnectionGUI directionsC
//                            = ConnectionGUIFragmentDirections.actionGlobalNavConnectionGUI(connection);
//                    Navigation.findNavController(this, R.id.nav_host_fragment)
//                            .navigate(directionsC);

//                    MobileNavigationDirections.ActionGlobalNavWeather directions
//                            = WeatherFragmentDirections.actionGlobalNavWeather(weather);
//
//                    Navigation.findNavController(this, R.id.nav_host_fragment)
//                            .navigate(directions);
//                } else {
//                    Log.e("ERROR!", "No data array");
//                }
//            } else {
//                Log.e("ERROR!", "No response");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("ERROR!", e.getMessage());
//        }
    }


    private void handleWeatherGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_weather_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_weather_response));
                if (response.has(getString(R.string.keys_json_weather_data))) {
                    JSONObject data = response.getJSONObject(
                            getString(R.string.keys_json_weather_data));
                    //TODO: data
                    Weather weather = new Weather(data);
//                    JSONArray data = response.getJSONArray(
//                            getString(R.string.keys_json_weather_data));
//
//                    BlogPost[] blogs = new BlogPost[data.length()];
//                    for(int i = 0; i < data.length(); i++) {
//                        JSONObject jsonBlog = data.getJSONObject(i);
//                        blogs[i] = (new BlogPost.Builder(
//                                jsonBlog.getString(
//                                        getString(R.string.keys_json_blogs_pubdate)),
//                                jsonBlog.getString(
//                                        getString(R.string.keys_json_blogs_title)))
//                                .addTeaser(jsonBlog.getString(
//                                        getString(R.string.keys_json_blogs_teaser)))
//                                .addUrl(jsonBlog.getString(
//                                        getString(R.string.keys_json_blogs_url)))
//                                .build());
//                    }

                    MobileNavigationDirections.ActionGlobalNavWeather directions
                            = WeatherFragmentDirections.actionGlobalNavWeather(weather);

                    Navigation.findNavController(this, R.id.nav_host_fragment)
                            .navigate(directions);
                } else {
                    Log.e("ERROR!", "No data");
                }
            } else {
                Log.e("ERROR!", "No response");
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
}
