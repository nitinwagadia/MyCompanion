package my.awesom.app.mycompanion;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import my.awesom.app.mycompanion.MyScheduledRemindersAdapter.MyViewHolder;

/**
 * Created by nitin on 5/9/15.
 */
public class MyScheduledRemindersAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    //int type[] = {0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0};


    public MyScheduledRemindersAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View layout = inflater.inflate(R.layout.card_view_reminders, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (Constants.scheduledEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_NO_SMS || Constants.scheduledEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_SMS) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_compass));
            holder.location.setVisibility(View.GONE);
            holder.dateTime.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_clock));
            holder.location.setVisibility(View.VISIBLE);
            holder.dateTime.setVisibility(View.GONE);
        }

        holder.message.setText(Constants.scheduledEvents.get(position).getMessage());
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
