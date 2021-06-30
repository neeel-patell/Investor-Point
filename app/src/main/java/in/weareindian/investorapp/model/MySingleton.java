package in.weareindian.investorapp.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static  MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context cntxt;

    private MySingleton(Context context){

        cntxt = context;
        requestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null)
        {

            requestQueue = Volley.newRequestQueue(cntxt.getApplicationContext());

        }
        return  requestQueue;

    }

    public static  synchronized  MySingleton getInstance(Context context){
        if(mInstance == null){
             mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public <T>void addtoRequestQueue(Request<T> request){
            requestQueue.add(request);
    }

}
