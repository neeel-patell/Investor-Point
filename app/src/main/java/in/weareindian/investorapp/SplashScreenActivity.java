package in.weareindian.investorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.weareindian.investorapp.api.ApiConstants;
import in.weareindian.investorapp.model.MySingleton;
import in.weareindian.investorapp.model.SP;
import in.weareindian.investorapp.R;

public class SplashScreenActivity extends AppCompatActivity {
    //Handler handler;
    VideoView vvSplashVideo;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        vvSplashVideo = findViewById(R.id.vvSplashVideo);

        setVideo();

        /*FirebaseMessaging.getInstance().subscribeToTopic("allDevices").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Success";
                if(!task.isSuccessful())
                {
                    msg = "Failed";
                }
                Log.d("msg" , msg);
                //Toast.makeText(SplashScreenActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });*/

        /*handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);*/
    }

    private void setVideo() {

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.quiknews_splash_video);
        vvSplashVideo.setVideoURI(video);

        vvSplashVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                startNextActivity();
            }
        });

        vvSplashVideo.start();

    }


    private void generateFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                final String token = instanceIdResult.getToken();
                //Log.i("FCM Token", token);
                //Toast.makeText(SplashScreenActivity.this, token, Toast.LENGTH_SHORT).show();

                sp = getSharedPreferences(SP.FCM_TOKEN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(SP.FCM_TOKEN, token);
                editor.commit();

                StringRequest request = new StringRequest(Request.Method.POST, ApiConstants.REGISTER_DEVICE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    String message = null;

                                    for(int count = 0; count<jsonArray.length(); count++)
                                    {
                                        JSONObject object = jsonArray.getJSONObject(count);
                                        message = object.getString("message");
                                    }

                                    //Toast.makeText(SplashScreenActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        params.put("token", token);
                        return params;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addtoRequestQueue(request);
            }
        });
    }

    private void startNextActivity()
    {
        generateFCMToken();
        Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}