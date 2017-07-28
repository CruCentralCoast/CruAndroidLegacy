package com.crucentralcoast.app.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.views.MainActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "FcmListenerService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage message from sender.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = "";
        String message = "";

        if (data.size() > 0) {
//            if (data.containsKey("payload")) {
//                message = data.get("body");
//                title = data.get("title");
//                payload = CruApplication.gson.fromJson(data.get("payload"), CruUser.class);
//            }
//            else {
//                message = data.get("body");
//                title = data.get("title");
//            }
            title = data.get("title");
            message = data.get("body");
        }

//        if (payload == null)
//            NotificationProvider.putNotification(Observers.empty(), new Notification(message, ZonedDateTime.now()));
//        else
//            NotificationProvider.putNotification(Observers.empty(), new Notification(message, ZonedDateTime.now(), payload));
//
//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        }
//        else {
//            // normal downstream message.
//        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message, title);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String title) {
        if (!SharedPreferencesUtil.getNotificationEnabled())
            return;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_cru_logo_cropped)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setDefaults(android.app.Notification.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
