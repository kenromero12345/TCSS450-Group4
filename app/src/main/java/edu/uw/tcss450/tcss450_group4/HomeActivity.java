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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uw.tcss450.tcss450_group4.model.Chat;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.ui.ChatFragmentDirections;
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
                R.id.nav_home, R.id.nav_connections, R.id.nav_chat_list, R.id.nav_weather)
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
            case R.id.nav_chat_list:
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
            case R.id.nav_connections:
                navController.navigate(R.id.nav_connections);
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
                Navigation.findNavController(this, R.id.nav_host_fragment)
                        .navigate(directions);
//                }    else {
//                    Log.e("ERROR!", "No data array");
//                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();;
            Log.e("ERROR!", e.getMessage());
        }
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
