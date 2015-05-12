package my.awesom.app.mycompanion.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import my.awesom.app.mycompanion.Animation.AnimationsClass;
import my.awesom.app.mycompanion.Constants;
import my.awesom.app.mycompanion.R;
import my.awesom.app.mycompanion.adapters.MyPastRemindersAdapter.ViewHolderPastEvents;

/**
 * Created by nitin on 5/12/15.
 */
public class MyPastRemindersAdapter extends RecyclerView.Adapter<ViewHolderPastEvents> {
    private final Context context;
    private final LayoutInflater inflater;
    private final String message = "Message : ";

    public MyPastRemindersAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderPastEvents onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.card_view_reminders, parent, false);
        ViewHolderPastEvents viewHolderPastEvents = new ViewHolderPastEvents(layout);
        return viewHolderPastEvents;
    }

    @Override
    public void onBindViewHolder(ViewHolderPastEvents holder, int position) {

        if (Constants.pastEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_NO_SMS || Constants.pastEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_SMS) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_compass));
            holder.location.setVisibility(View.VISIBLE);
            holder.dateTime.setVisibility(View.GONE);

        } else {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_clock));
            holder.location.setVisibility(View.GONE);
            holder.dateTime.setVisibility(View.VISIBLE);
        }

        holder.message.setText(message + Constants.pastEvents.get(position).getMessage());
        if (isLocationEvent(position)) {
            holder.location.setText("Latitude : " + Constants.pastEvents.get(position).getAddress());
        } else {
            holder.dateTime.setText("Reminder set for : " + Constants.pastEvents.get(position).getTime());
        }
        AnimationsClass.animateList(holder);


    }

    private boolean isLocationEvent(int position) {
        return ((Constants.pastEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_NO_SMS) || (Constants.pastEvents.get(position).getTypeOfEvent() == Constants.TYPE_LOCATION_SMS));
    }

    @Override
    public int getItemCount() {
        return Constants.pastEvents.size();
    }

    public class ViewHolderPastEvents extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView message, dateTime, location;
        ImageView imageView;

        public ViewHolderPastEvents(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.icon_image);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            message = (TextView) itemView.findViewById(R.id.message);
            location = (TextView) itemView.findViewById(R.id.location);
            dateTime = (TextView) itemView.findViewById(R.id.dateTime);
        }
    }
}
