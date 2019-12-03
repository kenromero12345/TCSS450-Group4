package edu.uw.tcss450.tcss450_group4.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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
import edu.uw.tcss450.tcss450_group4.model.Credentials;
import edu.uw.tcss450.tcss450_group4.utils.SendPostAsyncTask;


public class VerifyFragment extends Fragment {

    private Credentials mCredentials;

    private String mJwt = "";

    private int tries = 0;

    public VerifyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VerifyFragment.
     */
//    // TODO: Rename and change types and number of parameters
//    public static VerifyFragment newInstance(String param1, String param2) {
//        VerifyFragment fragment = new VerifyFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VerifyFragmentArgs args = VerifyFragmentArgs.fromBundle(getArguments());
        mCredentials = args.getCredentials();
        mJwt = args.getJwt();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verify, container, false);
        Button b = v.findViewById(R.id.verify_button);
        b.setOnClickListener(view -> validVerify(v));
        b = v.findViewById(R.id.verify_resend);
        b.setOnClickListener(view -> resendVerify());

        return v;
    }

    private void resendVerify() {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_verify))
                .appendPath(getString(R.string.ep_resend))
                .build();
        //build the JSONObject
        JSONObject msg = new JSONObject();
        try {
            msg.put("email", mCredentials.getEmail());
        } catch (JSONException e) {
            Log.e("JSON", "unexpected JSON exception", e);
        }
        //instantiate and execute the AsyncTask.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleResendOnPre)
                .onPostExecute(this::handleResendPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleResendPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_register_success));
            if (!success) {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                ((EditText) getView().findViewById(R.id.verify_textView))
                        .setError("Verification Unsuccessful");
            }
            getActivity().findViewById(R.id.verify_resendCode).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.verify_resend).setEnabled(true);
            getActivity().findViewById(R.id.verify_button).setEnabled(true);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            getActivity().findViewById(R.id.verify_resendCode).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.verify_resend).setEnabled(true);
            getActivity().findViewById(R.id.verify_button).setEnabled(true);
            ((TextView) getView().findViewById(R.id.verify_textView))
                    .setError("Unable to send code");
        }
    }

    private void handleResendOnPre() {
        getActivity().findViewById(R.id.verify_resendCode).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.verify_resend).setEnabled(false);
        getActivity().findViewById(R.id.verify_button).setEnabled(false);
    }

    /*
        Helper method that checks if registration information is all valid before proceeding.
     */
    private void validVerify(View v) {

        EditText verifyCode = v.findViewById(R.id.verify_textView);
        String verifyCodeStr = verifyCode.getText().toString();


        // Checks for valid first name
        if (verifyCodeStr.length() == 0) {
            verifyCode.setError("Enter verify code");
        } else {

//            mJwt = args.getJwt();
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_verify))
                    .appendPath(getString(R.string.ep_confirm))
                    .build();
            //build the JSONObject
            JSONObject msg = new JSONObject();
            try {
                msg.put("email", mCredentials.getEmail());
                msg.put("verifycode", verifyCodeStr);
            } catch (JSONException e) {
                Log.e("JSON", "unexpected JSON exception", e);
            }
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleVerifyOnPre)
                    .onPostExecute(this::handleVerifyOnPost)
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
    // TODO: make overlay on verify page
    private void handleVerifyOnPre() {
        getActivity().findViewById(R.id.layout_verify_wait).setVisibility(View.VISIBLE);
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleVerifyOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_register_success));
            if (success) {
                VerifyFragmentDirections.ActionNavVerifyToNavHomeActivity homeActivity =
                        VerifyFragmentDirections.actionNavVerifyToNavHomeActivity(mCredentials);
                homeActivity.setMemberId(resultsJSON.getInt(getString(R.string.keys_json_verify_memberId)));
                homeActivity.setJwt(mJwt);
                homeActivity.setProfileuri(resultsJSON.getString(getString(R.string.keys_json_verify_profileuri)));
                saveCredentials(mCredentials);
                Navigation.findNavController(getView()).navigate(homeActivity);
                //Remove this Activity from the back stack. Do not allow back navigation to login
                getActivity().finish();
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                if (tries >= 2) {
                    tries = 0;
                    ((EditText) getView().findViewById(R.id.verify_textView))
                            .setError("Verification unsuccessful. You have made 3 attempts.\n" +
                                        "A new code has been sent to your email");
                    resendVerify();
                } else {
                    ((EditText) getView().findViewById(R.id.verify_textView))
                            .setError("Verification Unsuccessful");
                    tries++;
                }
            }
            getActivity().findViewById(R.id.layout_verify_wait)
                    .setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            getActivity().findViewById(R.id.layout_verify_wait)
                    .setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.verify_textView))
                    .setError("Verification Unsuccessful");
        }
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

//    public void verifyFunction(View v){
//            String verify = ((EditText) v.findViewById(R.id.verify_textView)).getText().toString();
//            if (verify.matches("")){
//                ((EditText) v.findViewById(R.id.verify_textView)).setError("Invalid Verify");
//            } else {
//                Navigation.findNavController(getView())
//                        .navigate(R.id.action_nav_verify_to_nav_homeActivity);
//                mListener.onVerifySuccess();
//                getActivity().finish();
//            }
//
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onVerifySuccess();
//    }
}
