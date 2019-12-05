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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ConnectionRequestFragment extends Fragment {

    private List<ConnectionItem> mConnectionItem;
    private String mJwToken;
    private int mMemberId;
    private ConnectionItem mConItem;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionRequestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ConnectionRequestFragment newInstance(int columnCount) {
        ConnectionRequestFragment fragment = new ConnectionRequestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectionRequestFragmentArgs args = ConnectionRequestFragmentArgs.fromBundle(getArguments());
        mJwToken = args.getJwt();
        mMemberId = args.getMemberid();
        mConnectionItem = new ArrayList<>(Arrays.asList(args.getConnectionitems()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connectionrequest_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.connectionRequestList);
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
    }


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
            String status = root.getString("status");
            Log.e("Boolean!", root.getString("status"));
            if (hasConnection){
                JSONObject connectionJObject = root.getJSONObject(
                        getString(R.string.keys_json_connection_connection));
                if (status.equals("sent request to person")) {
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
                            ,connectionJObject.getString(
                            getString(R.string.keys_json_connection_username))
                            ,3
                            ,connectionJObject.getString(getString(R.string.keys_json_connection_image)));

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
                        .navigate(R.id.action_nav_connection_request_to_viewConnectionFragment, args);
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
