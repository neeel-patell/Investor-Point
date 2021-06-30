package in.weareindian.investorapp.ui.defense;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import in.weareindian.investorapp.adapters.CategoryAdapter;
import in.weareindian.investorapp.api.ApiConstants;
import in.weareindian.investorapp.model.MySingleton;
import in.weareindian.investorapp.utils.CommonUtils;
import in.weareindian.investorapp.R;
import in.weareindian.investorapp.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefenseFragment extends Fragment {
    // This is IPOs Fragment
    private DefenseViewModel mViewModel;
    View root;
    RecyclerView rvDefensePost;
    LinearLayoutManager manager;
    ProgressBar progressBarDefense;
    ArrayList<Category> list;
    CategoryAdapter adapter;
    Boolean isScrolling = false  ;
    int currentItems, totalItems, scrollOutItems;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout llNoData, llNoInternet;
    RelativeLayout rlMain;
    Button btnRetry;
    Context context;

    int limit = 0;
    String category = Integer.toString(3);
    String id, imageUrl, blog, time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(DefenseViewModel.class);
        root = inflater.inflate(R.layout.defense_fragment, container, false);
        context = getActivity();

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

        rvDefensePost.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

                if(isScrolling && (currentItems + scrollOutItems == totalItems - 1 ))
                {
                    isScrolling = false;
                    getMoreData();
                }
            }
        });

        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) { } });
        return root;
    }

    private void getMoreData() {
        progressBarDefense.setVisibility(View.VISIBLE);
        limit = limit +20;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_LINKS_CATEGORY,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                                    for(int i = 0; i<jsonArray.length(); i++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        imageUrl = object.getString("images");
                                        time = object.getString("time");
                                        Category temp = new Category(imageUrl, time);
                                        list.add(temp);
                                    }

                                    adapter.notifyDataSetChanged();
                                    progressBarDefense.setVisibility(View.GONE);

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
                        params.put("category", category);
                        return params;
                    }
                };
                MySingleton.getInstance(getContext()).addtoRequestQueue(request);

            }
        }, 1000);

    }

    private void GetDataFirstTime() {
        limit = 0;
        list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,
                ApiConstants.GET_LINKS_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for(int i = 0; i<jsonArray.length(); i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                imageUrl = object.getString("images");
                                time = object.getString("time");
                                Category temp = new Category(imageUrl, time);
                                list.add(temp);
                            }

                            if(list.size() == 0)
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }

                            adapter = new CategoryAdapter(getContext(), list);
                            rvDefensePost.setAdapter(adapter);
                            rvDefensePost.setLayoutManager(manager);
                            progressBarDefense.setVisibility(View.GONE);

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
                params.put("category", category);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addtoRequestQueue(request);
    }

    private void Init() {
        rvDefensePost = root.findViewById(R.id.rvDefensePost);
        manager = new LinearLayoutManager(getContext());
        progressBarDefense = root.findViewById(R.id.progressBarDefense);
        list = new ArrayList<>();
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);

        llNoData = root.findViewById(R.id.llNoData);
        llNoInternet = root.findViewById(R.id.llNoInternet);
        rlMain = root.findViewById(R.id.rlMain);
        btnRetry = root.findViewById(R.id.btnRetry);
    }

}