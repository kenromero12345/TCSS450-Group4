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
import android.widget.EditText;
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


    public ConnectionAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_add, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        ((TextView) getActivity().findViewById(R.id.connection_firstname))
                .setText("hello");
        if (getArguments() != null) {
            mJwToken = getArguments().getString("jwt");
            mBoolean = getArguments().getBoolean("boolean");
            mConItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));
            Log.e("Boolean!", String.valueOf(mBoolean));
        }
        if (mBoolean == true) {
            Log.e("firstname!", mConItem.getFirstName());
            Log.e("lastname!", mConItem.getLastName());
            Log.e("memberid!", mConItem.getContactId());
            Log.e("username!", mConItem.getContactUserName());
            ((TextView) getActivity().findViewById(R.id.connection_firstname))
                    .setText("Name: " + mConItem.getFirstName() + " " + mConItem.getLastName());
            ((TextView) getActivity().findViewById(R.id.connection_memberid))
                    .setText("ID: " + mConItem.getContactId()) ;
            ((TextView) getActivity().findViewById(R.id.connection_username))
                    .setText("Username : " + mConItem.getContactUserName());
        }



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

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) getActivity().findViewById(R.id.connection_firstname))
                .setText("Name: ");


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
            case R.id.fullChat:
//                Log.d("DEBUG", "entered");
//                Navigation.findNavController(getView())
//                        .navigate(R.id.action_nav_login_to_nav_register);

                break;
        }

    }

    private void searchConnection() {
//        mBoolean = true;
        EditText userNameText = getActivity().findViewById(R.id.connectionUserNameText);
        String username = userNameText.getText().toString();
        Uri uriSearch = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_search))
                .build();
        Log.e("name", String.valueOf(username));
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

    private void handleSearchOnPostExecute(String result) {
        //parse JSON
        try {
            boolean hasMember = false;
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_connection_member))){
                hasMember = true;
            } else {
                Log.e("ERROR!", "No Member");

            }

            if (hasMember){
                JSONObject connectionJObject = root.getJSONObject(
                        getString(R.string.keys_json_connection_member));
                mConItem = new ConnectionItem(connectionJObject.getInt(
                        getString(R.string.keys_json_connection_memberid))
                        , connectionJObject.getString(
                        getString(R.string.keys_json_connection_firstname))
                        , connectionJObject.getString(
                        getString(R.string.keys_json_connection_lastname))
                        ,connectionJObject.getString(
                        getString(R.string.keys_json_connection_username)));



                final Bundle args = new Bundle();
                args.putSerializable(getString(R.string.keys_connection_view), mConItem);
                args.putString("jwt", mJwToken);
                args.putBoolean("boolean", true);
//                args.putInt("memberid", mMemberId);
                Navigation.findNavController(getView())
                        .navigate(R.id.nav_connection_add, args);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}
