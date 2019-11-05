package edu.uw.tcss450.tcss450_group4.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.NewWeather;
import edu.uw.tcss450.tcss450_group4.utils.JSONWeatherParser;
import edu.uw.tcss450.tcss450_group4.utils.WeatherHttpClient;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link WeatherFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link WeatherFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class WeatherFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double mLongitude;
    private double mLatitude;
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView hum;
    private ImageView imgView;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO:
//        WeatherFragmentArgs.fromBundle(getArguments()).getWeather();
//        HomeActivityArgs.fromBundle(getArguments()).getWeather();
//        if (getArguments() != null) {
//            Weather weather = (Weather) getArguments().get(getString(R.string.keys_weather));
//            BlogPost blogPost = (BlogPost) getArguments().get(getString(R.string.key_blog_post_view));
//            //Log.d("Click Student", student.name);
//            TextView tvId = getActivity().findViewById(R.id.tv_blogPostTitle);
//            tvId.setText(blogPost.getTitle());
//            TextView tvName = getActivity().findViewById(R.id.tv_blogPostDate);
//            tvName.setText(blogPost.getPubDate());
//            TextView tvDetails = getActivity().findViewById(R.id.tv_blogPostTeaser);
//            tvDetails.setText(HtmlCompat.fromHtml((blogPost.getTeaser()), HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
//        }
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            ((TextView) getActivity().findViewById(R.id.weather_currentLocationLongitude))
//                    .setText("Longitude: NULL");
//            ((TextView) getActivity().findViewById(R.id.weather_currentLocationLatitude))
//                    .setText("Latitude: NULL");
        } else {
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            mLongitude = location.getLongitude();
//            mLatitude = location.getLatitude();
//            Log.d("long", " " + mLongitude);
//            ((TextView) getActivity().findViewById(R.id.weather_currentLocationLongitude))
//                    .setText("Longitude: " + mLongitude);
//            ((TextView) getActivity().findViewById(R.id.weather_currentLocationLatitude))
//                    .setText("Latitude: " + mLatitude);
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLongitude = location.getLongitude();
        mLatitude = location.getLatitude();
        cityText = (TextView) getActivity().findViewById(R.id.cityText);
        condDescr = (TextView) getActivity().findViewById(R.id.condDescr);
        temp = (TextView) getActivity().findViewById(R.id.temp);
        hum = (TextView) getActivity().findViewById(R.id.hum);
        press = (TextView) getActivity().findViewById(R.id.press);
        windSpeed = (TextView) getActivity().findViewById(R.id.windSpeed);
        windDeg = (TextView) getActivity().findViewById(R.id.windDeg);
        imgView = (ImageView) getActivity().findViewById(R.id.condIcon);
//        getAddressFromLocation(location,null, null);

        JSONWeatherTask task = new JSONWeatherTask();
        String city = "London,UK";
//        task.execute(new String[]{data});
        task.execute(new String[]{"" + round(mLongitude, 2), "" + round(mLatitude, 2)});
    }

    public static void getAddressFromLocation(
            final Location location, final Context context, final Handler handler) {
//        Thread thread = new Thread() {
//            @Override public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality

                        result = address.getAddressLine(0) + ", " + address.getLocality();
                        Log.d("address1", result);
                    }
                } catch (IOException e) {
                    Log.e("GEOFAIL", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
//            }
//        };
//        thread.start();
    }

    //    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment WeatherFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static WeatherFragment newInstance(String param1, String param2) {
//        WeatherFragment fragment = new WeatherFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, NewWeather> {

        @Override
        protected NewWeather doInBackground(String... params) {
            NewWeather weather = new NewWeather();
//            String longitude = ( (new WeatherHttpClient()).getWeatherData(params[0]));
//            String latitude = ( (new WeatherHttpClient()).getWeatherData(params[1]));
            String data = ((new WeatherHttpClient()).getWeatherData(params[0], params[1]));

            try {
                weather = JSONWeatherParser.getWeather(data);
//                weather = JSONWeatherParser.getWeather(longitude, latitude);

                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
                return weather;

            }

        @Override
        protected void onPostExecute(NewWeather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "�C");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");
            press.setText("" + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            windDeg.setText("" + weather.wind.getDeg() + "�");
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
