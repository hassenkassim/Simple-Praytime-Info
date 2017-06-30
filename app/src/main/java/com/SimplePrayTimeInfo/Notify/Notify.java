package com.SimplePrayTimeInfo.Notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;

import com.SimplePrayTimeInfo.MainActivity;
import com.SimplePrayTimeInfo.Message.Toasten;
import com.simplepraytimeinfofree.R;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HassenKassim on 04.01.15.
 */
public class Notify {
    private static Timer t;
    private static int TimeCounter = 0;
    private static Notification.Builder mbuilder;
    private static NotificationManager notificationManager;
    private static MessageHandler messageHandler;

    public static void notifying(Context context, String title, String message, int adhan, boolean vibration, int vibrationbeforetime, int vibrationsduration){
        messageHandler = new MessageHandler();







        if(vibration) vibrate(context, vibrationsduration);
        Toasten.toasten(context, "Vibrieren: " + vibrationbeforetime + "Sekunden");

        timercount(context, vibrationbeforetime);

        Toasten.toasten(context, "Vibrieren: fertig!");

        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        mbuilder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent);
        //Set Sound
        //Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/"
        //        + R.raw.adhan1);
        //mbuilder.setSound(uri);
        //Set Intent
        Intent contentIntent = new Intent(context, MainActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pintent = PendingIntent.getActivity(context, 0,
                contentIntent, 0);
        mbuilder.setContentIntent(pintent);


        // Build notification
        Notification noti = mbuilder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        //noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }

    private static void timercount(final Context ctx, int waittime){
        TimeCounter = waittime;

        TimerTask task = new TimerTask()
        {
            private final Handler mHandler    = new Handler(Looper.getMainLooper());

            @Override
            public void run()
            {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(TimeCounter==0) return;
                        else{
                            TimeCounter--;
                            /*
                            Bundle bundle = new Bundle();
                            bundle.putInt("current count", TimeCounter);
                            Message message = new Message();
                            messageHandler.sendMessage(message);*/
                            Toasten.toasten(ctx, "" + TimeCounter);

                            vibrate(ctx, 100);
                        }
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
        /*t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ctx.runOnUiThread(new Runnable() {
                    public void run() {
                        tvTimer.setText(String.valueOf(TimeCounter)); // you can set it to a textView to show it to the user to see the time passing while he is writing.
                        TimeCounter++;
                    }
                });

            }
        }, 1000, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
    }*/

    private static class MessageHandler extends Handler {
/*
mbuilder.setContentText("" + (TimeCounter--));
                            Notification notification = mbuilder.build();
                            notification.flags = Notification.FLAG_ONGOING_EVENT;
                            notificationManager.notify(R.string.app_name, notification);
 */
        @Override
        public void handleMessage(Message message){
            int time = message.getData().getInt("current count");
            /*mbuilder.setContentText("" + time);
            Notification notification = mbuilder.build();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notificationManager.notify(R.string.app_name, notification);*/
        }
    }


    private static void vibrate(Context ctx, int duration){
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(duration);
    }
}
