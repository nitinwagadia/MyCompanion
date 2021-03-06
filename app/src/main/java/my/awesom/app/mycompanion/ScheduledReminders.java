package my.awesom.app.mycompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.awesom.app.mycompanion.adapters.MyScheduledRemindersAdapter;


public class ScheduledReminders extends Fragment {

    String temp[] = {"A", "B", "C", "A", "B", "C", "A", "B", "C"};
    String temps[] = {"Hello", "Hey", "Hi", "How are you"};
    RecyclerView recyclerview;

    public static ScheduledReminders getInstance() {

        ScheduledReminders instance = new ScheduledReminders();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout;
        if (Constants.scheduledEvents.isEmpty()) {
            layout = inflater.inflate(R.layout.remider_no_evnets, null);

        } else {
            layout = inflater.inflate(R.layout.fragment_reminders_list, container, false);
            recyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);
        }
        return layout;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Constants.scheduledEvents.isEmpty()) {
            recyclerview.setAdapter(new MyScheduledRemindersAdapter(getActivity()));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerview.setLayoutManager(layoutManager);
        }

    }


}
