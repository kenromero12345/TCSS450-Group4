package edu.uw.tcss450.tcss450_group4.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Chat;
import edu.uw.tcss450.tcss450_group4.ui.ChatFragment.OnListFragmentInteractionListener;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Chat} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyChatRecyclerViewAdapter extends RecyclerView.Adapter<MyChatRecyclerViewAdapter.ViewHolder> {

    private final List<Chat> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyChatRecyclerViewAdapter(List<Chat> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mChatName.setText(mValues.get(position).getChatName());
        holder.mMostRecentMessage.setText(mValues.get(position).getMostRecentMessage());
        holder.mTimeStamp.setText(mValues.get(position).getTimeStamp());
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
        Log.d("CHAT", "onBindViewHolder() position: " + position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mChatName;
        public final TextView mMostRecentMessage;
        public final TextView mTimeStamp;
        public Chat mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mChatName = view.findViewById(R.id.txt_ChatName);
            mMostRecentMessage = view.findViewById(R.id.txt_mostRecentMessage);
            mTimeStamp = view.findViewById(R.id.txt_TimeStamp);
        }

        @Override
        public String toString() {
            return super.toString() + mMostRecentMessage.getText() + " '" + mTimeStamp.getText() + "'";
        }
    }
}
