package com.example.mail;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessaging extends FirebaseMessagingService {
    private  static final String NOTIFICATION_CHANNEL_ID = "MY_NOTIFICATION_CHANNEL_ID";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public void onMessageReceived(@NonNull RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String notificationType = remoteMessage.getData().get("notificationType");

        if (notificationType.equals("NewOrder")) {
            String buyerUid = remoteMessage.getData().get("buyerUid");
            String sellerUid = remoteMessage.getData().get("sellerlUid");
            String orderId = remoteMessage.getData().get("orderId");
            String notificationTitle = remoteMessage.getData().get("notificationTitle");
            String notificationDescription = remoteMessage.getData().get("notificationDescription");

            if (firebaseUser != null && firebaseAuth.getUid().equals(sellerUid)) {
                showNotification(orderId,sellerUid,buyerUid,notificationTitle,notificationDescription,notificationType);
            }
        }
            if(notificationType.equals("OrderStatusChanged"))
            {
                String buyerUid = remoteMessage.getData().get("buyerUid");
                String sellerUid = remoteMessage.getData().get("sellerlUid");
                String orderId = remoteMessage.getData().get("orderId");
                String notificationTitle = remoteMessage.getData().get("notificationTitle");
                String notificationDescription = remoteMessage.getData().get("notificationMessage");

                if(firebaseUser != null && firebaseAuth.getUid().equals(buyerUid)){
                    showNotification(orderId,sellerUid,buyerUid,notificationTitle,notificationDescription,notificationType);
            }
        }

    }
    private void  showNotification(String orderId , String sellerUid , String buyerUid,String notificationTitle ,String notificationDescription , String notificationType)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationID = new Random().nextInt(3000);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)

        {
            setupNotificationChanne(notificationManager);

        }
        Intent intent = null;

        if(notificationType.equals("NewOrder"))
        {
            intent =  new Intent(this,OrderDetailsWholesellerActivity.class);
            intent.putExtra("orderId",orderId);
            intent.putExtra("orderBy",buyerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


        }
        else if(notificationType.equals("OrderStatusChanged"))
        {
            intent =  new Intent(this,OrderdetailsRetailerActivity.class);
            intent.putExtra("orderId",orderId);
            intent.putExtra("orderTo",sellerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_baseline_shopping_basket_24);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_baseline_shopping_basket_24)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setSound(notificationSoundUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

            notificationManager.notify(notificationID,notificationBuilder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChanne(NotificationManager notificationManager) {

        CharSequence channelName = "Some Sample Text";
        String channelDescription = "Channel Description here";

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(channelDescription);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        if (notificationManager!= null)
        {
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

}
