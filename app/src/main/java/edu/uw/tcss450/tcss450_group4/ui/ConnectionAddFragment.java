package edu.uw.tcss450.tcss450_group4.ui;


import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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
 * This class is the fragment to search and add a user.
 */
public class ConnectionAddFragment extends Fragment implements View.OnClickListener{
    private String mJwToken;
    private ConnectionItem mConItem;
    private boolean mBoolean = false;
    private int mMemberId;
    private String mMessage;


    /**
     * Public constructor
     */
    public ConnectionAddFragment() {
        // Required empty public constructor
    }


    /**
     * On view created method that initializes the view and the add layout. As well as
     * handles an onclick
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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

    /**
     * Web service call to display the connection.
     */
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

    /**
     * Method handling a display connection on post execute
     * @param result the json response.
     */
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

    /**
     * Method to do on start and initialize variables.
     */
    @Override
    public void onStart(){
        super.onStart();

        if (getArguments() != null) {
            mJwToken = getArguments().getString("jwt");
            mBoolean = getArguments().getBoolean("boolean");
            mConItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));
            mMemberId = getArguments().getInt("memberId");

        }
        LinearLayout layout = getActivity().findViewById(R.id.addLayout);
        layout.setVisibility(View.GONE);

    }


    /**
     * on view created method that initializes an onclick for the search.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        ImageView button_search = (ImageView) view.findViewById(R.id.connectionSearchButton);
        button_search.setOnClickListener(this::onClick);

    }

    /**
     * Method to handle on click for the search.
     * @param v the view.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.connectionSearchButton:
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                searchConnection();
                break;
        }

    }

    /**
     * Webservice call to search for a connection.
     */
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
            msgBody.put("memberId", mMemberId);
        } catch (JSONException e) {
            Log.wtf("username", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriSearch.toString(), msgBody)
                .onPostExecute(this::handleSearchOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();

    }


    /**
     * Method handling a search on post execute
     * @param result the json response.
     */
    private void handleSearchOnPostExecute(String result) {
        //parse JSON
        try {

            JSONObject root = new JSONObject(result);
            Boolean booleanSuccess = root.getBoolean("success");

            if(booleanSuccess == false) {
                LinearLayout layout = getActivity().findViewById(R.id.addLayout);
                layout.setVisibility(View.GONE);
                showNoUser();
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
                    ((TextView) getActivity().findViewById(R.id.connection_username))
                            .setText("Username : " + mConItem.getContactUserName());

                    LinearLayout layout = getActivity().findViewById(R.id.addLayout);
                    layout.setVisibility(View.VISIBLE);

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

    /**
     * If there is no user, show this message dialogue.
     */
        private void showNoUser() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Search Error");
        builder.setMessage("Could not find a user with that name!");

        builder.setNegativeButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
