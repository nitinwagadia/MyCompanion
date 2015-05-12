package my.awesom.app.mycompanion.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import my.awesom.app.mycompanion.Animation.AnimationsClass;
import my.awesom.app.mycompanion.Constants;
import my.awesom.app.mycompanion.R;
import my.awesom.app.mycompanion.adapters.MyScheduledRemindersAdapter.MyViewHolder;

/**
 * Created by nitin on 5/9/15.
 */
public class MyScheduledRemindersAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final String message = "Message : ";
    Context context;
    LayoutInflater inflater;


    public MyScheduledRemindersAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        Log.i("MYLIST", "onCreateViewHolder");
        View layout = inflater.inflate(R.layout.card_view_reminders, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("MYLIST", "I am in BIND View");
        if (isLocationEvent(position)) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_compass));
            holder.location.setVisibility(View.VISIBLE);
            holder.dateTime.setVisibility(View.GONE);

        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_clock));
            holder.location.setVisibility(View.GONE);
            holder.dateTime.setVisibility(View.VISIBLE);
        }

        holder.message.setText(message + Constants.scheduledEvents.get(position).getMessage());
        if (isLocationEvent(position)) {
            holder.location.setText("Location : " + Constants.scheduledEvents.get(position).getAddress());
        } else {
            holder.dateTime.setText("Reminder set for : " + Constants.scheduledEvents.get(position).getTime());
        }
        AnimationsClass.animateList(holder);
    }

    private boolean isLocationEvent(int position) {
        return ((Constants.scheduledEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_NO_SMS) || (Constants.scheduledEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_SMS));
    }

    @Override
    public int getItemCount() {
        return Constants.scheduledEvents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView message, dateTime, location;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.icon_image);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            message = (TextView) itemView.findViewById(R.id.message);
            location = (TextView) itemView.findViewById(R.id.location);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);
        }
    }
}
