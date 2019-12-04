package edu.uw.tcss450.tcss450_group4.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.ui.ConnectionRequestFragment.OnListFragmentInteractionListener;
//import edu.uw.tcss450.tcss450_group4.ui.dummy.DummyContent.DummyItem;

import java.util.List;

///**
// * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
// * specified {@link OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
public class MyConnectionRequestRecyclerViewAdapter extends RecyclerView.Adapter<MyConnectionRequestRecyclerViewAdapter.ViewHolder> {

    private final List<ConnectionItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyConnectionRequestRecyclerViewAdapter(List<ConnectionItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connectionrequest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mItem = mValues.get(position);
        String cleanImage = mValues.get(position).getContactImage().replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,","");
        byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.name.setText("Name: " + mValues.get(position).getFirstName()
                + " " + mValues.get(position).getLastName());
        holder.userName.setText("Username: " + mValues.get(position).getContactUserName());
        holder.profileimage.setImageBitmap(decodedByte);
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
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView lastName;
        public final TextView name;
        public final TextView userName;
        public ConnectionItem mItem;
        public final ImageView profileimage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            lastName = view.findViewById(R.id.request_lastname);
            name = view.findViewById(R.id.request_firstname);
            userName = view.findViewById(R.id.request_username);
            profileimage = view.findViewById(R.id.profileImageFull);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }
    }
}
