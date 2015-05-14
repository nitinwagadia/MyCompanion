package my.awesom.app.mycompanion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import my.awesom.app.mycompanion.customviews.SlidingTabLayout;
import my.awesom.app.mycompanion.models.MyEventDetails;


public class MainActivity extends ActionBarActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        new GetAllEvents().execute();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        slidingTabLayout.setDistributeEvenly(true);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        slidingTabLayout.setViewPager(viewPager);
        (findViewById(R.id.add_time)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EventTime.class));
                finish();
            }
        });

        (findViewById(R.id.add_location)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EventLocation.class));
                finish();
            }
        });
        //AnimationsClass.animateToolbar(toolbar);

    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.i("MYLIST", "Main Activity on Stop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(ShowDialogForEvents s) {
        progressDialog = null;
        progressDialog = ProgressDialog.show(MainActivity.this, "Please Wait Loading...", "Retrieving Events!");
    }

    public void onEventMainThread(CloseDialogForEvents c) {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = null;
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        String items[] = {"Scheduled Reminders", "Past Reminders"};

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position == 0) {
                fragment = ScheduledReminders.getInstance();
            } else {
                fragment = PastReminders.getInstance();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items[position];

        }
    }

    private class GetAllEvents extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EventBus.getDefault().post(new ShowDialogForEvents());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0] + "% completed");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Constants.scheduledEvents = new ArrayList<MyEventDetails>();
            Constants.pastEvents = new ArrayList<MyEventDetails>();

            Database.getAllTimeEvents();
            setProgress(20);
            Database.getAllLocationEvents();
            setProgress(50);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new CloseDialogForEvents());

        }


    }

    class ShowDialogForEvents {
    }

    class CloseDialogForEvents {
    }
}
