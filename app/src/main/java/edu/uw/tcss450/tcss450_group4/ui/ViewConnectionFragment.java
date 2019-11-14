package edu.uw.tcss450.tcss450_group4.ui;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewConnectionFragment extends Fragment implements View.OnClickListener{
//

    private String mJwToken;
    private int mMemberId;
    private ConnectionItem mConnectionItem;
    private int mVerified;


    public ViewConnectionFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        ViewConnectionFragmentArgs args = ViewConnectionFragmentArgs.fromBundle(getArguments());
//        mJwToken = args.getJwt();
//
//
//    }


    @Override
    public void onStart(){
        super.onStart();
        if (getArguments() != null) {
//            BlogPost blogPost = (BlogPost) getArguments().getSerializable(getString(R.string.blog_key));
            mJwToken = getArguments().getString("jwt");
            mMemberId = getArguments().getInt("memberid");
            mConnectionItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));
            mVerified = mConnectionItem.getVerified();


            ((TextView) getActivity().findViewById(R.id.fullName))
                    .setText("Name: " + mConnectionItem.getFirstName() + " " + mConnectionItem.getLastName());
            ((TextView) getActivity().findViewById(R.id.fullID))
                    .setText("ID: " + mConnectionItem.getContactId()) ;
            ((TextView) getActivity().findViewById(R.id.fullUsername))
                    .setText("Username : " + mConnectionItem.getContactUserName());
        }
        if(mVerified != 0) {
            (getActivity().findViewById(R.id.verifiedImage))
                    .setVisibility(View.GONE);
        } else {
            (getActivity().findViewById(R.id.verifiedImage))
                    .setVisibility(View.VISIBLE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_connection, container, false);

        }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button_chat = (Button) view.findViewById(R.id.fullChat);
        button_chat.setOnClickListener(this::onClick);

        Button button_delete = (Button) view.findViewById(R.id.fullDelete);
        button_delete.setOnClickListener(this::onClick);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fullDelete:
                removeConnection();
                //navigate to chat
                break;
            case R.id.fullChat:
//                Log.d("DEBUG", "entered");
//                Navigation.findNavController(getView())
//                        .navigate(R.id.action_nav_login_to_nav_register);

                break;
        }

    }

        private void removeConnection() {
        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_remove))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberIdUser", mMemberId);
            msgBody.put("memberIdOther", mConnectionItem.getContactId());
        } catch (JSONException e) {
            Log.wtf("MEMBERID", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriConnection.toString(), msgBody)
                .onPostExecute(this::handleRemoveOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();


    }



    private void handleRemoveOnPostExecute(String result) {

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

                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(directions);

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



}
