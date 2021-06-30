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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import in.weareindian.investorapp.R;
import in.weareindian.investorapp.model.Category;
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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> list;
    String currentDate;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_home_post, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final String id, ImageUrl, Blog, time;
        currentDate = Datetime();
        holder.rlPostContent.setVisibility(View.VISIBLE);
        holder.rlBlogContent.setVisibility(View.GONE);

        ImageUrl = list.get(position).getImages();
        time = list.get(position).getTime();
        holder.tvPostTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));
        Glide.with(holder.ivHomePost.getContext()).load(ImageUrl).into(holder.ivHomePost);

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

                            Toast.makeText(context, "Image Saved in Pictures/QuikNews", Toast.LENGTH_LONG).show();

                        } catch (FileNotFoundException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (IOException e2) {
                            Toast.makeText(context, e2.getMessage(), Toast.LENGTH_LONG).show();
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
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView ivHomePost;
        TextView tvSharePost, tvSavePost, tvPostTime;

        RelativeLayout rlBlogContent, rlPostContent;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHomePost = itemView.findViewById(R.id.ivHomePost);
            tvSharePost = itemView.findViewById(R.id.tvSharePost);
            tvSavePost = itemView.findViewById(R.id.tvSavePost);
            rlPostContent = itemView.findViewById(R.id.rlPostContent);
            rlBlogContent = itemView.findViewById(R.id.rlBlogContent);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
        }
    }
}
