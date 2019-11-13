package edu.uw.tcss450.tcss450_group4.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Location;
import edu.uw.tcss450.tcss450_group4.ui.LocationsFragment.OnListFragmentInteractionListener;

import java.util.List;

public class MyLocationsRecyclerViewAdapter extends RecyclerView.Adapter<MyLocationsRecyclerViewAdapter.ViewHolder> {

    private final List<Location> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyLocationsRecyclerViewAdapter(List<Location> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_locations, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNicknameView.setText(mValues.get(position).getName());
//        holder.mLatLonView.setText(mValues.get(position).getLat() + " | " + mValues.get(position).getLon());
//        if (mValues.get(position).getZip() != -1) {
//            holder.mZipView.setText(mValues.get(position).getZip());
//        }
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
        public final TextView mNicknameView;
//        public final TextView mLatLonView;
//        public final TextView mZipView;
        public Location mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNicknameView = (TextView) view.findViewById(R.id.weather_nickname);
//            mLatLonView = (TextView) view.findViewById(R.id.weather_latLon);
//            mZipView = (TextView) view.findViewById(R.id.weather_zip);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNicknameView.getText() + "'";
        }
    }
}
