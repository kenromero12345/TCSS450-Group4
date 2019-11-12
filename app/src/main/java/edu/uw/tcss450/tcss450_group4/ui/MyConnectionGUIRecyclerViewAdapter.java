package edu.uw.tcss450.tcss450_group4.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.ui.ConnectionGUIFragment.OnListFragmentInteractionListener;


import java.util.List;

///**
// * {@link RecyclerView.Adapter} that can display a {@link connection} and makes a call to the
// * specified {@link OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
public class MyConnectionGUIRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionGUIRecyclerViewAdapter.ViewHolder> {

    private final List<ConnectionItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyConnectionGUIRecyclerViewAdapter(List<ConnectionItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connectiongui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.contactid.setText("ID: " + mValues.get(position).getContactId());
        holder.name.setText("Name: " + mValues.get(position).getFirstName()
        + " " + mValues.get(position).getLastName());
//        holder.lastname.setText("Last Name: " + mValues.get(position).getLastName());
        holder.userName.setText("Username: " + mValues.get(position).getContactUserName());
//        holder.mNae.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() { return mValues.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView contactid;
        public final TextView lastname;
        public final TextView name;
        public final TextView userName;
        public ConnectionItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            contactid = (TextView) view.findViewById(R.id.connection_memberid);
            lastname = (TextView) view.findViewById(R.id.connection_lastname);
            name = (TextView) view.findViewById(R.id.connection_firstname);
            userName = (TextView) view.findViewById(R.id.connection_username);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
}
