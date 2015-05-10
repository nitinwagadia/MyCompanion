package my.awesom.app.mycompanion.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import my.awesom.app.mycompanion.R;

/**
 * Created by nitin on 5/1/15.
 */
public class FutureEventAdapter extends BaseAdapter {

    int type[] = {0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0};
    Context context;
    ArrayList data;
    LayoutInflater inflater;
    String temp[];

    public FutureEventAdapter(Context context, String[] temp) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        data = new ArrayList();
        this.temp = temp;
    }

    @Override
    public int getCount() {

        return temp.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.card_view_reminders, null);
            holder.date = (TextView) convertView.findViewById(R.id.dateTime);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type[position] == 0) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_clock));
            holder.location.setVisibility(View.GONE);
            holder.date.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_compass));
            holder.location.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.GONE);
        }

        holder.message.setText(temp[position]);
        return convertView;
    }


    class GetFutureEvents extends AsyncTask<Void, Void, ArrayList> {


        @Override
        protected ArrayList doInBackground(Void... params) {
            return null;
        }
    }


    class ViewHolder {

        TextView date, location, message;
        ImageView imageView;


    }


}