package my.awesom.app.mycompanion.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

import my.awesom.app.mycompanion.Database;
import my.awesom.app.mycompanion.R;

public class ServiceForSMS extends IntentService {

    public ServiceForSMS() {
        super("ServiceForSMS");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (!intent.getExtras().isEmpty()) {

                Bundle b = intent.getExtras();
                String requestCode = b.get("requestcode").toString();
                Log.i("MYLIST", "ServiceFORSMS requestcode is " + requestCode);
                List data = null;
                try {
                    data = new RetrievePhoneNumbers().execute(requestCode).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                String message = String.valueOf(data.get(data.size() - 1));
                SmsManager smsManager = SmsManager.getDefault();
                for (int i = 0; i < data.size() - 1; i++) {

                    String number = String.valueOf(data.get(i));
                    smsManager.sendTextMessage(number, null, message, null, null);
                    Log.i("MYLIST", "Message " + message + " sent to " + number);
                }

                new MakeEventPast().execute(requestCode);

                Notification.Builder notificationBuilder = new Notification.Builder(this);
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
                notificationBuilder.setContentTitle(getString(R.string.notification_title));
                notificationBuilder.setContentText(getString(R.string.notification_content));
                notificationBuilder.setSmallIcon(R.drawable.sms_notification_icon);
                notificationBuilder.setTicker(getString(R.string.notification_ticker));

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(Integer.parseInt(requestCode), notificationBuilder.build());
                Log.i("MYLIST", "Notification done");
            }

        }
    }

    class RetrievePhoneNumbers extends AsyncTask<String, Void, List> {


        @Override
        protected List doInBackground(String... params) {


            return Database.getPhoneNumbers(params[0]);
        }
    }

    class MakeEventPast extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            int eventId = Integer.parseInt(params[0]);
            Database.makeEventPast(eventId);
            return null;
        }
    }
}
