package my.awesom.app.mycompanion.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

import my.awesom.app.mycompanion.Database;
import my.awesom.app.mycompanion.DialogTest;


public class ServiceDialog extends IntentService {
    AlarmManager alarmManager;
    boolean flag = true;
    private String message;
    private String requestCode;

    public ServiceDialog() {
        super("ServiceDialog");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Intent i = new Intent(ServiceDialog.this, DialogTest.class);
            if (!intent.getExtras().isEmpty()) {
                Bundle b = intent.getExtras();
                requestCode = b.get("requestcode").toString();

            }

            try {
                message = new GetMessage().execute(requestCode).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            i.putExtra("message", message);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            new MakeEventPast().execute(requestCode);

        }
    }

    private class MakeEventPast extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int eventId = Integer.parseInt(params[0]);
            Database.makeEventPast(eventId);
            return null;
        }
    }

    private class GetMessage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            return Database.getMessage(params[0]);
        }
    }
}



