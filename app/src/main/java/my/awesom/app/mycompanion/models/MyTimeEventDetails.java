package my.awesom.app.mycompanion.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 4/23/15.
 */

public class MyTimeEventDetails {
    private String message;
    private ArrayList<MyContacts> data;
    private String time;
    private int eventId;
    private int isPast;
    private int typeOfEvent;


    public MyTimeEventDetails(String message, ArrayList<MyContacts> data, String time, int eventId, int isPast, int typeOfEvent) {
        this.message = message;
        this.data = data;
        this.time = time;
        this.eventId = eventId;
        this.isPast = isPast;
        this.typeOfEvent = typeOfEvent;
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

}
