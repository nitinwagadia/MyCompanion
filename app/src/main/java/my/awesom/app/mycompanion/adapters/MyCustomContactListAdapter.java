package my.awesom.app.mycompanion.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import my.awesom.app.mycompanion.R;
import my.awesom.app.mycompanion.models.MyContacts;

/**
 * Created by nitin on 4/16/15.
 */
public class MyCustomContactListAdapter extends BaseAdapter{
    private Context context;
    private static final int UNCHECKED=0;
    private static final int CHECKED=1;
    private LayoutInflater inflater;
    private List<MyContacts> data;
    //LinearLayout cardView;
    public static int checked[];
    private CheckBox checkBox;

    public MyCustomContactListAdapter(Context context, List<MyContacts> data) {
        this.context=context;
        this.data=data;
        checked=new int[data.size()];
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
            MyContacts current=data.get(position);
           convertView=inflater.inflate(R.layout.contact_card,null);
        /*(convertView.findViewById(R.id.card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Hey",Toast.LENGTH_SHORT).show();
                v.getLayoutParams().height=300;
                TranslateAnimation translateAnimation=new TranslateAnimation(v.getX(),v.getX(),v.getY()-10,10);
                translateAnimation.setDuration(500);
                translateAnimation.setFillAfter(true);
                v.setAnimation(translateAnimation);

                v.requestLayout();

            }
        });*/

        ((TextView) convertView.findViewById(R.id.contactName)).setText(current.getName());
        ((TextView) convertView.findViewById(R.id.phoneNumber)).setText(current.getPhoneNumber());
           checkBox= (CheckBox) convertView.findViewById(R.id.checkBox);
           checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(!isChecked)
                   {
                           checked[position]=UNCHECKED;

                   }
                   else
                   {

                           checked[position]=CHECKED;
                   }
                   Log.i("Check","of "+position+" is" +checked[position]);
               }
           });

        return convertView;
    }

}
