package edu.uw.tcss450.tcss450_group4.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.model.Message;
public class MyMessageListRecyclerViewAdapter extends RecyclerView.Adapter<MyMessageListRecyclerViewAdapter.ViewHolder> {
    private List<Message> mValues;
    private Context mContext;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public MyMessageListRecyclerViewAdapter(List<Message> messageList, Context context ) {
        mContext = context;
        mValues = messageList;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.their_message, parent, false);
        return new ViewHolder(view);
//
//        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.my_message, parent, false);
//            return new SentMessageHolder(view);
//        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.their_message, parent, false);
//            return new ReceivedMessageHolder(view);
//        }
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mUsername.setText(mValues.get(position).getUsername());
        holder.mMessage.setText(mValues.get(position).getMessage());

//        Random rnd = new Random();
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        holder.mAvatar.setBackground(color);
//        Message message = (Message) mValues.get(position);

//        switch (holder.getItemViewType()) {
//            case VIEW_TYPE_MESSAGE_SENT:
//                ((SentMessageHolder) holder).bind(message);
//                break;
//            case VIEW_TYPE_MESSAGE_RECEIVED:
//                ((ReceivedMessageHolder) holder).bind(message);
//        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addMessage(String userName, String message, String timeStamp) {
        Message newMess = new Message.Builder(userName, message, timeStamp).build();
        mValues.add(newMess);
    }
    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) mValues.get(position);
//
//        if (message.getSender().getUserId().equals(SendBird.getCurrentUser().getUserId())) {
//            // If the current user is the sender of the message
//            return VIEW_TYPE_MESSAGE_SENT;
//        } else {
//            // If some other user sent the message
//            return VIEW_TYPE_MESSAGE_RECEIVED;
//        }
        return VIEW_TYPE_MESSAGE_SENT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMessage;
        public final TextView mUsername;
        ChatFragment chat = new ChatFragment();
        public Message mItem;
        public final ImageView mAvatar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            if (String.valueOf(mItem.getMemberId()).equals(chat.getmMemberId())) {
//                mUsername = null;
//                mMessage = view.findViewById(R.id.txt_myMessage);
//            } else {
                mUsername = view.findViewById(R.id.txt_friendUserName);
                mMessage = view.findViewById(R.id.txt_theirMessage);
                mAvatar = view.findViewById(R.id.avatar);
   //         }

        }

        @Override
        public String toString() {
            return super.toString() + mUsername.getText() + " '" + mMessage.getText() + "'";
        }
    }
}
