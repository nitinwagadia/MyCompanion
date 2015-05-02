package my.awesom.app.mycompanion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import my.awesom.app.mycompanion.adapters.MyCustomContactListAdapter;
import my.awesom.app.mycompanion.models.MyContacts;


public class Contacts extends ActionBarActivity {

    private Toolbar toolbar;
    private List<MyContacts> contacts;
    private ListView list;
    private ProgressDialog backgroundDialog;
    private MyCustomContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        EventBus.getDefault().register(this);
        contacts = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar_contacts);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        list = (ListView) findViewById(R.id.contact_list);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        new GetContacts().execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_okay) {
            String names[] = new String[MyCustomContactListAdapter.checked.length];
            String numbers[] = new String[MyCustomContactListAdapter.checked.length];
            int arr[] = MyCustomContactListAdapter.checked;
            ArrayList<MyContacts> data = new ArrayList<>();
            for (int i = 0; i < arr.length; i++)
                if (arr[i] == 1) {
                    MyContacts current = new MyContacts(contacts.get(i).getName(), contacts.get(i).getPhoneNumber());
                    data.add(current);

                }
            Intent i = getIntent();
            i.putParcelableArrayListExtra(Constants.CONTACT_DATA, data);
            setResult(Constants.PICKED_CONTACTS, i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onEventMainThread(CloseDialogForContacts c) {
        if (backgroundDialog != null)
            backgroundDialog.dismiss();
        backgroundDialog = null;
    }

    public void onEventMainThread(ShowDialogForContacts s) {
        backgroundDialog = null;
        backgroundDialog = ProgressDialog.show(Contacts.this, "Please Wait", "Retreiving Contacts!");
    }

    private class GetContacts extends AsyncTask<Void, Void, List<MyContacts>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EventBus.getDefault().post(new ShowDialogForContacts());

        }

        @Override
        protected List<MyContacts> doInBackground(Void... params) {
            String phoneNumber = null, name = null;
            Cursor contentCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC");


            if (contentCursor.getCount() > 0) {
                while (contentCursor.moveToNext()) {
                    String id = contentCursor.getString(contentCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String hasPhonesNumber = contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhonesNumber.equals("1")) {

                        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                        while (phone.moveToNext()) {
                            phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            MyContacts newContact = new MyContacts(name, phoneNumber);
                            contacts.add(newContact);
                        }
                        phone.close();

                    }
                }

            }

            return contacts;

        }

        @Override
        protected void onPostExecute(List<MyContacts> myContactses) {
            super.onPostExecute(myContactses);
            EventBus.getDefault().post(new CloseDialogForContacts());
            adapter = new MyCustomContactListAdapter(Contacts.this, contacts);
            list.setAdapter(adapter);


        }
    }
}


class ShowDialogForContacts {
    ;
}

class CloseDialogForContacts {
    ;
}
