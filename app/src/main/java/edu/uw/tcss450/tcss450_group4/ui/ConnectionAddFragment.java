package edu.uw.tcss450.tcss450_group4.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionAddFragment extends Fragment implements View.OnClickListener{
    private String mJwToken;
    private ConnectionItem mConItem;
    private boolean mBoolean = false;
    private int mMemberId;
    private String mMessage;


    public ConnectionAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_connection_add, container, false);
        LinearLayout layout = view.findViewById(R.id.addLayout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConnection();
            }
        });

        return view;
    }

    private void displayConnection() {
        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_getPerson))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberIdUser", mMemberId);
            msgBody.put("memberIdOther", mConItem.getContactId());
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
            if (root.has(getString(R.string.keys_json_connection_connection))){
                hasConnection = true;
            } else {
                Log.e("ERROR!", "No connection");
            }

            if (hasConnection){
                JSONObject connectionJObject = root.getJSONObject(
                        getString(R.string.keys_json_connection_connection));
                String status = root.getString("status");
                Log.e("Verified add fragment", status);
                Log.e("Member id", mConItem.getContactId());
                if (status.equals("already connected")) {
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,1
                            , connectionJObject.getString(getString(R.string.keys_json_connection_image)));

                }
                else if (status.equals("sent request to person")) {
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,2
                            ,connectionJObject.getString(getString(R.string.keys_json_connection_image)));

                }
                else if (status.equals("received request from person")) {
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            , 3
                            , connectionJObject.getString(getString(R.string.keys_json_connection_image)));
                } else {
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,0
                            ,connectionJObject.getString(getString(R.string.keys_json_connection_image)));
                }



                final Bundle args = new Bundle();
                args.putSerializable(getString(R.string.keys_connection_view), mConItem);
                args.putString("jwt", mJwToken);
                args.putInt("memberid", mMemberId);
                Navigation.findNavController(getView())
                        .navigate(R.id.action_nav_connection_add_to_viewConnectionFragment, args);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
//        ((TextView) getActivity().findViewById(R.id.connection_firstname))
//                .setText("hello");
        if (getArguments() != null) {
            mJwToken = getArguments().getString("jwt");
            mBoolean = getArguments().getBoolean("boolean");
            mConItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));
            mMemberId = getArguments().getInt("memberId");
//            Log.e("Boolean!", String.valueOf(mBoolean));

        }
        LinearLayout layout = getActivity().findViewById(R.id.addLayout);
        layout.setVisibility(View.GONE);
        Button button = getActivity().findViewById(R.id.connectionAddButton);
        button.setVisibility(View.GONE);
//        if (mBoolean == true) {
//            Log.e("firstname!", mConItem.getFirstName());
//            Log.e("lastname!", mConItem.getLastName());
//            Log.e("memberid!", mConItem.getContactId());
//            Log.e("username!", mConItem.getContactUserName());
//            ((TextView) getActivity().findViewById(R.id.connection_firstname))
//                    .setText("Name: " + mConItem.getFirstName() + " " + mConItem.getLastName());
//            ((TextView) getActivity().findViewById(R.id.connection_memberid))
//                    .setText("ID: " + mConItem.getContactId()) ;
//            ((TextView) getActivity().findViewById(R.id.connection_username))
//                    .setText("Username : " + mConItem.getContactUserName());
//        }



//        if (mBoolean == true) {
//            ((TextView) getActivity().findViewById(R.id.connection_firstname))
//                    .setText("Name: " + mConItem.getFirstName() + " " + mConItem.getLastName());
//            ((TextView) getActivity().findViewById(R.id.connection_memberid))
//                    .setText("ID: " + mConItem.getContactId()) ;
//            ((TextView) getActivity().findViewById(R.id.connection_username))
//                    .setText("Username : " + mConItem.getContactUserName());
//
//        }
    }

//    public void populateSearch(){
//
//        ((TextView) getActivity().findViewById(R.id.connection_firstname))
//                .setText("Name: " + mConItem.getFirstName() + " " + mConItem.getLastName());
//        ((TextView) getActivity().findViewById(R.id.connection_memberid))
//                .setText("ID: " + mConItem.getContactId()) ;
//        ((TextView) getActivity().findViewById(R.id.connection_username))
//                .setText("Username : " + mConItem.getContactUserName());
//    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ((TextView) getActivity().findViewById(R.id.connection_firstname))
//                .setText("Name: ");
//
//        if (mBoolean == true) {
//            Log.e("firstname!", mConItem.getFirstName());
//            Log.e("lastname!", mConItem.getLastName());
//            Log.e("memberid!", mConItem.getContactId());
//            Log.e("username!", mConItem.getContactUserName());
//            populateSearch();
//
//        }


        Button button_search = (Button) view.findViewById(R.id.connectionSearchButton);
        button_search.setOnClickListener(this::onClick);

        Button button_add = (Button) view.findViewById(R.id.connectionAddButton);
        button_add.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.connectionSearchButton:
                searchConnection();
                //navigate to chat
                break;
            case R.id.connectionAddButton:
                addConnection();

                break;
        }

    }

    private void searchConnection() {
        EditText userNameText = getActivity().findViewById(R.id.connectionUserNameText);
        String username = userNameText.getText().toString();
        Uri uriSearch = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_search))
                .build();
//        Log.e("name", String.valueOf(username));
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("username", username);
        } catch (JSONException e) {
            Log.wtf("username", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriSearch.toString(), msgBody)
                .onPostExecute(this::handleSearchOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();

    }

    private void addConnection() {
        EditText userNameText = getActivity().findViewById(R.id.connectionUserNameText);
        String username = userNameText.getText().toString();
        Log.e("ERROR!", mConItem.getContactId());
        Uri uriSearch = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_add))
                .build();
//        Log.e("name", String.valueOf(username));
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberIdUser", mMemberId);
            msgBody.put("memberIdOther", mConItem.getContactId());
        } catch (JSONException e) {
            Log.wtf("username", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriSearch.toString(), msgBody)
                .onPostExecute(this::handleAddOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();
    }

    private void handleAddOnPostExecute(String result) {
        //parse JSON
        Button button_add = getActivity().findViewById(R.id.connectionAddButton);
        try {
            boolean hasSuccess = false;
            JSONObject root = new JSONObject(result);
            Log.e("root!", String.valueOf(root));
            Log.e("Success!", String.valueOf(root.getBoolean("success")));
            boolean success = root.getBoolean("success");
            if (root.has("success")){
                hasSuccess = true;
            } else {
                Log.e("ERROR!", "No Success");
            }

            if (hasSuccess){
                if (success == true) {
                    button_add.setText("Added!");
                } else {
                    button_add.setText("Already Added!");

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void handleSearchOnPostExecute(String result) {
        //parse JSON
        try {

//            boolean hasSuccess = false;
//            JSONObject root = new JSONObject(result);
//            if (root.has(getString(R.string.keys_json_connection_success))){
//                Boolean booleanSuccess = root.getBoolean(String.valueOf(R.string.keys_json_connection_success));
//            } else {
//                mMessage = root.getString("message");
//                Log.e("ERROR!", "No Member");
//
//            }
            JSONObject root = new JSONObject(result);
            Boolean booleanSuccess = root.getBoolean("success");

            if(booleanSuccess == false) {
                LinearLayout layout = getActivity().findViewById(R.id.addLayout);
                layout.setVisibility(View.GONE);

                Button button = getActivity().findViewById(R.id.connectionAddButton);
                button.setVisibility(View.GONE);
            } else {
                JSONObject connectionJObject = root.getJSONObject(
                        getString(R.string.keys_json_connection_member));
                    mConItem = new ConnectionItem(connectionJObject.getInt(
                            getString(R.string.keys_json_connection_memberid))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_firstname))
                            , connectionJObject.getString(
                            getString(R.string.keys_json_connection_lastname))
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,connectionJObject.getString(getString(R.string.keys_json_connection_image)));

                ImageView img = getActivity().findViewById(R.id.profileImageAdd);
                String cleanImage = mConItem.getContactImage().replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
                byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);

                    ((TextView) getActivity().findViewById(R.id.connection_firstname))
                            .setText("Name: " + mConItem.getFirstName() + " " + mConItem.getLastName());
//                    ((TextView) getActivity().findViewById(R.id.connection_memberid))
//                            .setText("ID: " + mConItem.getContactId()) ;
                    ((TextView) getActivity().findViewById(R.id.connection_username))
                            .setText("Username : " + mConItem.getContactUserName());

                    LinearLayout layout = getActivity().findViewById(R.id.addLayout);
                    layout.setVisibility(View.VISIBLE);

                    Button button = getActivity().findViewById(R.id.connectionAddButton);
                    button.setVisibility(View.VISIBLE);
            }


//            if (booleanSuccess){
//                if(booleanSuccess == false){
//                    LinearLayout layout = getActivity().findViewById(R.id.addLayout);
//                    layout.setVisibility(View.GONE);
//
//                    Button button = getActivity().findViewById(R.id.connectionAddButton);
//                    button.setVisibility(View.GONE);
//                } else {
//
//                    JSONObject connectionJObject = root.getJSONObject(
//                            getString(R.string.keys_json_connection_member));
//                    mConItem = new ConnectionItem(connectionJObject.getInt(
//                            getString(R.string.keys_json_connection_memberid))
//                            , connectionJObject.getString(
//                            getString(R.string.keys_json_connection_firstname))
//                            , connectionJObject.getString(
//                            getString(R.string.keys_json_connection_lastname))
//                            ,connectionJObject.getString(
//                            getString(R.string.keys_json_connection_username)));
//
//
//
//                    ((TextView) getActivity().findViewById(R.id.connection_firstname))
//                            .setText("Name: " + mConItem.getFirstName() + " " + mConItem.getLastName());
//                    ((TextView) getActivity().findViewById(R.id.connection_memberid))
//                            .setText("ID: " + mConItem.getContactId()) ;
//                    ((TextView) getActivity().findViewById(R.id.connection_username))
//                            .setText("Username : " + mConItem.getContactUserName());
//
//                    LinearLayout layout = getActivity().findViewById(R.id.addLayout);
//                    layout.setVisibility(View.VISIBLE);
//
//                    Button button = getActivity().findViewById(R.id.connectionAddButton);
//                    button.setVisibility(View.VISIBLE);
//
//                }


//                final Bundle args = new Bundle();
//                args.putSerializable(getString(R.string.keys_connection_view), mConItem);
//                args.putString("jwt", mJwToken);
//                args.putBoolean("boolean", true);
////                args.putInt("memberid", mMemberId);
//                Navigation.findNavController(getView())
//                        .navigate(R.id.nav_connection_add, args);



        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}
