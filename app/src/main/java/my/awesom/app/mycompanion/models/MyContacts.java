package my.awesom.app.mycompanion.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import my.awesom.app.mycompanion.EventTime;

/**
 * Created by nitin on 4/16/15.
 */
public class MyContacts implements Parcelable {
    private  String name;
    private String PhoneNumber;

    public MyContacts(String name, String phoneNumber) {
        this.name = name;
        this.PhoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }


    public String getPhoneNumber() {
        return PhoneNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle=new Bundle();
        bundle.putString(EventTime.NAMES,getName());
        bundle.putString(EventTime.NUMBERS,getPhoneNumber());
        dest.writeBundle(bundle);

    }

    public static final Creator<MyContacts> CREATOR= new Creator<MyContacts>() {
        @Override
        public MyContacts createFromParcel(Parcel source) {

            Bundle bundle=source.readBundle();


            return new MyContacts(bundle.getString(EventTime.NAMES),bundle.getString(EventTime.NUMBERS));
        }

        @Override
        public MyContacts[] newArray(int size) {
            return new MyContacts[size];
        }
    };
}
