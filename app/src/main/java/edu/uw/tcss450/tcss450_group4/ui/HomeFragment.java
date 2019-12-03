package edu.uw.tcss450.tcss450_group4.ui;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.HomeActivityArgs;
import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Chat;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.State;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static edu.uw.tcss450.tcss450_group4.R.color.redviolet;
import static edu.uw.tcss450.tcss450_group4.R.color.uwPurple;
import static edu.uw.tcss450.tcss450_group4.R.id.layout_connection_wait;
import static edu.uw.tcss450.tcss450_group4.R.id.layout_weather_wait;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_host_fragment;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_cityCountry;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_conditionIcon;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_conditonDescription;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_humidity;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_pressure;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_temperature;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_temperatureSwitch;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_windDegree;
import static edu.uw.tcss450.tcss450_group4.R.id.weather_windSpeed;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_chats;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_connection;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_getall;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_requestsReceived;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_connections;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_firstname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_lastname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_memberid;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_username;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_login_success;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.getImgUrl;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.tempFromKelvinToCelsiusString;
import static edu.uw.tcss450.tcss450_group4.model.WeatherHelper.tempFromKelvinToFahrenheitString;

/**
 * A simple {@link Fragment} subclass.
 * @author Ken Gil Romero kgmr@uw.edu
 */
public class HomeFragment extends Fragment {
    // the view of the fragmen
    private View mView;
    //the  weather of the fragment
    private Weather mWeather;
    // the char degree of the fragment
    private static final char DEGREE = (char) 0x00B0;
    private ConnectionItem[] mConnectionItems;
    private ConnectionItem mConItem;
    private Chat[] mChats;
    private String mJwToken;
    private int mMemberId;
    private String mEmail;
    private int mColumnCount = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        HomeActivityArgs args = HomeActivityArgs.fromBundle(getArguments());
//        mJwToken = args.getJwt();
//        mEmail = args.getCredentials().getEmail();
//        mMemberId = args.getMemberId();
//        mConnectionItem = new ArrayList<>(Arrays.asList(args.getConnectionitems()));
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * when view is created
     * @param view  the view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(layout_weather_wait).setVisibility(View.VISIBLE);
        view.findViewById(weather_temperatureSwitch).setVisibility(View.INVISIBLE);
        initialization(view);

        HomeFragmentArgs args = HomeFragmentArgs.fromBundle(getArguments());
        mJwToken = args.getJwt();
        mMemberId = args.getMemberId();

        view.findViewById(layout_connection_wait).setVisibility(View.VISIBLE);
        gotoConnection();
        gotoChat();

//        mConnectionItem = new ArrayList<>(Arrays.asList(args.getConnectionItems()));
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
                mChats = new Chat[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonChatLists = data.getJSONObject(i);

                    String recentMessage = jsonChatLists.getString("message");
                    if (recentMessage != "null") {
                        mChats[i] = (new Chat.Builder(jsonChatLists.getString("chatid"),
                                jsonChatLists.getString("name"),
                                jsonChatLists.getString("message"),
                                convertTimeStampToDate(jsonChatLists.getString("timestamp")))
                                .build());
                    } else {
                        mChats[i] = (new Chat.Builder(jsonChatLists.getString("chatid"),
                                jsonChatLists.getString("name"),
                                "",
                                "")
                                .build());
                    }
                }
                RecyclerView rv = getView().findViewById(R.id.chatList);
                // Set the adapter
                if (rv instanceof RecyclerView) {
                    Context context = rv.getContext();
                    RecyclerView recyclerView = rv;
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }
                    recyclerView.setAdapter(new MyChatRecyclerViewAdapter(new ArrayList<>(Arrays.asList(mChats)), this::displayChat));
                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    private void displayChat(Chat chat) {

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

    private void gotoConnection() {
        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_connection))
                .appendPath(getString(ep_requestsReceived))
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
        try {
            boolean hasConnection = false;
            JSONObject root = new JSONObject(result);
            if (root.has(getString(keys_json_connection_connections))) {
                hasConnection = true;
            } else {
                Log.e("ERROR!", "No connection");
            }

            if (hasConnection) {
                JSONArray connectionJArray = root.getJSONArray(
                        getString(keys_json_connection_connections));
                mConnectionItems = new ConnectionItem[connectionJArray.length()];
                for (int i = 0; i < connectionJArray.length(); i++) {
                    JSONObject jsonConnection = connectionJArray.getJSONObject(i);
                    mConnectionItems[i] = new ConnectionItem(
                            jsonConnection.getInt(
                                    getString(keys_json_connection_memberid))
                            , jsonConnection.getString(
                            getString(keys_json_connection_firstname))
                            , jsonConnection.getString(
                            getString(keys_json_connection_lastname))
                            , jsonConnection.getString(
                            getString(keys_json_connection_username)));
                }
                RecyclerView rv = getView().findViewById(R.id.connectionList);
                if (rv instanceof RecyclerView ) {
                    Context context = rv.getContext();
                    RecyclerView recyclerView = (RecyclerView) rv;
                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }
                    recyclerView.setAdapter(new MyConnectionGUIRecyclerViewAdapter(new ArrayList<>(Arrays.asList(mConnectionItems)), this::displayConnection));
                }
                getView().findViewById(layout_connection_wait).setVisibility(View.INVISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayConnection(ConnectionItem theConnection) {

        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_getfriend))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberIdUser", mMemberId);
            msgBody.put("memberIdOther", theConnection.getContactId());
        } catch (JSONException e) {
            Log.wtf("MEMBERID", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriConnection.toString(), msgBody)
                .onPostExecute(this::handleDisplayOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();


    }

    private void handleDisplayOnPostExecute(String result) {
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
                JSONObject connectionJObject = root.getJSONObject(
                        getString(R.string.keys_json_connection_connections));
                if (connectionJObject.get(getString(R.string.keys_json_connection_verified)) != null) {
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,0);

                } else {
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,1);
                }


                final Bundle args = new Bundle();
                args.putSerializable(getString(R.string.keys_connection_view), mConItem);
                args.putString("jwt", mJwToken);
                args.putInt("memberid", mMemberId);
                Navigation.findNavController(getView())
                        .navigate(R.id.action_nav_connectionGUI_to_viewConnectionFragment, args);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialize all fields
     * @param view the view given
     */
    private void initialization(@NonNull View view) {
        mView = view;
        HomeFragmentArgs args = HomeFragmentArgs.fromBundle(Objects.requireNonNull(getArguments()));
        mWeather = Objects.requireNonNull(args).getWeather();
        if (mWeather != null) {
            setComponents();
        }
    }

    /**
     * set components and their action
     */
    private void setComponents() {
        setWeather();
        mView.findViewById(weather_temperatureSwitch).setOnClickListener(e -> switchTemperature());
    }

    /**
     * set's the weather of the view
     */
    private void setWeather() {
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
        TextView cityText = mView.findViewById(weather_cityCountry);
        TextView condDescr = mView.findViewById(weather_conditonDescription);
        TextView temp = mView.findViewById(weather_temperature);
        TextView hum = mView.findViewById(weather_humidity);
        TextView press = mView.findViewById(weather_pressure);
        TextView windSpeed = mView.findViewById(weather_windSpeed);
        TextView windDeg = mView.findViewById(weather_windDegree);

        if (mWeather.getState() == null) {
            cityText.setText(String.format("%s, %s", mWeather.getCity(), mWeather.getCountry()));
        } else {
            if (State.valueOfName(mWeather.getState()) == State.UNKNOWN) {
                cityText.setText(String.format("%s, %s, %s", mWeather.getCity()
                        , mWeather.getState()
                        , mWeather.getCountry()));
            } else {
                cityText.setText(String.format("%s, %s, %s", mWeather.getCity()
                        , State.valueOfName(mWeather.getState()).getAbbreviation()
                        , mWeather.getCountry()));
            }
        }
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
                        mView.findViewById(weather_temperatureSwitch).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    /**
     * switch the temperature of the weather
     */
    private void switchTemperature() {
        if (((Switch) mView.findViewById(weather_temperatureSwitch)).isChecked()) {
            TextView temp = mView.findViewById(weather_temperature);
            temp.setText(tempFromKelvinToFahrenheitString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), redviolet));

        } else {
            TextView temp = mView.findViewById(weather_temperature);
            temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));
        }
    }
}
