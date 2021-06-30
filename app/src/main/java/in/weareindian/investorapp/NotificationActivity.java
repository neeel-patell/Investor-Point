package in.weareindian.investorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.weareindian.investorapp.adapters.NotificationAdapter;
import in.weareindian.investorapp.api.ApiConstants;
import in.weareindian.investorapp.model.MySingleton;
import in.weareindian.investorapp.model.Notification;
import in.weareindian.investorapp.utils.CommonUtils;
import in.weareindian.investorapp.R;

public class NotificationActivity extends AppCompatActivity {

    LinearLayout customToolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rlMain;
    RecyclerView rvNotification;
    ProgressBar progressBar_notification;
    LinearLayout llNoData, llNoInternet, llBack;
    Button btnRetry;

    NotificationAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<Notification> notificationList;

    Boolean isScrolling = false  ;
    int currentItems, totalItems, scrollOutItems;

    int limit = 0;
    String title, body, time;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        context = getApplicationContext();

        Init();
        GetDataFirstTime();

        if (CommonUtils.isNetworkAvailable(context)) {
            llNoInternet.setVisibility(View.GONE);
            rlMain.setVisibility(View.VISIBLE);
        } else {
            llNoInternet.setVisibility(View.VISIBLE);
            rlMain.setVisibility(View.GONE);
        }

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isNetworkAvailable(context)) {
                    llNoInternet.setVisibility(View.GONE);
                    rlMain.setVisibility(View.VISIBLE);
                    GetDataFirstTime();
                }
                else
                {
                    Toast.makeText(NotificationActivity.this, "No Internet Conection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GetDataFirstTime();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });


        rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                //Toast.makeText(getContext(), totalItems + " " + currentItems + " " + scrollOutItems, Toast.LENGTH_SHORT).show();
                if(isScrolling && (currentItems + scrollOutItems == totalItems - 1 ))
                {
                    isScrolling = false;
                    getMoreData();
                }
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void getMoreData() {
        progressBar_notification.setVisibility(View.VISIBLE);
        limit = limit +20;

        new Handler().postDelayed(
                new Runnable() {
            @Override
            public void run() {

                StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_NOTIFICATIONS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for(int i = 0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        title = object.getString("title");
                                        body = object.getString("body");
                                        time = object.getString("time");

                                        Notification temp = new Notification(title, body, time);
                                        notificationList.add(temp);
                                    }

                                    adapter.notifyDataSetChanged();
                                    progressBar_notification.setVisibility(View.GONE);
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
                        params.put("limit", Integer.toString(limit));
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addtoRequestQueue(request);

            }
        }, 1000);
    }


    private void GetDataFirstTime() {
        limit = 0;
        notificationList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for(int i = 0; i<jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                title = object.getString("title");
                                body = object.getString("body");
                                time = object.getString("time");

                                Notification temp = new Notification(title, body, time);
                                notificationList.add(temp);
                            }

                            if(notificationList.size() == 0)
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }

                            adapter = new NotificationAdapter(getApplicationContext(), notificationList);
                            rvNotification.setAdapter(adapter);
                            rvNotification.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            progressBar_notification.setVisibility(View.GONE);

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
                params.put("limit", Integer.toString(limit));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addtoRequestQueue(request);
    }

    private void Init() {

        customToolbar = findViewById(R.id.customToolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rlMain = findViewById(R.id.rlMain);
        progressBar_notification = findViewById(R.id.progressBar_notification);
        llNoData = findViewById(R.id.llNoData);
        llNoInternet = findViewById(R.id.llNoInternet);
        btnRetry = findViewById(R.id.btnRetry);
        llBack = findViewById(R.id.llBack);

        rvNotification = findViewById(R.id.rvNotification);
        manager = new LinearLayoutManager(getApplicationContext());
        notificationList = new ArrayList<>();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}