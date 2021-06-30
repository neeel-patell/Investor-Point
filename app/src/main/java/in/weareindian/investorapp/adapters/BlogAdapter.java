package in.weareindian.investorapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.weareindian.investorapp.OpenBlogWebActivity;
import in.weareindian.investorapp.R;
import in.weareindian.investorapp.model.Blog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder>{
    Context context;
    ArrayList<Blog> blogList;
    String currentDate;

    public BlogAdapter(Context context, ArrayList<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_home_post, parent, false);
        return new BlogViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        holder.rlPostContent.setVisibility(View.GONE);
        holder.rlBlogContent.setVisibility(View.VISIBLE);
        final String id, imageUrl, subject, description, time;
        id = blogList.get(position).getId();
        imageUrl = blogList.get(position).getImages();
        subject = blogList.get(position).getSubject();
        description = blogList.get(position).getDescription();
        time = blogList.get(position).getTime();


        Glide.with(holder.ivHomePost.getContext()).load(imageUrl).into(holder.ivHomePost);
        holder.tvBlogSubject.setText(subject);


        currentDate = Datetime();
        holder.tvBlogTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));

        holder.tvReadBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OpenBlogWebActivity.class);
                intent.putExtra("htmlCode", description);
                intent.putExtra("id", id);
                context.startActivity(intent);
                //finish();

                //String url = "https://lampros.tech/";
                /*String url = imageUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);*/

                /*ViewBlogFragment viewBlogFragment = new ViewBlogFragment();

                Bundle bundle=new Bundle();
                bundle.putString("blogId", id);

                viewBlogFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, viewBlogFragment)
                        .addToBackStack(null)
                        .commit();*/
            }
        });
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
        return blogList.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHomePost;
        RelativeLayout rlBlogContent, rlPostContent;
        TextView tvBlogSubject, tvReadBlog, tvBlogTime;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHomePost = itemView.findViewById(R.id.ivHomePost);
            rlPostContent = itemView.findViewById(R.id.rlPostContent);
            rlBlogContent = itemView.findViewById(R.id.rlBlogContent);
            tvBlogSubject = itemView.findViewById(R.id.tvBlogSubject);
            tvReadBlog = itemView.findViewById(R.id.tvReadBlog);
            tvBlogTime = itemView.findViewById(R.id.tvBlogTime);

        }
    }
}
