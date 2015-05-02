package my.awesom.app.mycompanion.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

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
                message = b.get("1").toString();
                requestCode = b.get("requestcode").toString();

            }

            i.putExtra("message", message);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);


        }
    }
}



