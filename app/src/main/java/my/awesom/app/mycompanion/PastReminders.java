package my.awesom.app.mycompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.awesom.app.mycompanion.adapters.MyPastRemindersAdapter;

/**
 * Created by nitin on 5/7/15.
 */
public class PastReminders extends Fragment {
    private static PastReminders instance;
    String temp[] = {"A", "B", "C", "A", "B", "C", "A", "B", "C"};
    String temps[] = {"Hello", "Hey", "Hi", "How are you"};
    private RecyclerView recyclerview;

    public static PastReminders getInstance() {
        instance = new PastReminders();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("MYLIST", "PR : Create View");
        View layout = inflater.inflate(R.layout.fragment_reminders_list, null);
        recyclerview = (RecyclerView) layout.findViewById(R.id.recyclerview);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("MYLIST", "PR : Activity Created");
        recyclerview.setAdapter(new MyPastRemindersAdapter(getActivity()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Database.getPastEvents();
        Log.i("MYLIST", "PR : I am on Resume");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("MYLIST", "PR : I am on start");
    }


}
