package edu.uw.tcss450.tcss450_group4.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Credentials;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;

/*
    Register page that allows users to create an account.
 */
public class RegisterFragment extends Fragment {

    private Credentials mCredentials;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        v.findViewById(R.id.register_register).setOnClickListener(view -> validRegister(v));
        v.findViewById(R.id.register_cancel).setOnClickListener(view ->
                Navigation.findNavController(getView())
                        .navigate(R.id.action_nav_register_to_nav_login));
        return v;
    }

    /*
        Helper method that checks if registration information is all valid before proceeding.
     */
    private void validRegister(View v) {
        EditText firstName = v.findViewById(R.id.register_firstName);
        EditText lastName = v.findViewById(R.id.register_lastName);
        EditText username = v.findViewById(R.id.register_username);
        EditText email = v.findViewById(R.id.register_email);
        EditText password = v.findViewById(R.id.register_password);
        EditText rePassword = v.findViewById(R.id.register_retypePassword);


        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String usernameStr = username.getText().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String rePasswordStr = rePassword.getText().toString();


        boolean firstNameError = false;
        boolean lastNameError = false;
        boolean nicknameError = false;
        boolean emailError = false;
        boolean passwordError = false;

        // Checks for valid first name
        if (firstNameStr.length() == 0) {
            firstName.setError("Enter first name");
        } else {
            firstNameError = true;
        }

        // Checks for valid last name
        if (lastNameStr.length() == 0) {
            lastName.setError("Enter last name");
        } else {
            lastNameError = true;
        }

        // Checks for valid nickname
        if (usernameStr.length() == 0) {
            username.setError("Enter a username");
        } else {
            nicknameError = true;
        }

        // TODO: Connect to server to check if email even exists
        // Checks if email is valid
        if (emailStr.length() == 0) {
            email.setError("Choose an email address");
        } else if (!emailStr.contains("@")){
            email.setError("Requires a valid email");
        } else {
            emailError = true;
        }

        // Checks if password is valid
        if (passwordStr.length() == 0) {
            password.setError("Enter a password");
        } else if (passwordStr.length() < 6) {
            password.setError("Use 6 characters or more for your password");
        } else if (rePasswordStr.length() == 0) {
            rePassword.setError("Confirm your password");
        } else if (!rePasswordStr.equals(passwordStr)) {
            rePassword.setError("Passwords do not match");
        } else {
            passwordError = true;
        }

        // TODO: Implement transition to verify page
        // If valid go to Verify page
        if (firstNameError && lastNameError && nicknameError && emailError && passwordError) {
            Credentials credentials = new Credentials.Builder(emailStr, passwordStr)
                    .addFirstName(firstNameStr)
                    .addLastName(lastNameStr)
                    .addUsername(usernameStr)
                    .build();
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_register))
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleRegisterOnPre)
                    .onPostExecute(this::handleRegisterOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();



        }
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleRegisterOnPre() {
        getActivity().findViewById(R.id.layout_register_wait).setVisibility(View.VISIBLE);
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_register_success));
            if (success) {
                RegisterFragmentDirections.ActionNavRegisterToNavVerify homeActivity =
                        RegisterFragmentDirections.actionNavRegisterToNavVerify(mCredentials);
                String jwt = resultsJSON.getString(getString(R.string.keys_json_login_jwt));
                homeActivity.setJwt(jwt);
                Navigation.findNavController(getView()).navigate(homeActivity);
                //Remove this Activity from the back stack. Do not allow back navigation to login
//                getActivity().finish();
//                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                String detail = resultsJSON.getJSONObject(getString(R.string.keys_json_register_error))
                        .getString(getString(R.string.keys_json_register_detail));
                if (detail.startsWith("Key (username)")) {
                    ((EditText) getView().findViewById(R.id.register_username))
                            .setError("Username already exists");
                } else {
                    ((EditText) getView().findViewById(R.id.register_email))
                            .setError("Email already is used");
                }
            }
            getActivity().findViewById(R.id.layout_register_wait)
                    .setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            getActivity().findViewById(R.id.layout_register_wait)
                    .setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.register_email))
                    .setError("Registration Unsuccessful");
        }
    }
}
