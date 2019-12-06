package edu.uw.tcss450.tcss450_group4.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.ConnectionRequestNotification;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static edu.uw.tcss450.tcss450_group4.R.id.nav_host_fragment;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_connections;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_firstname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_image;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_lastname;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_memberid;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_connection_username;

/**
 * A fragment representing a list of connection items.
 */
public class ConnectionGUIFragment extends Fragment implements View.OnClickListener{

    private List<ConnectionItem> mConnectionItem;
    private String mJwToken;
    private int mMemberId;
    private ConnectionItem mConItem;
    private ConnectionRequestNotification mConnectionRequest;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionGUIFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConnectionGUIFragment newInstance(int columnCount) {
        ConnectionGUIFragment fragment = new ConnectionGUIFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate method to initialize data to be used for other methods.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectionGUIFragmentArgs args = ConnectionGUIFragmentArgs.fromBundle(getArguments());
        mJwToken = args.getJwt();
        mMemberId = args.getMemberid();
        mConnectionItem = new ArrayList<>(Arrays.asList(args.getConnectionitems()));
        mConnectionRequest = args.getConnectionRequest();
    }

    /**
     * This initializes the view of of the layout.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connectiongui_list, container, false);

        // Set the adapter

        return view;
    }

    /**
     * This method initializes the buttons and sets an onclick for each button.
     * @param view the View
     * @param savedInstanceState the saved instance.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView button_chat = (ImageView) view.findViewById(R.id.searchImage);
        ImageView button_sent = (ImageView) view.findViewById(R.id.sentImage);
        ImageView button_received = (ImageView) view.findViewById(R.id.confirmImage);
        button_chat.setOnClickListener(this::onClick);
        button_sent.setOnClickListener(this::onClick);
        button_received.setOnClickListener(this::onClick);

        RecyclerView rv = view.findViewById(R.id.connectionGuiList);
        if (rv instanceof RecyclerView ) {
            Context context = rv.getContext();
            RecyclerView recyclerView = (RecyclerView) rv;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyConnectionGUIRecyclerViewAdapter(mConnectionItem, this::displayConnection));
        }
        if (mConnectionRequest != null) {
            requestConnection();
            mConnectionRequest = null;
        }
    }

    /**
     * Method to handle on clicks of each button.
     * @param v the View
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.searchImage:
                addConnection();
                //navigate to chat
                break;


            case R.id.sentImage:
                sentConnection();

                break;


            case R.id.confirmImage:
                requestConnection();

                break;
        }

    }

    /**
     * Method for when clicking a sent button.
     */
    private void sentConnection() {

        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_requestsSent))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberId", mMemberId);
        } catch (JSONException e) {
            Log.wtf("MEMBERID", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriConnection.toString(), msgBody)
                .onPostExecute(this::handleRequestOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();


    }

    /**
     * Method for when clicking a request button.
     */
    private void requestConnection() {

        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_requestsReceived))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberId", mMemberId);
        } catch (JSONException e) {
            Log.wtf("MEMBERID", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriConnection.toString(), msgBody)
                .onPostExecute(this::handleReceivedOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();

    }

    /**
     * Method handling a received on post execute
     * @param result the json response.
     */
    private void handleReceivedOnPostExecute(String result) {
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
                        getString(keys_json_connection_connections));
                ConnectionItem[] conItem = new ConnectionItem[connectionJArray.length()];
                for(int i = 0; i < connectionJArray.length(); i++){
                    JSONObject jsonConnection = connectionJArray.getJSONObject(i);
                    conItem[i] = new ConnectionItem(
                            jsonConnection.getInt(
                                    getString(keys_json_connection_memberid))
                            , jsonConnection.getString(
                            getString(keys_json_connection_firstname))
                            , jsonConnection.getString(
                            getString(keys_json_connection_lastname))
                            ,jsonConnection.getString(
                            getString(keys_json_connection_username)),
                            jsonConnection.getString(
                                    getString(keys_json_connection_image)));
                }

                MobileNavigationDirections.ActionGlobalNavConnectionRequest directions
                        = ConnectionRequestFragmentDirections.actionGlobalNavConnectionRequest(conItem);
                directions.setJwt(mJwToken);
                directions.setMemberid(mMemberId);

                Navigation.findNavController(getActivity(), nav_host_fragment)
                        .navigate(directions);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Helper method to navigate from connection GUI to add connection.
     */
    private void addConnection() {
        final Bundle args = new Bundle();
        args.putString("jwt", mJwToken);
        args.putInt("memberId", mMemberId);
        Navigation.findNavController(getView())
                .navigate(R.id.action_nav_connectionGUI_to_nav_connection_add, args);
    }

    /**
     * Method handling a request on post execute
     * @param result the json response.
     */
    private void handleRequestOnPostExecute(String result) {
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
                        getString(keys_json_connection_connections));
                ConnectionItem[] conItem = new ConnectionItem[connectionJArray.length()];
                for(int i = 0; i < connectionJArray.length(); i++){
                    JSONObject jsonConnection = connectionJArray.getJSONObject(i);
                    conItem[i] = new ConnectionItem(
                            jsonConnection.getInt(
                                    getString(keys_json_connection_memberid))
                            , jsonConnection.getString(
                            getString(keys_json_connection_firstname))
                            , jsonConnection.getString(
                            getString(keys_json_connection_lastname))
                            ,jsonConnection.getString(
                            getString(keys_json_connection_username)),
                            jsonConnection.getString(
                                    getString(keys_json_connection_image)));
                }

                MobileNavigationDirections.ActionGlobalNavConnectionRequest directions
                        = ConnectionRequestFragmentDirections.actionGlobalNavConnectionRequest(conItem);
                directions.setJwt(mJwToken);
                directions.setMemberid(mMemberId);

                Navigation.findNavController(getActivity(), nav_host_fragment)
                        .navigate(directions);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * This displays the connection when clicked on.
     * @param theConnection the connection item.
     */
    private void displayConnection(ConnectionItem theConnection) {

        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_getPerson))
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

            Log.e("root", String.valueOf(root));
            Log.e("Status!", root.getString("status"));
//            Log.e("Status!", String.valueOf(root.getString("status") == "already connected"));
//            String status = root.getString("status");
//            Log.e("Status variable! boolean", String.valueOf(status.equals("already connected")));
            if (hasConnection){
                JSONObject connectionJObject = root.getJSONObject(
                        getString(R.string.keys_json_connection_connection));
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
                Log.e("verified!", String.valueOf(mConItem.getVerified()));

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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ConnectionItem item);
    }
}
