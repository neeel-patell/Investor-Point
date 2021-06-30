package in.weareindian.investorapp.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import in.weareindian.investorapp.R;
import in.weareindian.investorapp.model.Home;
import in.weareindian.investorapp.utils.CommonUtils;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeNewsAdapter extends RecyclerView.Adapter<HomeNewsAdapter.HomeNewsViewHolder>{

    Context context;
    ArrayList<Home> homeList;
    String currentDate;
    String blogSubject, blogDescription;

    public HomeNewsAdapter(Context context, ArrayList<Home> homeList) {
        this.context = context;
        this.homeList = homeList;
    }

    @NonNull
    @Override
    public HomeNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_home_post, parent, false);
        return new HomeNewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeNewsViewHolder holder, int position) {
        final String id, ImageUrl, blog, time;
        final HomeNewsViewHolder hld = holder;
        currentDate = Datetime();

//        Uncomment below code for blogs

//        id = homeList.get(position).getId();
        ImageUrl = homeList.get(position).getImages();
//        blog = homeList.get(position).getBlog();
        time = homeList.get(position).getTime();
        Glide.with(holder.ivHomePost.getContext()).load(ImageUrl).into(holder.ivHomePost);
        holder.progressBarImage.setVisibility(View.GONE);

//        if(blog.equals(Integer.toString(0)))
//        {
//            holder.rlPostContent.setVisibility(View.GONE);
//            holder.rlBlogContent.setVisibility(View.VISIBLE);
//            holder.tvBlogTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));
//
//            StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_BLOG_DETAILS,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                                for(int count = 0; count<jsonArray.length(); count++)
//                                {
//                                    JSONObject object = jsonArray.getJSONObject(count);
//                                    blogSubject = object.getString("subject");
//                                    blogDescription = object.getString("description");
//
//                                }
//                                hld.tvBlogSubject.setText(blogSubject);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("image", id);
//                    return params;
//                }
//            };
//            MySingleton.getInstance(context).addtoRequestQueue(request);
//
//            holder.tvReadBlog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(context, OpenBlogWebActivity.class);
//                    intent.putExtra("htmlCode", blogDescription);
//                    intent.putExtra("id", id);
//                    context.startActivity(intent);
//                }
//            });
//        }else {
//            holder.rlPostContent.setVisibility(View.VISIBLE);
//            holder.rlBlogContent.setVisibility(View.GONE);
//            holder.tvPostTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));
//        }

//        Remove this code for because it's already added in else above
        holder.rlPostContent.setVisibility(View.VISIBLE);
        holder.rlBlogContent.setVisibility(View.GONE);
        holder.tvPostTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));

        holder.tvSharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = ImageUrl;
                String shareSub = "Image Url ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        holder.tvSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                
                //checkStoragePermission();


                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

                    return;
                }

                Picasso.get().load(ImageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            File mydie = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"QuikNews".toString());
                            if (!mydie.exists()) {
                                mydie.mkdirs();
                            }

                            File myFile = new File(mydie, new Date().toString() + ".jpg");
                            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();

                            CommonUtils.refreshGallery(context, myFile);

                            Toast.makeText(context, "Image Saved in Pictures/QuikNews", Toast.LENGTH_SHORT).show();

                        } catch (FileNotFoundException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e2) {
                            Toast.makeText(context, e2.getMessage(), Toast.LENGTH_SHORT).show();
                            e2.printStackTrace();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

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
        return homeList.size();
    }

    public class HomeNewsViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHomePost;
        TextView tvSharePost, tvSavePost;

        RelativeLayout rlBlogContent, rlPostContent;
        TextView tvBlogSubject, tvReadBlog;
        TextView tvBlogTime, tvPostTime;

        ProgressBar progressBarImage;


        public HomeNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHomePost = itemView.findViewById(R.id.ivHomePost);
            tvSharePost = itemView.findViewById(R.id.tvSharePost);
            tvSavePost = itemView.findViewById(R.id.tvSavePost);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvBlogTime = itemView.findViewById(R.id.tvBlogTime);

            rlBlogContent = itemView.findViewById(R.id.rlBlogContent);
            rlPostContent = itemView.findViewById(R.id.rlPostContent);
            tvBlogSubject = itemView.findViewById(R.id.tvBlogSubject);
            tvReadBlog = itemView.findViewById(R.id.tvReadBlog);

            progressBarImage = itemView.findViewById(R.id.progressBarImage);

        }
    }
}
