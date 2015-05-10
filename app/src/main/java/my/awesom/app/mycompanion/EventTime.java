package my.awesom.app.mycompanion;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import my.awesom.app.mycompanion.Animation.AnimationsClass;
import my.awesom.app.mycompanion.models.MyContacts;
import my.awesom.app.mycompanion.models.MyEventDetails;
import my.awesom.app.mycompanion.services.ServiceDialog;
import my.awesom.app.mycompanion.services.ServiceForSMS;


public class EventTime extends ActionBarActivity implements View.OnClickListener {

    public static final String NAMES = "contact_name";
    public static final String NUMBERS = "contact_number";
    View v = null;
    private TimePicker timePicker;
    private RadioGroup radioGroup;
    private LinearLayout contactLayout;
    private AlarmManager alarmManager;
    private Toolbar toolbar;
    private Button contactIntentButton, confirmButton;
    private EditText messageBox;
    private String names_list = "";

    private ArrayList<MyContacts> selectedContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_time);
        toolbar = (Toolbar) findViewById(R.id.toolbar_event_time);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        radioGroup = (RadioGroup) findViewById(R.id.smschoice);
        contactLayout = (LinearLayout) findViewById(R.id.contactLayout);
        contactIntentButton = (Button) findViewById(R.id.contactIntentButton);
        confirmButton = (Button) findViewById(R.id.confirm);
        messageBox = (EditText) findViewById(R.id.message);
        confirmButton.setOnClickListener(this);
        contactIntentButton.setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_sms)
                    contactLayout.setVisibility(View.VISIBLE);

                if (checkedId == R.id.no_sms)
                    contactLayout.setVisibility(View.GONE);
            }
        });

        if (savedInstanceState != null) {
            timePicker.setCurrentHour(Integer.parseInt(savedInstanceState.getString("hour")));
            timePicker.setCurrentMinute(Integer.parseInt(savedInstanceState.getString("minute")));
            messageBox.setText(savedInstanceState.getString("message"));
            selectedContacts = savedInstanceState.getParcelableArrayList("contacts");
            if (selectedContacts != null) {
                for (int i = 0; i < selectedContacts.size(); i++)
                    names_list += selectedContacts.get(i).getName() + "\n";

                ((TextView) findViewById(R.id.contacts_selected)).setText(names_list);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        names_list = "";
        if (requestCode == Constants.PICKED_CONTACTS && data != null) {
            selectedContacts = data.getParcelableArrayListExtra(Constants.CONTACT_DATA);

            if (selectedContacts != null) {
                for (int i = 0; i < selectedContacts.size(); i++)
                    names_list += selectedContacts.get(i).getName() + "\n";

                ((TextView) findViewById(R.id.contacts_selected)).setText(names_list);
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("contacts", selectedContacts);
        outState.putInt("radio", radioGroup.getCheckedRadioButtonId());
        outState.putString("message", messageBox.getText().toString());
        outState.putString("hour", timePicker.getCurrentHour().toString());
        outState.putString("minute", timePicker.getCurrentMinute().toString());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);

        if (id == R.id.accept)
            finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        AnimationsClass.animateButtonClick(v);
        if (id == R.id.contactIntentButton) {
            Intent contactPick = new Intent(EventTime.this, Contacts.class);
            startActivityForResult(contactPick, Constants.PICKED_CONTACTS);
        } else if (id == R.id.confirm) {
            int type;
            String message, time;

            if (radioGroup.getCheckedRadioButtonId() == R.id.yes_sms) {
                type = Constants.TYPE_TIME_SMS;
            } else {
                type = Constants.TYPE_TIME_NO_SMS;
            }

            message = messageBox.getText().toString();

            if (message.isEmpty()) {
                AnimationsClass.animateInvalidInput(findViewById(R.id.messageLayout));
                messageBox.setError("Cannot be Empty");
            } else {
                time = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString();
                if (type == Constants.TYPE_TIME_SMS) {
                    if (selectedContacts != null) {
                        int eventId = Constants.eventId++;
                        Log.i("MYLIST", "SMS eventId is" + eventId);
                        MyEventDetails details = new MyEventDetails(message, selectedContacts, time, eventId, Constants.IS_NOT_PAST, type);
                        new AddTimeToDataBase().execute(details);

                        Calendar calendar = setUpCalender();

                        Intent i = new Intent(EventTime.this, ServiceForSMS.class);
                        i.putExtra("1", "hello i am SMS TYPE EVENT");
                        i.putExtra("requestcode", eventId);
                        PendingIntent pendingIntent = PendingIntent.getService(EventTime.this, eventId, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        Toast.makeText(EventTime.this, "Alarm set", Toast.LENGTH_SHORT).show();
                    } else {
                        AnimationsClass.animateContactBox(contactIntentButton);
                    }

                } else {
                    int eventId = Constants.eventId++;
                    Log.i("MYLIST", "NO SMS eventId is" + eventId);

                    MyEventDetails details = new MyEventDetails(message, null, time, eventId, Constants.IS_NOT_PAST, type);
                    new AddTimeToDataBase().execute(details);


                    Calendar calendar = setUpCalender();

                    Intent i = new Intent(EventTime.this, ServiceDialog.class);
                    i.putExtra("1", "hello i am No SMS TYPE EVENT");
                    i.putExtra("requestcode", eventId);

                    PendingIntent pendingIntent = PendingIntent.getService(EventTime.this, eventId, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                }

            }


        } else {
            finish();
        }

    }

    private Calendar setUpCalender() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        if (System.currentTimeMillis() >= calendar.getTimeInMillis()) {

            calendar.add(Calendar.DATE, 1);

        }

        return calendar;

    }

    class AddTimeToDataBase extends AsyncTask<MyEventDetails, Void, Void> {
        @Override
        protected Void doInBackground(MyEventDetails... params) {
            Log.i("MYLIST", "Adding to Database");
            Database.addEvent(params[0]);
            return null;
        }
    }
}
