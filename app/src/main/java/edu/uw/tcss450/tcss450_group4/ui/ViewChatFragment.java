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
    private int mMemberId;
    private EditText mMessageInputEditText;
    private String CHAT_ID = "";
//    private String mEmail;
    private String mJwToken;
    private String mSendUrl;
    private PushMessageReceiver mPushMessageReciever;
    private RecyclerView mMessageRecycler;
    private MyMessageListRecyclerViewAdapter mMessageAdapter;
    private List<Message> mMessageList;
    private int mMessageCount;
    private RecyclerView mRecyclerView;
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
            mMemberId = args.getMemberId();
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
        mMessageInputEditText = view.findViewById(R.id.editText_chat_message_input);
        mMessageCount = mMessageList.size() - 1;
        RecyclerView rv = view.findViewById(R.id.viewChatList);
        mMessageAdapter = new MyMessageListRecyclerViewAdapter(mMessageList, mMemberId, null);
        if (rv instanceof RecyclerView) {
            Context context = rv.getContext();
            mRecyclerView = rv;
            if (mColumnCount <= 1) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setStackFromEnd(true);
                mRecyclerView.setLayoutManager(linearLayoutManager);
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mRecyclerView.setAdapter(mMessageAdapter);
//            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

        }
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
//        mMessageAdapter.notifyDataSetChanged();

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

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class PushMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("SENDER") && intent.hasExtra("MESSAGE") && intent.hasExtra("SENDERID")) {

                String sender = intent.getStringExtra("SENDER");
                String messageText = intent.getStringExtra("MESSAGE");
                String chatid = intent.getStringExtra("CHATID");
                int senderId = intent.getIntExtra("SENDERID", -1);
                String profileUri = intent.getStringExtra("PROFILEURI");
                mMessageAdapter.addMessage(sender, senderId, messageText, "", profileUri);
                mMessageAdapter.notifyDataSetChanged();
                ((RecyclerView) getView().findViewById(R.id.viewChatList)).smoothScrollToPosition(mMessageAdapter.getItemCount());
            }

        }
    }

}
