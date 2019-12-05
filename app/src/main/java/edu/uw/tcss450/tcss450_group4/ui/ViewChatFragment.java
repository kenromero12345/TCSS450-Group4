package edu.uw.tcss450.tcss450_group4.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Chat;
import edu.uw.tcss450.tcss450_group4.model.Message;
import edu.uw.tcss450.tcss450_group4.utils.PushReceiver;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewChatFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "CHAT_FRAG";
    private int mColumnCount = 1;
    private TextView mUsesrnameOutputTextView;
    private TextView mMessageOutputTextView;
    private EditText mMessageInputEditText;
    private String CHAT_ID = "";
//    private String mEmail;
    private int mMemberId;
    private String mJwToken;
    private String mSendUrl;
    private PushMessageReceiver mPushMessageReciever;
    private RecyclerView mMessageRecycler;
    private MyMessageListRecyclerViewAdapter mMessageAdapter;
    private List<Message> mMessageList;
    private int mMessageCount;
    public ViewChatFragment() {
        // Required empty public constructor
    }

    public static ViewChatFragment newInstance(int columnCount) {
        ViewChatFragment fragment = new ViewChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewChatFragmentArgs args = ViewChatFragmentArgs.fromBundle(getArguments());
        //mEmail = args.getEmail();
        if(getArguments() != null) {
//            Chat chat = (Chat) getArguments().getSerializable(getString(R.string.chat_object));
//            mEmail = getArguments().getString("email");
//            mJwToken = getArguments().getString("jwt");
//            mMessageList = (List<Message>) getArguments().getSerializable("List");
//            mEmail = args.getEmail();
            mJwToken = args.getJwt();
            mMessageList = new ArrayList<>(Arrays.asList(args.getMessageList()));
            CHAT_ID = args.getChatId();
            mMemberId = args.getMemberId();
            if(CHAT_ID.equals("")) {
                CHAT_ID = getArguments().getString("chatid");
            }
        }
//        mMessageRecycler = (RecyclerView) getActivity().findViewById(R.id.reyclerview_message_list);
//        mMessageAdapter = new MessageListAdapter(this, messageList);
//        mMessageRecycler.setLayoutManager(new RelativeLayout(this));
    }

    @Override
    public void onStart() {
        super.onStart();

        //We will use this url every time the user hits send. Let's only build it once, ya?
        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_send))
                .build()
                .toString();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        mUsesrnameOutputTextView = view.findViewById(R.id.txt_friendUserName);
        mMessageOutputTextView = view.findViewById(R.id.txt_theirMessage);
        mMessageInputEditText = view.findViewById(R.id.editText_chat_message_input);
        mMessageCount = mMessageList.size() - 1;
        RecyclerView rv = view.findViewById(R.id.viewChatList);
        if (rv instanceof RecyclerView) {
            Context context = rv.getContext();
            RecyclerView recyclerView = rv;
            if (mColumnCount <= 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mMessageAdapter = new MyMessageListRecyclerViewAdapter(mMessageList, null);
            recyclerView.setAdapter(mMessageAdapter);
        }
//
        view.findViewById(R.id.button_chat_send).setOnClickListener(this::handleSendClick);

    }

    private void handleSendClick(final View theButton) {
        String msg = mMessageInputEditText.getText().toString();
        Log.d("MESSAGE", "CLICKED");
        Log.d("MESSAGE", mMemberId + "");
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("memberId", mMemberId);
            messageJson.put("message", msg);
            messageJson.put("chatId", CHAT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SendPostAsyncTask.Builder(mSendUrl, messageJson)
                .onPostExecute(this::endOfSendMsgTask)
                .onCancelled(error -> Log.e(TAG, error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();

        InputMethodManager inputManager = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        mMessageAdapter.notifyDataSetChanged();

    }

    private void endOfSendMsgTask(final String result) {
        try {
            //This is the result from the web service
            JSONObject res = new JSONObject(result);

            if(res.has("success")  && res.getBoolean("success")) {
                //The web service got our message. Time to clear out the input EditText
                mMessageInputEditText.setText("");

                //its up to you to decide if you want to send the message to the output here
                //or wait for the message to come back from the web service.
                mMessageCount++;

                Log.e("SCROLL", mMessageCount + "");
                ((RecyclerView) getView().findViewById(R.id.viewChatList)).smoothScrollToPosition(mMessageCount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReciever == null) {
            mPushMessageReciever = new PushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        getActivity().registerReceiver(mPushMessageReciever, iFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReciever != null){
            getActivity().unregisterReceiver(mPushMessageReciever);
        }
    }

//    private List<Message> getMessageList(String chatId) {
//        Uri uriGetAllMessages = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(R.string.ep_base_url))
//                .appendPath(getString(R.string.ep_messaging_base))
//                .appendPath(getString(R.string.ep_messaging_getAll))
//                .build();
//        try {
//            JSONObject msgBody = new JSONObject();
//            msgBody.put("chatId", CHAT_ID);
//
//            new SendPostAsyncTask.Builder(uriGetAllMessages.toString(), msgBody)
//                    .onPostExecute(this::handleGetAllMessageOnPostExecute)
//                    .onCancelled(error -> Log.e("GET ALL MESSAGES", error))
//                    .addHeaderField("authorization", mJwToken)
//                    .build().execute();
//        } catch (JSONException e) {
//            Log.wtf("get messages", "Error creating JSON: " + e.getMessage());
//        }
//    }
//    private void handleGetAllMessageOnPostExecute(final String result) {
//        try {
//            JSONObject root = new JSONObject(result);
//            if (root.has("success") && root.getBoolean("success")) {
//                JSONArray data = root.getJSONArray("messages");
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class PushMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE")) {

                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
                mMessageAdapter.addMessage(sender, messageText, "");
                mMessageAdapter.notifyDataSetChanged();
//                mUsesrnameOutputTextView.append(sender+": ");
//                mMessageOutputTextView.setText(messageText);
//                mMessageOutputTextView.append(sender + ":" + messageText);
//                mMessageOutputTextView.append(System.lineSeparator());
//                mMessageOutputTextView.append(System.lineSeparator());
            }
        }
    }

}
