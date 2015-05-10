package my.awesom.app.mycompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import my.awesom.app.mycompanion.adapters.FutureEventAdapter;

/**
 * Created by nitin on 5/7/15.
 */
public class PastReminders extends Fragment {
    private static PastReminders instance;
    String temp[] = {"A", "B", "C", "A", "B", "C", "A", "B", "C"};
    String temps[] = {"Hello", "Hey", "Hi", "How are you"};
    private ListView list;

    public static PastReminders getInstance() {
        instance = new PastReminders();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("MYLIST", "PR : Create View");
        View layout = inflater.inflate(R.layout.fragment_reminders_list, null);
        //list = (ListView) layout.findViewById(R.id.fragmentList);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("MYLIST", "PR : Activity Created");
        list.setAdapter(new FutureEventAdapter(getActivity(), temp));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Database.getPastEvents();
        Log.i("MYLIST", "PR : I am on Resume");
        list.setAdapter(new FutureEventAdapter(getActivity(), temps));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("MYLIST", "PR : I am on start");
    }


}
