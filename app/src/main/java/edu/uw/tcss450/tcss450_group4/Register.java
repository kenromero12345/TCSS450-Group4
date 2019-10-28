package edu.uw.tcss450.tcss450_group4;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public Register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ((Button)v.findViewById(R.id.register_register)).setOnClickListener(view -> validRegister(v));
        return v;
    }

    /*
        Helper method that checks if registration information is all valid before proceeding.
     */
    private void validRegister(View v) {
        EditText firstName = v.findViewById(R.id.register_firstName);
        EditText lastName = v.findViewById(R.id.register_lastName);
        EditText nickname = v.findViewById(R.id.register_nickname);
        EditText email = v.findViewById(R.id.register_email);
        EditText password = v.findViewById(R.id.register_password);
        EditText rePassword = v.findViewById(R.id.register_retypePassword);


        String firstNameStr = firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String nicknameStr = nickname.getText().toString();
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
        if (nicknameStr.length() == 0) {
            nickname.setError("Enter a nickname");
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

        if (firstNameError && lastNameError && nicknameError && emailError && passwordError) {
            Log.d("REGISTER", "It's valid");
//            Credentials toSend = new Credentials.Builder(emailStr, passwordStr).build();
//            mListener.onRegisterSuccess(toSend);
        }
    }

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
//    public interface OnFragmentInteractionListener {
//    }
}
