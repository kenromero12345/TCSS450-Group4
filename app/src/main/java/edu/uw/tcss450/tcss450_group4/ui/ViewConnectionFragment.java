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
            ConnectionItem connectionItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));

            ((TextView) getActivity().findViewById(R.id.fullName))
                    .setText("ID: " + connectionItem.getContactId());
            ((TextView) getActivity().findViewById(R.id.fullID))
                    .setText("Name: " + connectionItem.getFirstName() + " " + connectionItem.getLastName());
            ((TextView) getActivity().findViewById(R.id.fullUsername))
                    .setText("Username : " + connectionItem.getContactUserName());
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
            msgBody.put("memberIdOther", R.id.fullID);
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

    }


}
