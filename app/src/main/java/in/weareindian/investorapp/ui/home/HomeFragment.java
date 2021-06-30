package in.weareindian.investorapp.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import in.weareindian.investorapp.adapters.HomeNewsAdapter;
import in.weareindian.investorapp.api.ApiConstants;
import in.weareindian.investorapp.model.Home;
import in.weareindian.investorapp.model.MySingleton;
import in.weareindian.investorapp.utils.CommonUtils;
import in.weareindian.investorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    View root;
    RecyclerView rvHomePost;
    LinearLayoutManager manager;
    ProgressBar progressBar;
    ArrayList<Home> homeList;
    HomeNewsAdapter adapter;
    Boolean isScrolling = false  ;
    int currentItems, totalItems, scrollOutItems;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout llNoData, llNoInternet;
    RelativeLayout rlMain;
    Button btnRetry;
    Context context;

    int limit = 0;
    String id, imageUrl, blog, time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();

        Init();
        GetDataFirstTime();
        grantUriPermission();

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
                    Toast.makeText(getContext(), "No Internet Conection", Toast.LENGTH_SHORT).show();
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

        rvHomePost.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    private void getMoreData() {
        progressBar.setVisibility(View.VISIBLE);
        limit = limit +20;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_LINKS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for(int i = 0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        id = object.getString("id");
                                        imageUrl = object.getString("images");
                                        blog = object.getString("blog");
                                        time = object.getString("time");

                                        Home temp = new Home(id, imageUrl, blog, time);
                                        homeList.add(temp);
                                    }

                                    adapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
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
                MySingleton.getInstance(getContext()).addtoRequestQueue(request);

            }
        }, 1000);
    }

    private void GetDataFirstTime() {
            limit = 0;
        homeList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_LINKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for(int i = 0; i<jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                id = object.getString("id");
                                imageUrl = object.getString("images");
                                blog = object.getString("blog");
                                time = object.getString("time");

                                Home temp = new Home(id, imageUrl, blog, time);
                                homeList.add(temp);
                            }

                            if(homeList.size() == 0)
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }

                            adapter = new HomeNewsAdapter(getContext(), homeList);
                            rvHomePost.setAdapter(adapter);
                            rvHomePost.setLayoutManager(manager);
                            progressBar.setVisibility(View.GONE);

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
        MySingleton.getInstance(getContext()).addtoRequestQueue(request);
    }

    private void Init() {
        rvHomePost = root.findViewById(R.id.rvHomePost);
        manager = new LinearLayoutManager(getContext());
        progressBar = root.findViewById(R.id.progressBar);
        homeList = new ArrayList<>();
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);

        llNoData = root.findViewById(R.id.llNoData);
        llNoInternet = root.findViewById(R.id.llNoInternet);
        rlMain = root.findViewById(R.id.rlMain);
        btnRetry = root.findViewById(R.id.btnRetry);
    }

    private void grantUriPermission() {


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        }


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

        }


    }
}