package edu.uw.tcss450.tcss450_group4.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.Message;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static edu.uw.tcss450.tcss450_group4.R.id.nav_host_fragment;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_add_friend_to_new_chat;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_chats;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_create_chat;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CreateChatFragment extends Fragment implements View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<ConnectionItem> mFriendList;
    private ArrayList<Integer> mFriendIDList;
    private String mJwToken;
//    private String mEmail;
    private String mChatId;
    private Bundle mBundle;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CreateChatFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CreateChatFragment newInstance(int columnCount) {
        CreateChatFragment fragment = new CreateChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = new Bundle();
        mFriendIDList = MyCreateChatRecyclerViewAdapter.getFriendIDList();
        CreateChatFragmentArgs args = CreateChatFragmentArgs.fromBundle(Objects.requireNonNull(getArguments()));
        mFriendList = new ArrayList<>(Arrays.asList(args.getFriendList()));
        mJwToken = getArguments().getString("jwt");
        mFriendIDList.add(args.getMemberId());
//        mEmail = args.getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createchat_list, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.list);
        // Set the adapter
        if (rv instanceof RecyclerView) {
            Context context = rv.getContext();
            RecyclerView recyclerView = rv;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(new MyCreateChatRecyclerViewAdapter(mFriendList, null));


        }
        Button btn_create_new_chat = (Button) view.findViewById(R.id.button_create_new_chat);
        btn_create_new_chat.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_new_chat:
                createNewChat();
                addFriendToNewChat();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
//                final Bundle bundle = new Bundle();
//                bundle.putString("chatid", mChatId);
//                bundle.putString("email", mEmail);
//                bundle.putString("jwt", mJwToken);
//                Navigation.findNavController(getView()).navigate(R.id.action_nav_create_chat_to_nav_view_chat, bundle);

                break;
        }
    }

    private void createNewChat() {

        EditText editText_ChatName = getActivity().findViewById(R.id.editText_chatName);
        Uri uriCreateChat = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_chats))
                .appendPath(getString(ep_create_chat))
                .build();
        try{
            JSONObject msgBody = new JSONObject();
                String chatName = editText_ChatName.getText().toString();
                msgBody.put("chatName", chatName);

                new SendPostAsyncTask.Builder(uriCreateChat.toString(), msgBody)
                        .onCancelled(error -> Log.e("CREATE CHAT FRAG", error))
                        .addHeaderField("authorization", mJwToken)
                        .build().execute();
        } catch (JSONException e){
            Log.wtf("chatName", "Error creating JSON: " + e.getMessage());
        }
    }
    private void addFriendToNewChat() {
        Uri uriCreateChat = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_chats))
                .appendPath(getString(ep_add_friend_to_new_chat))
                .build();
        try {
            for (int i = 0; i < mFriendIDList.size(); i++) {
                JSONObject msgBody = new JSONObject();
                msgBody.put("contactID", mFriendIDList.get(i));
                new SendPostAsyncTask.Builder(uriCreateChat.toString(), msgBody)
                        .onPostExecute(this::handleCreateChatOnPost)
                        .onCancelled(error -> Log.e("ADD FRIEND TO NEW CHAT FRAG", error))
                        .addHeaderField("authorization", mJwToken)
                        .build().execute();
                Log.wtf("Message", "created successful: " + msgBody.get("contactID"));
            }
        } catch (JSONException e){
            Log.wtf("chatName", "Error creating JSON: " + e.getMessage());
        }
    }

    private void handleCreateChatOnPre() {
        getActivity().findViewById(R.id.layout_createChat_wait).setVisibility(View.VISIBLE);
    }
    private void handleCreateChatOnPost (String result) {
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean(getString(R.string.keys_json_success));
            if (success) {
                mChatId = resultJSON.getString("chatid");
//                Navigation.findNavController(getView()).navigate(R.id.action_nav_create_chat_to_nav_view_chat, bundle);

                Message[] message = new Message[0];
                MobileNavigationDirections.ActionGlobalNavViewChat directions;
                directions = ViewChatFragmentDirections.actionGlobalNavViewChat(message);
                directions.setJwt(mJwToken);
                directions.setChatId(mChatId);
                Navigation.findNavController(getActivity(), nav_host_fragment).navigate(directions);
            }
        } catch (JSONException e) {
            Log.wtf("JSON_PARSE_ERROR", "Error creating JSON: " + e.getMessage());
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
    public class Holder {
        CheckBox contactId;
    }
}
