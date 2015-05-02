package my.awesom.app.mycompanion;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by nitin on 5/1/15.
 */
public class FutureReminders extends Fragment {

    ListView list;
    String temp[] = {"A", "B", "C", "A", "B", "C", "A", "B", "C"};

    public static FutureReminders getInstance() {

        FutureReminders instance = new FutureReminders();
        Log.i("MYLIST", "GIVNG INSTANCE");
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_reminders_list, null);
        list = (ListView) layout.findViewById(R.id.fragmentList);
        return layout;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list.setAdapter(new FutureEventAdapter(getActivity()));
        //list.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, temp));
    }
}
