package my.awesom.app.mycompanion;


import java.util.List;

import my.awesom.app.mycompanion.models.MyEventDetails;

public class Constants {
    public static final int PICK_CONTACT = 1;
    public static final int PICKED_CONTACTS = 1;
    public static final String NAMES = "CONTACT_NAMES";
    public static final String NUMBERS = "CONTACT_NUMBERS";
    public static final String CONTACT_DATA = "CONTACT_DETAIL";
    public static final int TYPE_TIME_NO_SMS = 10;
    public static final int TYPE_TIME_SMS = 11;
    public static final int TYPE_LOCATION_SMS = 20;
    public static final int TYPE_LOCATION_NO_SMS = 21;
    public static final String DATABASE_NAME = "EVENTS";
    public static final int DATABASE_VERSION = 1;
    public static final int IS_NOT_PAST = 0;
    public static final int IS_PAST = 1;
    public static final int TRANSITION_ENTER = 0;
    public static final int TRANSITION_LEAVE = 1;
    public static final int time = 24 * 60 * 60 * 1000;
    public static int eventId = 0;
    public static List<MyEventDetails> pastEvents;
    public static List<MyEventDetails> scheduledEvents;


}
