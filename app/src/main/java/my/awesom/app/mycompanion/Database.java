package my.awesom.app.mycompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import my.awesom.app.mycompanion.models.MyContacts;
import my.awesom.app.mycompanion.models.MyEventDetails;


public class Database extends SQLiteOpenHelper {

    private static final String GENERAL_TABLE_NAME = "general_details";
    private static final String PHONE_INFO_TABLE_NAME = "Phone_details";
    private static final String LOCATION_INFO_TABLE_NAME = "location_details";
    private static final String TIME_INFO_TABLE_NAME = "time_based_reminder_details";
    private static final String TIME_OF_REMINDER = "time_reminder";
    private static final String EVENT_ID = "event_id";
    private static final String TABLE_TIME_INFO = "create table if not exists " + TIME_INFO_TABLE_NAME + " (" + EVENT_ID + " integer," + TIME_OF_REMINDER + " varchar, foreign key(" + EVENT_ID + ") references " + GENERAL_TABLE_NAME + " (" + EVENT_ID + "));";
    private static final String EVENT_TYPE = "event_type";
    private static final String MESSAGE = "message";
    private static final String ISPAST = "isPast";
    private static final String TRANSITION_TYPE = "transition_type";
    private static final String TABLE_GENERAL_INFO = "create table if not exists " + GENERAL_TABLE_NAME + "(" + EVENT_ID + " INTEGER Primary Key ," + EVENT_TYPE + " INTEGER, " + ISPAST + " integer, " + MESSAGE + " Text );";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String TABLE_PHONE_NUMBER_INFO = "create table if not exists " + PHONE_INFO_TABLE_NAME + "(" + EVENT_ID + " integer," + PHONE_NUMBER + " integer(10), foreign key(" + EVENT_ID + ") references " + GENERAL_TABLE_NAME + "(" + EVENT_ID + "));";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String TABLE_LOCATION_INFO = "create table if not exists " + LOCATION_INFO_TABLE_NAME + " (" + EVENT_ID + " integer," + LATITUDE + " varchar(20) ," + LONGITUDE + " varchar(20), " + TRANSITION_TYPE + " integer, foreign key(" + EVENT_ID + ") references " + GENERAL_TABLE_NAME + " (" + EVENT_ID + "));";
    private static final String GET_PAST_TIME_EVENT = "select * from " + GENERAL_TABLE_NAME + " g inner join " + TIME_INFO_TABLE_NAME + " t on  t." + EVENT_ID + " =g." + EVENT_ID;
    private static final String GET_PAST_LOCATION_EVENT = "select * from " + GENERAL_TABLE_NAME + " g inner join " + LOCATION_INFO_TABLE_NAME + " t on  t." + EVENT_ID + " =g." + EVENT_ID;
    private static Database database;

    private Database(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    public static Database getDatabase() {
        if (database == null)
            database = new Database(MyApplication.getInstance());
        return database;

    }

    public static void addEvent(MyEventDetails details) {


        if (details.getTypeOfEvent() == Constants.TYPE_TIME_SMS || details.getTypeOfEvent() == Constants.TYPE_LOCATION_SMS) {

            AddEventData(details, details.getTypeOfEvent());

            List<MyContacts> temp = details.getData();
            for (int i = 0; i < temp.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(EVENT_ID, details.getEventId());
                contentValues.put(PHONE_NUMBER, temp.get(i).getPhoneNumber());
                getDatabase().getWritableDatabase().insert(PHONE_INFO_TABLE_NAME, null, contentValues);

            }

        } else {
            AddEventData(details, details.getTypeOfEvent());

        }

    }


    static void AddEventData(MyEventDetails details, int typeOfEvent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_ID, details.getEventId());
        contentValues.put(EVENT_TYPE, details.getTypeOfEvent());
        contentValues.put(ISPAST, details.getIsPast());
        contentValues.put(MESSAGE, details.getMessage());
        getDatabase().getWritableDatabase().insert(GENERAL_TABLE_NAME, null, contentValues);
        contentValues.clear();
        if (typeOfEvent == Constants.TYPE_TIME_SMS || typeOfEvent == Constants.TYPE_TIME_NO_SMS) {

            contentValues.put(EVENT_ID, details.getEventId());
            contentValues.put(TIME_OF_REMINDER, details.getTime());
            getDatabase().getWritableDatabase().insert(TIME_INFO_TABLE_NAME, null, contentValues);
        } else {
            contentValues.put(EVENT_ID, details.getEventId());
            contentValues.put(LATITUDE, details.getLatitude());
            contentValues.put(LONGITUDE, details.getLongitude());
            contentValues.put(TRANSITION_TYPE, details.getTransition_type());
            getDatabase().getWritableDatabase().insert(LOCATION_INFO_TABLE_NAME, null, contentValues);

        }
    }

    public static List getPhoneNumbers(String eventId) {
        List phoneNumbers = new ArrayList();
        Cursor cursor = getDatabase().getReadableDatabase().query(PHONE_INFO_TABLE_NAME, null, EVENT_ID + "=" + eventId, null, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        phoneNumbers.add(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
        while (cursor.moveToNext())
            phoneNumbers.add(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));

        cursor.close();

        cursor = getDatabase().getReadableDatabase().query(GENERAL_TABLE_NAME, null, EVENT_ID + "=" + eventId, null, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        phoneNumbers.add(cursor.getString(cursor.getColumnIndex(MESSAGE)));
        cursor.close();


        return phoneNumbers;
    }

    public static void makeEventPast(int eventId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ISPAST, Constants.IS_PAST);
        getDatabase().getWritableDatabase().update(GENERAL_TABLE_NAME, contentValues, EVENT_ID + "=" + eventId, null);
    }

    public static String getMessage(String eventId) {

        Cursor cursor = getDatabase().getReadableDatabase().query(GENERAL_TABLE_NAME, null, EVENT_ID + "=" + eventId, null, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(MESSAGE));
    }

    public static void getAllTimeEvents() {

        Cursor cursor = getDatabase().getReadableDatabase().rawQuery(GET_PAST_TIME_EVENT, null);

        while (cursor.moveToNext()) {
            int event_id = cursor.getInt(cursor.getColumnIndex(EVENT_ID));
            int event_type = cursor.getInt(cursor.getColumnIndex(EVENT_TYPE));
            int isPast = cursor.getInt(cursor.getColumnIndex(ISPAST));
            String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
            String time = cursor.getString(cursor.getColumnIndex(TIME_OF_REMINDER));
            if (isPast == Constants.IS_PAST) {
                Constants.pastEvents.add(new MyEventDetails(message, null, time, event_id, isPast, event_type));
            } else {
                Constants.scheduledEvents.add(new MyEventDetails(message, null, time, event_id, isPast, event_type));
            }
        }

    }


    public static void getAllLocationEvents() {

        Cursor cursor = getDatabase().getReadableDatabase().rawQuery(GET_PAST_LOCATION_EVENT, null);

        while (cursor.moveToNext()) {
            int event_id = cursor.getInt(cursor.getColumnIndex(EVENT_ID));
            int event_type = cursor.getInt(cursor.getColumnIndex(EVENT_TYPE));
            int isPast = cursor.getInt(cursor.getColumnIndex(ISPAST));
            String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
            String longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE));
            String latitude = cursor.getString(cursor.getColumnIndex(LATITUDE));
            int transition_type = cursor.getInt(cursor.getColumnIndex(TRANSITION_TYPE));

            if (isPast == Constants.IS_PAST) {
                Constants.pastEvents.add(new MyEventDetails(message, null, latitude, longitude, event_id, isPast, event_type, transition_type));
            } else {
                Constants.scheduledEvents.add(new MyEventDetails(message, null, latitude, longitude, event_id, isPast, event_type, transition_type));
            }
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_GENERAL_INFO);
        db.execSQL(TABLE_PHONE_NUMBER_INFO);
        db.execSQL(TABLE_LOCATION_INFO);
        db.execSQL(TABLE_TIME_INFO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
