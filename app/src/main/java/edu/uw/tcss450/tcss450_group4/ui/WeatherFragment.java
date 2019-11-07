package edu.uw.tcss450.tcss450_group4.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Weather;
import edu.uw.tcss450.tcss450_group4.utils.GetAsyncTask;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;


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
    private Weather mWeather;
    private View mView;
//    private String mSendUrl;
    private String mEmail;
    private String mJwToken;

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WeatherFragmentArgs args = null;
        if (getArguments() != null) {
            args = WeatherFragmentArgs.fromBundle(getArguments());
        }
        if (args != null) {
            mWeather = args.getWeather();
            mEmail = args.getEmail();
            mJwToken = args.getJwt();
        }
        mView = view;

        setWeather(view);

        ((Button) view.findViewById(R.id.weather_zipButton)).setOnClickListener(e -> attemptGetWeatherZip());
        ((Button) view.findViewById(R.id.weather_saveButton)).setOnClickListener(e -> attemptSaveWeather());
        ((Button) view.findViewById(R.id.weather_savedButton)).setOnClickListener(e -> gotoSavedWeatherRecyclerView());
    }

    private String attemptGetZip(double tLat, double tLon) {
        return "";
    }

    /**
     *
     */
    private void attemptSaveWeather() {
        JSONObject msg = new JSONObject();

        try {
            msg.put("email", mEmail);
            msg.put("city", mWeather.getCity());
            msg.put("country", mWeather.getCountry());
            msg.put("lat", mWeather.getLat());
            msg.put("lon", mWeather.getLon());
            msg.put("zip", attemptGetZip(mWeather.getLat(), mWeather.getLon()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_weather))
                .appendPath(getString(R.string.ep_send))
                .build();

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::endOfSendWeatherTask)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

    private void endOfSendWeatherTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);

            if(res.has("success")  && res.getBoolean("success")) {
                alert("save unsuccessful");
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
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_weather))
                .appendPath(getString(R.string.ep_get))
                .build();

        JSONObject msg = new JSONObject();

        try {
            msg.put("email", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleWeathersGetOnPostExecute)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

    /**
     *
     */
    private void attemptGetWeatherZip() {
        boolean success = true;
            EditText et = (EditText) mView.findViewById(R.id.weather_zipEditText);
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
     * @param view
     */
    @SuppressLint("SetTextI18n")
    private void setWeather(@NonNull View view) {
        TextView cityText = cityText = (TextView) view.findViewById(R.id.weather_cityCountry);
        TextView condDescr = condDescr = (TextView) view.findViewById(R.id.weather_conditonDescription);;
        TextView temp = (TextView) view.findViewById(R.id.weather_temperature);;
        TextView hum = (TextView) view.findViewById(R.id.weather_humidity);;
        TextView press = (TextView) view.findViewById(R.id.weather_pressure);;
        TextView windSpeed = (TextView) view.findViewById(R.id.weather_windSpeed);
        TextView windDeg = (TextView) view.findViewById(R.id.weather_windDegree);
        ImageView imgView = (ImageView) view.findViewById(R.id.weather_conditionIcon);

        cityText.setText(mWeather.getCity() + ", " + mWeather.getCountry());
        condDescr.setText(mWeather.getDescription());
        temp.setText("" + Math.round((mWeather.getTemp() - 273.15)) + "�C");
        hum.setText("" + mWeather.getHumidity() + "%");
        press.setText("" + mWeather.getPressure() + " hPa");
        windSpeed.setText("" + mWeather.getSpeed() + " mps");
        windDeg.setText("" + mWeather.getDeg() + "�");
        String IMG_URL = "http://openweathermap.org/img/wn/";
        String IMG_URL_BIG = "@2x";
        String IMG_URL_END = ".png";
        Picasso.get().load(IMG_URL + mWeather.getIcon() + IMG_URL_BIG + IMG_URL_END).into(imgView);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    /**
     *
     */
    private void getWeatherZip() {
        String zip = ((EditText)mView.findViewById(R.id.weather_zipEditText)).getText().toString().trim();

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_weather))
                .appendPath(getString(R.string.ep_zip))
                .build();

        JSONObject msg = new JSONObject();
        try {
            msg.put("zip",/* round(*/zip/*,2)*/);
        } catch (JSONException e) {
            Log.wtf("LONG/LAT", "Error creating JSON: " + e.getMessage());
        }

        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPostExecute(this::handleWeatherGetOnPostExecute)
                .onCancelled(error -> /*((EditText) mView.findViewById(R.id.weather_zipEditText)
                        ).setError("empty!!")*/Log.e(TAG, error)/*alert("save unsuccessful")*/)
                .addHeaderField("authorization", mJwToken) //add the JWT as a header
                .build().execute();
    }

    /**
     *
     */
    private void getWeatherLatLon() {
        double longitude = 0;
        double latitude = 0;
        //TODO: get long lat

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

    /**
     *
     * @param result
     */
    private void handleWeathersGetOnPostExecute(final String result) {
//        Log.d("GETSAVED", "success");
        alert("saved weathers");
    }

    /**
     *
     * @param result
     */
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

                mWeather = new Weather(
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
                        /*, mWeather.getJwt()*/);

                setWeather(mView);

            } else {
//                alert("Not a valid zip code");
                ((EditText) mView.findViewById(R.id.weather_zipEditText))
                    .setError("Not a valid zip code");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void alert(String tS) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(tS);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}