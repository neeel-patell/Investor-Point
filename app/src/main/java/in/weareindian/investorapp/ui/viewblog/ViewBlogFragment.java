package in.weareindian.investorapp.ui.viewblog;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import in.weareindian.investorapp.api.ApiConstants;
import in.weareindian.investorapp.model.MySingleton;
import in.weareindian.investorapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewBlogFragment extends Fragment {

    private ViewBlogViewModel mViewModel;

    View root;
    ImageView ivViewBlogImage;
    TextView tvViewBlogSubject, tvViewBlogDescription;
    Button btnViewBlogShare;

    String blogId;

    String blogImageUrl, blogSubject, blogDescription;

    String ramdomCharater;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(ViewBlogViewModel.class);
        root = inflater.inflate(R.layout.view_blog_fragment, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);

        Init();
        GetBlogDetails();



        btnViewBlogShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRandomCharacter(1);

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = ramdomCharater;
                String shareSub = "Blog Image Url ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

            }
        });


        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    private void GetBlogDetails() {

        StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.GET_BLOG_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int count = 0; count<jsonArray.length(); count++)
                            {
                                JSONObject object = jsonArray.getJSONObject(count);
                                blogImageUrl = object.getString("image");
                                blogSubject = object.getString("subject");
                                blogDescription = object.getString("description");
                            }

                            Glide.with(getContext()).load(blogImageUrl).into(ivViewBlogImage);
                            tvViewBlogSubject.setText(blogSubject);
                            tvViewBlogDescription.setText(blogDescription);
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
                params.put("image", blogId);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addtoRequestQueue(request);
    }


    private void Init() {
        ivViewBlogImage = root.findViewById(R.id.ivViewBlogImage);
        tvViewBlogSubject = root.findViewById(R.id.tvViewBlogSubject);
        tvViewBlogDescription = root.findViewById(R.id.tvViewBlogDescription);
        btnViewBlogShare = root.findViewById(R.id.btnViewBlogShare);

        Bundle bundle= getArguments();

        blogId = bundle.getString("blogId");
    }

    private void generateRandomCharacter(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        ramdomCharater = sb.toString();

    }

}