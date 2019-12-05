package edu.uw.tcss450.tcss450_group4.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Location;
import edu.uw.tcss450.tcss450_group4.ui.LocationsFragment.OnListFragmentInteractionListener;

/**
 * adapter for the recycler view of locations
 */
public class MyLocationsRecyclerViewAdapter extends RecyclerView.Adapter<MyLocationsRecyclerViewAdapter.ViewHolder> {
    /**
     * list of locations that are saved
     */
    private final List<Location> mValues;
    /**
     * listener for the fragment interaction
     */
    private final OnListFragmentInteractionListener mListener;

    private Map<Integer, View> mViews;

    private boolean mFlag;
    public int mRow_index;
    /**
     * contructor for the recycler view of locations adapter
     * @param items the given locations
     * @param listener the given listener
     */
    public MyLocationsRecyclerViewAdapter(List<Location> items, OnListFragmentInteractionListener listener, boolean flag) {
        mValues = items;
        mListener = listener;
        mViews = new HashMap<>();
        mFlag = flag;
        mRow_index = -1;
    }

//    public void deleteColor(boolean tFlag) {
//        int color;
//        if (!tFlag) {
//            color = Color.parseColor("#85754d");
//        } else {
//            color = Color.parseColor("#4b2e83");
//        }
//
//        for (Map.Entry<Integer,View> entry : mViews.entrySet()) {
//            entry.getValue().setBackgroundColor(color);
//        }
//    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (!mFlag) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_locations_delete, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_locations, parent, false);
        }
        return new ViewHolder(view);
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNicknameView.setText(mValues.get(position).getName());
        mViews.put(position, holder.mView.findViewById(R.id.location_card));
//        holder.mLatLonView.setText(mValues.get(position).getLat() + " | " + mValues.get(position).getLon());
//        if (mValues.get(position).getZip() != -1) {
//            holder.mZipView.setText(mValues.get(position).getZip());
//        }
//        holder.mView.seto((v, event) -> {
//            Log.d("positionH", "" + position);
//            mRow_index=position;
//            return true;
//        });
        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
//                mRow_index=position;
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
//        int color = Color.TRANSPARENT;
//        Drawable background = holder.mCv.getBackground();
//        if (background instanceof ColorDrawable) {
//            color = ((ColorDrawable) background).getColor();
//        }
//        Log.d("row=", "" + (mRow_index==position));
//        if(mRow_index==position){
//            if (color == Color.parseColor("#4B2E83")) {
//                holder.mCv.setBackgroundColor(Color.parseColor("#554b2e83"));
//            } else if (color == Color.parseColor("#85754D")) {
//                holder.mCv.setBackgroundColor(Color.parseColor("#7785754D"));
//            }
//        }
//        else
//        {
//            if (color == Color.parseColor("#554b2e83")) {
//                holder.mCv.setBackgroundColor(Color.parseColor("#4B2E83"));
//            } else if (color == Color.parseColor("#7785754D")) {
//                holder.mCv.setBackgroundColor(Color.parseColor("#85754D"));
//            }
//        }

        Log.d("positionR", "" + position);
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     *
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNicknameView;
//        public final TextView mLatLonView;
//        public final TextView mZipView;
        public Location mItem;
        public int mRow_index = -1;
        public final CardView mCv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNicknameView = view.findViewById(R.id.weather_nickname);
            mCv = view.findViewById(R.id.location_card);
//            mLatLonView = (TextView) view.findViewById(R.id.weather_latLon);
//            mZipView = (TextView) view.findViewById(R.id.weather_zip);
        }

        /**
         *
         * @return
         */
        @Override
        public String toString() {
            return super.toString() + " '" + mNicknameView.getText() + "'";
        }
    }
}
