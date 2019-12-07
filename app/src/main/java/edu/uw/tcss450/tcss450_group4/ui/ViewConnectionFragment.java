package edu.uw.tcss450.tcss450_group4.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uw.tcss450.tcss450_group4.MobileNavigationDirections;
import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.model.Message;
import edu.uw.tcss450.tcss450_group4.utils.GetAsyncTask;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

import static edu.uw.tcss450.tcss450_group4.R.id.layout_homeActivity_wait;
import static edu.uw.tcss450.tcss450_group4.R.id.nav_host_fragment;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_add_friend_to_new_chat;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_base_url;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_chats;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_create_chat;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_getIndividalChat;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_messaging_base;
import static edu.uw.tcss450.tcss450_group4.R.string.ep_messaging_getAll;
import static edu.uw.tcss450.tcss450_group4.R.string.keys_json_messaging_success;


/**
 * A simple {@link Fragment} subclass.
 * This class displays the full profile of a connection or user.
 */
public class ViewConnectionFragment extends Fragment implements View.OnClickListener{
    /**
     * Instance field for variables to be used throught the class.
     */

    private String mJwToken;
    private int mMemberId;
    private ConnectionItem mConnectionItem;
    private int mVerified;
    private String mChatId;
    private String mChatName;


    /**
     * Public contructor.
     */
    public ViewConnectionFragment() {
        // Required empty public constructor
    }


    /**
     * onStart method to initialize variables and the image. Also checks the verification
     * to properly display each item in the fragment.
     */
    @Override
    public void onStart(){
        super.onStart();
        if (getArguments() != null) {
            mJwToken = getArguments().getString("jwt");
            mMemberId = getArguments().getInt("memberid");
            mConnectionItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));
            mVerified = mConnectionItem.getVerified();
            ImageView img = getActivity().findViewById(R.id.profileImageFull);
            String cleanImage = mConnectionItem.getContactImage().replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
            byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img.setImageBitmap(decodedByte);


            ((TextView) getActivity().findViewById(R.id.fullName))
                    .setText("Name: " + mConnectionItem.getFirstName() + " " + mConnectionItem.getLastName());
            ((TextView) getActivity().findViewById(R.id.fullUsername))
                    .setText("Username : " + mConnectionItem.getContactUserName());
            Log.e("verified", String.valueOf(mVerified));
        }
        if(mVerified == 1) {
            //set verified check
            ImageView img = getActivity().findViewById(R.id.verifiedImage);
            (getActivity().findViewById(R.id.sentViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.confirmViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.addViewImage))
                    .setVisibility(View.GONE);
        }
        else if(mVerified == 2){
            //set sent request image
            ImageView img = getActivity().findViewById(R.id.sentImage);

            (getActivity().findViewById(R.id.verifiedImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.confirmViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.addViewImage))
                    .setVisibility(View.GONE);

            (getActivity().findViewById(R.id.fullChat))
                    .setVisibility(View.GONE);
        }
        else if(mVerified == 3){
            //set received image

            //set verified and sent not visible.
            (getActivity().findViewById(R.id.verifiedImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.sentViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.addViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.fullChat))
                    .setVisibility(View.GONE);
        }
        else {
            (getActivity().findViewById(R.id.verifiedImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.sentViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.confirmViewImage))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.fullDelete))
                    .setVisibility(View.GONE);
            (getActivity().findViewById(R.id.fullChat))
                    .setVisibility(View.GONE);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_connection, container, false);

        }

    /**
     * This method puts an on click to each clickable item in the fragment.
     * @param view the View
     * @param savedInstanceState the Saved instance state.
     */
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button_chat = (Button) view.findViewById(R.id.fullChat);
        button_chat.setOnClickListener(this::onClick);

        Button button_delete = (Button) view.findViewById(R.id.fullDelete);
        button_delete.setOnClickListener(this::onClick);

        ImageView sent_image = view.findViewById(R.id.sentViewImage);
        sent_image.setOnClickListener(this::onClick);

        ImageView confirm_image = view.findViewById(R.id.confirmViewImage);
        confirm_image.setOnClickListener(this::onClick);

        ImageView add_image = view.findViewById(R.id.addViewImage);
        add_image.setOnClickListener(this::onClick);
    }


    /**
     * Method that handles each button clicked
     * @param v the view.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.addViewImage:
                addImage();
                break;

            case R.id.fullDelete:
                showDeleteDialogButtonClicked();
                break;
                
            case R.id.sentViewImage:
                sentImage();
                break;
                
            case R.id.confirmViewImage:
                confirmImage();
                break;

            case R.id.fullChat:
                checkIndividualChat();
                break;
        }

    }

    /**
     * Webservice call to get the chats.
     */
    private void checkIndividualChat(){
        Uri uriCheckChat = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_chats))
                .appendPath(getString(ep_getIndividalChat))
                .build();
        try {
            JSONObject msgBody = new JSONObject();
            msgBody.put("memberIdOne", mMemberId);
            msgBody.put("memberIdTwo", mConnectionItem.getContactId());
            Log.e("MEMBERS", msgBody.toString());
            new SendPostAsyncTask.Builder(uriCheckChat.toString(), msgBody)
                    .onCancelled(error -> Log.e("CHECK INDIVIDUAL CHAT ERROR", error))
                    .onPostExecute(this::createOrNot)
                    .addHeaderField("authorization", mJwToken)
                    .build().execute();
        }
        catch (JSONException e){
            Log.wtf("chatName", "Error creating JSON: " + e.getMessage());
        }
    }

    /**
     * This method is to check if we should create a new chat or go to existing
     * @param result json response from web service.
     */
    private void createOrNot(final String result) {
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean(getString(R.string.keys_json_success));
            if (success) {
                mChatId = resultJSON.getString("chatid");
                displayChat();

            } else if (resultJSON.has("chatname")) {
                mChatName = resultJSON.getString("chatname");
                createNewChat();
                addFriendToNewChat();
                getNewestChatId();
//                if (editText_ChatName.getText().length() != 0) {
//                    InputMethodManager inputManager = (InputMethodManager)
//                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//                }
                MyCreateChatRecyclerViewAdapter.getFriendIDList().clear();
            }
        } catch (JSONException e) {
            Log.wtf("JSON_PARSE_ERROR", "Error creating JSON: " + e.getMessage());
        }
    }

    /**
     * Web service call to display the chat.
     */
    private void displayChat(){

//        mChatId = chatId;
        JSONObject msgBody = new JSONObject();
        try {
            msgBody.put("chatId", mChatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri uriChats = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_messaging_base))
                .appendPath(getString(ep_messaging_getAll))
                .build();
        new SendPostAsyncTask.Builder(uriChats.toString(), msgBody)
                .onPostExecute(this::handleMessageGetOnPostExecute)
                .addHeaderField("authorization", mJwToken)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();

//        final Bundle args = new Bundle();
//        args.putSerializable(getString(R.string.chat_object), chat);
//        args.putString("email", mEmail);
//        args.putString("jwt", mJwToken);
//        args.putSerializable("List", mMessageList);
        //Navigation.findNavController(getView()).navigate(R.id.action_nav_chat_list_to_nav_view_chat, args);
    }

    /**
     * Handling the messages on post execute
     * @param result the json response from the server.
     */
    private void handleMessageGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean(getString(keys_json_messaging_success))) {
                JSONArray data = root.getJSONArray("messages");
//                if (response.has(getString(R.string.keys_json_chats_data))) {
//                    JSONArray data = response.getJSONArray(getString(R.string.keys_json_chats_data));
                Message[] messages = new Message[data.length()];
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonChatLists = data.getJSONObject(i);

                    messages[i] = (new Message.Builder(jsonChatLists.getString("username"),
                            jsonChatLists.getInt("memberid"),
                            jsonChatLists.getString("message"),
                            convertTimeStampToDate(jsonChatLists.getString("timestamp")),
                            jsonChatLists.getString("profileuri"))
                            .build());
                }
//                mMessageList = new ArrayList<Message>(Arrays.asList(messages));
                MobileNavigationDirections.ActionGlobalNavViewChat directions;
                directions = ViewChatFragmentDirections.actionGlobalNavViewChat(messages);
//                directions.setEmail(mEmail);
                directions.setMemberId(mMemberId);
                directions.setJwt(mJwToken);
                directions.setChatId(mChatId);
                Navigation.findNavController(getActivity(), nav_host_fragment).navigate(directions);

            } else {
                Log.e("ERROR!", "No response");
            }
            getActivity().findViewById(layout_homeActivity_wait).setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
            getActivity().findViewById(layout_homeActivity_wait).setVisibility(View.GONE);
            Log.e("ERROR!", e.getMessage());
        }
    }

    /**
     * Handle errors in the async task.
     * @param result
     */
    private void handleErrorsInTask(final String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Method to do the timestamp correctly.
     * @param timestamp the timestamp.
     * @return the correct timestamp result.
     */
    private String convertTimeStampToDate(String timestamp) {
        Date date = new Date();
        String result = "";
        //Date showTime = new Date();
        //Date showDate = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        //DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        try {
            date = format.parse(timestamp);
            result = timeFormat.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Web service call to make a new chat.
     */
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


            msgBody.put("chatName", mChatName);

            new SendPostAsyncTask.Builder(uriCreateChat.toString(), msgBody)
                    .onCancelled(error -> Log.e("CREATE CHAT FRAG", error))
                    .addHeaderField("authorization", mJwToken)
                    .build().execute();
        } catch (JSONException e){
            Log.wtf("chatName", "Error creating JSON: " + e.getMessage());
        }
    }

    /**
     * Web service call to add a friend to a new chat.
     */
    private void addFriendToNewChat() {
        Uri uriCreateChat = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_chats))
                .appendPath(getString(ep_add_friend_to_new_chat))
                .build();
        try {
                JSONObject msgBody = new JSONObject();
                msgBody.put("contactID", mMemberId);
                new SendPostAsyncTask.Builder(uriCreateChat.toString(), msgBody)
                        .onCancelled(error -> Log.e("ADD FRIEND TO NEW CHAT FRAG", error))
                        .addHeaderField("authorization", mJwToken)
                        .build().execute();
            msgBody = new JSONObject();
            msgBody.put("contactID", mConnectionItem.getContactId());
            new SendPostAsyncTask.Builder(uriCreateChat.toString(), msgBody)
                    .onCancelled(error -> Log.e("ADD FRIEND TO NEW CHAT FRAG", error))
                    .addHeaderField("authorization", mJwToken)
                    .build().execute();
                Log.wtf("Message", "created successful: " + msgBody.get("contactID"));
        } catch (JSONException e){
            Log.wtf("chatName", "Error creating JSON: " + e.getMessage());
        }

    }

    /**
     * Web service call to get the newest chat id's.
     */
    private void getNewestChatId() {
        Uri uriGetNewestChatId = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(ep_base_url))
                .appendPath(getString(ep_chats))
                .appendPath(getString(R.string.ep_chats_get_newest_chatId))
                .build();
        new GetAsyncTask.Builder(uriGetNewestChatId.toString())
                .onPostExecute(this::handleGetNewestChatIdOnPost)
                .onCancelled(error -> Log.e("GET NEWEST CHAT ID TO VIEW CHAT FRAG", error))
                .addHeaderField("authorization", mJwToken)
                .build().execute();
    }

    /**
     * This method handles what to do after the webservice call for the id.
     * @param result json response from server.
     */
    private void handleGetNewestChatIdOnPost(final String result) {
        try {
            JSONObject resultJSON = new JSONObject(result);
            boolean success = resultJSON.getBoolean("success");
            if (success) {
                mChatId = resultJSON.getString("chatid");
                Message[] message = new Message[0];
                MobileNavigationDirections.ActionGlobalNavViewChat directions;
                directions = ViewChatFragmentDirections.actionGlobalNavViewChat(message);
                directions.setJwt(mJwToken);
                directions.setChatId(mChatId);
                directions.setMemberId(mMemberId);
                Navigation.findNavController(getActivity(), nav_host_fragment).navigate(directions);
            }
        } catch (JSONException e) {
            Log.wtf("JSON_PARSE_ERROR", "Error creating JSON: " + e.getMessage());
        }
    }

    /**
     * Webservice call to add someone.
     */
    private void addImage() {
        Uri uriSearch = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_add))
                .build();
        JSONObject msgBody = new JSONObject();
        try{
            msgBody.put("memberIdUser", mMemberId);
            msgBody.put("memberIdOther", mConnectionItem.getContactId());
        } catch (JSONException e) {
            Log.wtf("username", "Error creating JSON: " + e.getMessage());

        }
        new SendPostAsyncTask.Builder(uriSearch.toString(), msgBody)
                .onPostExecute(this::handleAddOnPostExecute)
                .onCancelled(error -> Log.e("CONNECTION FRAG", error))
                .addHeaderField("authorization", mJwToken)  //add the JWT as header
                .build().execute();
    }

    /**
     * Handles the add on post execution
     * @param result the json response from the server.
     */
    private void handleAddOnPostExecute(String result) {
        //parse JSON
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
                    showAddSuccessDialogButtonClicked();

                } else {
                    showAddUnsuccessDialogButtonClicked();

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Method to handle the dialogue for a sent image.
     */
    private void sentImage() {
        showSentDialogButtonClicked();
    }

    /**
     * Method to handle the dialogue for a confirm image.
     */
    private void confirmImage() {
        showConfirmDialogButtonClicked();
    }

    /**
     * Method that shows the add dialogue on success.
     */
    private void showAddSuccessDialogButtonClicked() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Friend");
        builder.setMessage("Added!");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

            /**
             * Method to go back to the main connection gui.
             * @param result the result.
             */
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
                                    getString(R.string.keys_json_connection_username)),
                                    jsonConnection.getString(getString(R.string.keys_json_connection_image)));
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

            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Method that shows the add dialogue on unsuccessful.
     */
    private void showAddUnsuccessDialogButtonClicked() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Friend");
        builder.setMessage("Already Added!");

        builder.setNegativeButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Method that shows the confirm dialogue on success.
     */
    private void showConfirmDialogButtonClicked() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Request");
        builder.setMessage("Are you sure you want to confirm this request?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do something like...
                confirmConnection();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Method that shows the delete dialogue on success.
     */
    public void showDeleteDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove Connection");
        builder.setMessage("Are you sure you want to remove this connection?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do something like...
                removeConnection();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method that shows the sent dialogue on success.
     */
    public void showSentDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sent Request");
        builder.setMessage("You sent a request to this user");
        builder.setNegativeButton("OK", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Webservice call for confirming a request.
     */
    private void confirmConnection() {
        Uri uriConnection = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_connection))
                .appendPath(getString(R.string.ep_confirm))
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

    /**
     * Webservice call to remove a connection.
     */
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


    /**
     * Method that handles a remove on post execute.
     * @param result the json response from the web service.
     */
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



    /**
     * Method that handles a connection on post execute.
     * @param result the json response from the web service.
     */
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
                            getString(R.string.keys_json_connection_username)),
                            jsonConnection.getString(getString(R.string.keys_json_connection_image)));
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

    }

}
