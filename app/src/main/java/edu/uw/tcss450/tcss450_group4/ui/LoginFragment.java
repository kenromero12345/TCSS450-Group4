package edu.uw.tcss450.tcss450_group4.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ChatMessageNotification;
import edu.uw.tcss450.tcss450_group4.model.Credentials;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;
import me.pushy.sdk.Pushy;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Credentials mCrendentials;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {

            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
            //Load the two login EditTexts with the credentials found in SharedPrefs
            EditText emailEdit = getActivity().findViewById(R.id.editText_email);
            emailEdit.setText(email);
            EditText passwordEdit = getActivity().findViewById(R.id.editText_password);
            passwordEdit.setText(password);

//            InputMethodManager inputManager = (InputMethodManager)
//                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);

            doLogin(new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build());

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        int uwPurple = Color.rgb(51, 0, 111);
//        Fragment loginFragment = (Fragment) getFragmentManager().findFragmentById(R.id.loginFragment);
//        loginFragment.getView().setBackgroundColor(Color.RED);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button_register = view.findViewById(R.id.button_register);
        button_register.setOnClickListener(this::onClick);

        Button button_login = view.findViewById(R.id.button_signin);
        button_login.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
//        InputMethodManager inputManager = (InputMethodManager)
//                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId()) {
            case R.id.button_signin:
                attemptLogin(v.findViewById(R.id.button_signin));
                break;
            case R.id.button_register:
                Log.d("DEBUG", "entered");
                Navigation.findNavController(getView())
                        .navigate(R.id.action_nav_login_to_nav_register);
                break;
        }
    }

    private void attemptLogin(final View theButton) {
        EditText editTxtEmail = getActivity().findViewById(R.id.editText_email);
        EditText editTxtPasword = getActivity().findViewById(R.id.editText_password);
        boolean hasError = false;
        if (editTxtEmail.getText().length() == 0) {
            hasError = true;
            editTxtEmail.setError("Field must not be empty.");
        } else if (editTxtEmail.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            editTxtEmail.setError("Field must contain a valid email address.");
        }
        if (editTxtPasword.getText().length() == 0) {
            hasError = true;
            editTxtPasword.setError("Field must not be empty.");
        }
        if (!hasError) {

            doLogin(new Credentials.Builder(
                    editTxtEmail.getText().toString(),
                    editTxtPasword.getText().toString())
                    .build());

//            Credentials credentials = new Credentials.Builder(editTxtEmail.getText().toString(),
//                    editTxtPasword.getText().toString())
//                    .build();
//            //build the web service URL
//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_login))
//                    .build();
//
//            //build the JSONObject
//            JSONObject msg = credentials.asJSONObject();
//
//            mCrendentials = credentials;
//
//            //instantiate and execute the AsyncTask.
//            new SendPostAsyncTask.Builder(uri.toString(), msg)
//                    .onPreExecute(this::handleLoginOnPre)
//                    .onPostExecute(this::handleLoginOnPost)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();
        }
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask (String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.VISIBLE);
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */

    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean(getString(R.string.keys_json_login_success));

            if (success) {

                LoginFragmentDirections.ActionNavLoginToNavHomeActivity homeActivity =
                        LoginFragmentDirections.actionNavLoginToNavHomeActivity(mCrendentials);
                homeActivity.setMemberId(resultsJSON.getInt(getString(R.string.keys_json_login_memberId)));
                homeActivity.setJwt(resultsJSON.getString(getString(R.string.keys_json_login_jwt)));
                homeActivity.setProfileuri(resultsJSON.getString(getString(R.string.keys_json_login_profileuri)));
                saveCredentials(mCrendentials);
                Navigation.findNavController(getView()).navigate(homeActivity);
                //Remove this Activity from the back stack. Do not allow back navigation to login
                getActivity().finish();
            } else {
                String error = resultsJSON.getString(getString(R.string.keys_json_error));
                Log.d("COW", error);
                if (error.equals("not verified")) {
                    LoginFragmentDirections.ActionNavLoginToNavVerify verifyFragment =
                            LoginFragmentDirections.actionNavLoginToNavVerify(mCrendentials);
                    verifyFragment.setJwt(resultsJSON.getString(getString(R.string.keys_json_login_jwt)));
                    Navigation.findNavController(getView()).navigate(verifyFragment);
                    //Remove this Activity from the back stack. Do not allow back navigation to login
//                    getActivity().finish();
                } else {
                    //Logiin was unsuccessful. Don't switch fragments and
                    //inform the user
                    ((TextView) getView().findViewById(R.id.editText_email))
                            .setError("Login Unsuccessful");
                }
            }
//            getActivity().findViewById(R.id.layout_login_wait)
//                    .setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result + System.lineSeparator()
                    + e.getMessage());
            getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.editText_email)).setError("Login Unsuccessful");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }

    private void doLogin(Credentials credentials) {
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .appendPath(getString(R.string.ep_pushy))
                .build();

//        build the JSONObject
//        JSONObject msg = credentials.asJSONObject();

        mCrendentials = credentials;

        new AttemptLoginTask().execute(uri.toString());
        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
//        new SendPostAsyncTask.Builder(uri.toString(), msg)
//                .onPreExecute(this::handleLoginOnPre)
//                .onPostExecute(this::handleLoginOnPost)
//                .onCancelled(this::handleErrorsInTask)
//                .build().execute();
    }

    class AttemptLoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            //get pushy token
            String deviceToken = "";

            try {
                // Assign a unique token to this device
                deviceToken = Pushy.register(getActivity().getApplicationContext());

                //subscribe to a topic (this is a Blocking call)
                Pushy.subscribe("all", getActivity().getApplicationContext());
            }
            catch (Exception exc) {

                cancel(true);
                // Return exc to onCancelled
                return exc.getMessage();
            }

            //feel free to remove later.
            Log.d("LOGIN", "Pushy Token: " + deviceToken);


            //attempt to log in: Send credentials AND pushy token to the web service
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;

            try {
                URL urlObject = new URL(urls[0]);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");


                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

                JSONObject message = mCrendentials.asJSONObject();
                message.put("token", deviceToken);
                wr.write(message.toString());
                wr.flush();
                wr.close();

                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while((s = buffer.readLine()) != null) {
                    response.append(s);
                }
                publishProgress();
            } catch (Exception e) {
                response = new StringBuilder("Unable to connect, Reason: "
                        + e.getMessage());
                cancel(true);
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return response.toString();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.GONE);
            Log.e("LOGIN_ERROR", "Error in Login Async Task: " + s);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                Log.d("JSON result",result);

                JSONObject resultsJSON = new JSONObject(result);
                boolean success = resultsJSON.getBoolean("success");
                Log.d("Member ID",String.valueOf(resultsJSON.getInt(getString(R.string.keys_json_login_memberId))));

                if (success) {
                    mCrendentials = new Credentials.Builder(
                            mCrendentials.getEmail(),
                            mCrendentials.getPassword())
                            .addFirstName(resultsJSON.getString(getString(R.string.keys_json_login_firstname)))
                            .addLastName(resultsJSON.getString(getString(R.string.keys_json_login_lastname)))
                            .addUsername(resultsJSON.getString(getString(R.string.keys_json_login_username)))
                            .build();

                    saveCredentials(mCrendentials);

                    //Login was successful. Switch to the SuccessFragment.
                    LoginFragmentDirections.ActionNavLoginToNavHomeActivity homeActivity =
                            LoginFragmentDirections.actionNavLoginToNavHomeActivity(mCrendentials);
                    homeActivity.setJwt(resultsJSON.getString(
                            getString(R.string.keys_json_login_jwt)));
                    homeActivity.setMemberId(resultsJSON.getInt(getString(R.string.keys_json_login_memberId)));
                    homeActivity.setProfileuri(resultsJSON.getString(getString(R.string.keys_json_login_profileuri)));

                    if (getArguments() != null) {
                        if (getArguments().containsKey("type")) {
                            if (getArguments().getString("type").equals("msg")) {
                                String msg = getArguments().getString("message");
                                String sender = getArguments().getString("sender");
                                String chatId = getArguments().getString("chatid");
                                ChatMessageNotification chat =
                                        new ChatMessageNotification.Builder(sender, msg, chatId).build();
                                homeActivity.setChatMessage(chat);
                            }
                        }
                    }

                    Navigation.findNavController(getView()).navigate(homeActivity);

                    getActivity().finish();
                    return;
                } else {
                    //Saving the token wrong. Don’t switch fragments and inform the user
                    ((TextView) getView().findViewById(R.id.editText_email))
                            .setError("Login Unsuccessful");
                }
            } catch (JSONException e) {
                //It appears that the web service didn’t return a JSON formatted String
                //or it didn’t have what we expected in it.
                Log.e("JSON_PARSE_ERROR",  result
                        + System.lineSeparator()
                        + e.getMessage());

                ((TextView) getView().findViewById(R.id.editText_email))
                        .setError("Login Unsuccessful");
            }
        }
    }

}
