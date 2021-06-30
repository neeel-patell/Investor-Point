package in.weareindian.investorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.weareindian.investorapp.api.ApiConstants;
import in.weareindian.investorapp.model.MySingleton;
import in.weareindian.investorapp.R;

public class OpenSinglePostActivity extends AppCompatActivity {

    ImageView ivHomePost;
    TextView tvSharePost, tvSavePost;

    RelativeLayout rlBlogContent, rlPostContent;
    TextView tvBlogSubject, tvReadBlog;
    TextView tvBlogTime, tvPostTime;

    ProgressBar progressBarImage;


    String id, ImageUrl, blog, time;

    String currentDate;
    String blogSubject, blogDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_single_post);

        Init();

        //GetData();

        SetData();


    }

    private void SetData() {

        Glide.with(ivHomePost.getContext()).load(ImageUrl).into(ivHomePost);
        progressBarImage.setVisibility(View.GONE);

        if(blog.equals(Integer.toString(1)))
        {
            rlPostContent.setVisibility(View.GONE);
            rlBlogContent.setVisibility(View.VISIBLE);
            tvBlogTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));

            StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_BLOG_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for(int count = 0; count<jsonArray.length(); count++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(count);
                                    blogSubject = object.getString("subject");
                                    blogDescription = object.getString("description");

                                }
                                tvBlogSubject.setText(blogSubject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", id);
                    return params;
                }
            };
            MySingleton.getInstance(getApplicationContext()).addtoRequestQueue(request);

            tvReadBlog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), OpenBlogWebActivity.class);
                    intent.putExtra("htmlCode", blogDescription);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }else {
            rlPostContent.setVisibility(View.VISIBLE);
            rlBlogContent.setVisibility(View.GONE);
            tvPostTime.setText(DateUtils.getRelativeTimeSpanString(getDateInMillis(time), getDateInMillis(currentDate) ,DateUtils.MINUTE_IN_MILLIS));
        }

        tvSharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = ImageUrl;
                String shareSub = "Image Url ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });

        tvSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //checkStoragePermission();


                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(mydie, new Date().toString() + ".jpg"));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Toast.makeText(getApplicationContext(), "Image Saved in Pictures/QuikNews", Toast.LENGTH_SHORT).show();

                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (IOException e2) {
                            Toast.makeText(getApplicationContext(), e2.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void GetData() {
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_POST_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for(int count = 0; count<jsonArray.length(); count++)
                            {
                                JSONObject object = jsonArray.getJSONObject(count);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addtoRequestQueue(request);
    }


    private void Init() {

        ivHomePost = findViewById(R.id.ivHomePost);
        tvSharePost = findViewById(R.id.tvSharePost);
        tvSavePost = findViewById(R.id.tvSavePost);
        tvPostTime = findViewById(R.id.tvPostTime);
        tvBlogTime = findViewById(R.id.tvBlogTime);

        rlBlogContent = findViewById(R.id.rlBlogContent);
        rlPostContent = findViewById(R.id.rlPostContent);
        tvBlogSubject = findViewById(R.id.tvBlogSubject);
        tvReadBlog = findViewById(R.id.tvReadBlog);

        progressBarImage = findViewById(R.id.progressBarImage);

        currentDate = Datetime();

        id = getIntent().getStringExtra("id");
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

    public long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException | java.text.ParseException e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            //Log.d("Exception while parsing date. " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}