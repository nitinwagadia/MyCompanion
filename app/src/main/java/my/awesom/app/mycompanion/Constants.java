package my.awesom.app.mycompanion;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by nitin on 4/18/15.
 */
class  Constants {
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
    public static int eventId = 0;
    public static int IS_NOT_PAST=0;
    public static int IS_PAST=1;

    public static int count=0;


}
