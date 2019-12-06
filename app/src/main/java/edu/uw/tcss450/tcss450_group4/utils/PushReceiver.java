package edu.uw.tcss450.tcss450_group4.utils;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import edu.uw.tcss450.tcss450_group4.R;
import edu.uw.tcss450.tcss450_group4.MainActivity;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";

    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {

        // the following variables are used to store the information sent from Pushy
        // In the WS, you define what gets sent. You can change it there to suit your
        // needs
        // Then here on the Android side, decide what to do with the message you got

        // for the lab, the WS is only sending chat messages so the type will always be
        // msg
        // for your project, the WS need to send different types of push messages.
        // perform so logic/routing based on the "type"
        // feel free to change the key or type of values. You could use numbers like
        // HTTP: 404 etc
        String typeOfMessage = intent.getStringExtra("type");

        // The WS sent us the name of the sender

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            // app is in the foreground so send the message to the active Activities
            // Log.d("PUSHY", "Message received in foreground: " + messageText);
            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            if (typeOfMessage.equals("msg")) {
                String messageText = intent.getStringExtra("message");
                String chatId = intent.getStringExtra("chatid");
                int senderId = intent.getIntExtra("senderid", -1);
                String profileUri = intent.getStringExtra("profileuri");
                String sender = intent.getStringExtra("sender");

                // create an Intent to broadcast a message to other parts of the app.
                i.putExtra("SENDER", sender);
                i.putExtra("MESSAGE", messageText);
                i.putExtra("MESSAGE", messageText);
                i.putExtra("CHATID", chatId);
                i.putExtra("SENDERID", senderId);
                i.putExtra("PROFILEURI", profileUri);
                i.putExtras(intent.getExtras());
            } else if (typeOfMessage.equals("request")) {
                i.putExtra("REQUEST", "You got a connection request");
                i.putExtras(intent.getExtras());
            }

            context.sendBroadcast(i);

        } else {
            // app is in the background so create and post a notification
            // Log.d("PUSHY", "Message received in background: " + messageText);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            if (typeOfMessage.equals("msg")) {
                String sender = intent.getStringExtra("sender");

                String messageText = intent.getStringExtra("message");
                // research more on notifications the how to display them
                // https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true).setSmallIcon(R.drawable.ic_chat_gold_24dp)
                        .setContentTitle("Message from: " + sender).setContentText(messageText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running
                // Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            } else if (typeOfMessage.equals("request")) {
                String username = intent.getStringExtra("username");
                // String memberId = intent.getStringExtra("memberid");

                String profileUri = intent.getStringExtra("profileuri");

                String cleanImage = profileUri.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,",
                        "");
                byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                // research more on notifications the how to display them
                // https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true).setSmallIcon(R.drawable.ic_connections_gold_24dp)
                        .setContentTitle("Request from: " + username)
                        // .setContentText(messageText)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running
                // Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }

        }

    }
}
