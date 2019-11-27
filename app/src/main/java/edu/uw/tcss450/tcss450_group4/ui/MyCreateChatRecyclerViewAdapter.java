package edu.uw.tcss450.tcss450_group4.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.ConnectionItem;
import edu.uw.tcss450.tcss450_group4.ui.CreateChatFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ConnectionItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCreateChatRecyclerViewAdapter extends RecyclerView.Adapter<MyCreateChatRecyclerViewAdapter.ViewHolder> {

    private final List<ConnectionItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ArrayList<Integer> mFriendIDList;

    public MyCreateChatRecyclerViewAdapter(List<ConnectionItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_createchat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUserName.setText(mValues.get(position).getContactUserName());
        //holder.mContactId.setText(mValues.get(position).getContactId());
        mFriendIDList = new ArrayList<>();
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
       holder.mContactId.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                addMemberIdToList(mFriendIDList, Integer.valueOf(mValues.get(position).getContactId()));
                Log.e("Checkbox MemberID", mValues.get(position).getContactId());
            } else {
                removeMemberIdToList(mFriendIDList,Integer.valueOf(mValues.get(position).getContactId()));
            }
       });
       Bundle bundle = new Bundle();
       bundle.putIntegerArrayList("friend list", mFriendIDList);

       ChatFragment args = new ChatFragment();
       args.setArguments(bundle);
    }
    private void addMemberIdToList(ArrayList<Integer> list, int memberId) {
        list.add(memberId);
    }

    private void removeMemberIdToList(ArrayList<Integer> list, int memberId) {
        list.remove(new Integer(memberId));
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public ArrayList<Integer> getFriendIDList() {
        return mFriendIDList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mUserName;
        public final CheckBox mContactId;
        public ConnectionItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUserName = (TextView) view.findViewById(R.id.txt_userName);
            mContactId = (CheckBox) view.findViewById(R.id.checkbox_contactId);
        }
    }
}
