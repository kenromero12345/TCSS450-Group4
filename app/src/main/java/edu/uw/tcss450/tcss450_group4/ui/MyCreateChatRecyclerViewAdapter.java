package edu.uw.tcss450.tcss450_group4.ui;

import androidx.navigation.Navigation;
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
 * This class uses to display all the added friends of the user so they will be displayed in CreateChatFragment
 * and user can select whoever they want to chat with.
 *
 * Created by Chinh Le on 11/1/2019.
 *
 * @author Chinh Le
 * @version Nov 1 2019
 */
public class MyCreateChatRecyclerViewAdapter extends RecyclerView.Adapter<MyCreateChatRecyclerViewAdapter.ViewHolder> {

    private final List<ConnectionItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private static final ArrayList<Integer> mFriendIDList = new ArrayList<>();

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


    public static ArrayList<Integer> getFriendIDList() {
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
