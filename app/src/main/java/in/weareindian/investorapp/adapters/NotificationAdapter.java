package in.weareindian.investorapp.adapters;

import android.content.Context;
import android.net.ParseException;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.weareindian.investorapp.R;
import in.weareindian.investorapp.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    ArrayList<Notification> notificationList;
    String currentDate;

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        currentDate = Datetime();
        String title, body, time;
        title = notificationList.get(position).getTitle();
        body = notificationList.get(position).getBody();
        time = notificationList.get(position).getTime();

        holder.tvTitleNotification.setText(title);
        holder.tvBodyNotification.setText(body);
        holder.tvTimeDateNotification.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));

    }


    public long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException | java.text.ParseException e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            //Log.d("Exception while parsing date. " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static String Datetime()
    {
        String formattedDate;
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitleNotification, tvBodyNotification, tvTimeDateNotification;
        ImageView ivRead;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleNotification = itemView.findViewById(R.id.tvTitleNotification);
            tvBodyNotification = itemView.findViewById(R.id.tvBodyNotification);
            tvTimeDateNotification = itemView.findViewById(R.id.tvTimeDateNotification);
            ivRead = itemView.findViewById(R.id.ivRead);
        }
    }
}
