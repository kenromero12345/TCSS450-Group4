package edu.uw.tcss450.tcss450_group4.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.tcss450_group4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewChatFragment extends Fragment {


    public ViewChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_chat, container, false);
    }

}
