package my.awesom.app.mycompanion.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MyEventDetails {
    private String address;
    private String message;
    private ArrayList<MyContacts> data;
    private String time;
    private int eventId;
    private int isPast;
    private int typeOfEvent;
    private int transition_type;

    public MyEventDetails(String message, ArrayList<MyContacts> data, String time, int eventId, int isPast, int typeOfEvent) {
        this.message = message;
        this.data = data;
        this.time = time;
        this.eventId = eventId;
        this.isPast = isPast;
        this.typeOfEvent = typeOfEvent;
    }

    public MyEventDetails(String message, ArrayList<MyContacts> data, String address, int eventId, int isPast, int typeOfEvent, int transition_type) {
        this.message = message;
        this.data = data;
        this.eventId = eventId;
        this.isPast = isPast;
        this.typeOfEvent = typeOfEvent;
        this.address = address;
        this.transition_type = transition_type;
        Log.i("MYLIST", "Address is : " + address);

    }


    public String getAddress() {
        return address;
    }

    public int getEventId() {
        return eventId;
    }

    public int getIsPast() {
        return isPast;
    }

    public String getMessage() {
        return message;
    }

    public List<MyContacts> getData() {
        return data;
    }

    public String getTime() {
        return time;
    }

    public int getTypeOfEvent() {
        return typeOfEvent;
    }

    public int getTransition_type() {
        return transition_type;
    }
}
