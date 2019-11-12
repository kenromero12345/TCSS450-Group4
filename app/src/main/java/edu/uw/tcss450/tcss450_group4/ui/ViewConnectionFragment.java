package edu.uw.tcss450.tcss450_group4.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewConnectionFragment extends Fragment {


    public ViewConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart(){
        super.onStart();
        if (getArguments() != null) {
//            BlogPost blogPost = (BlogPost) getArguments().getSerializable(getString(R.string.blog_key));
            ConnectionItem connectionItem = (ConnectionItem)
                    getArguments().get(getString(R.string.keys_connection_view));

            ((TextView) getActivity().findViewById(R.id.fullName)).setText(connectionItem.getFirstName());
            ((TextView) getActivity().findViewById(R.id.fullID)).setText(connectionItem.getContactId());
            ((TextView) getActivity().findViewById(R.id.fullUsername)).setText(connectionItem.getContactUserName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_connection, container, false);
    }

}
