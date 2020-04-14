package fau.amoracen.covid_19update.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    /**
     * Constructor
     *
     * @param context object calling this class
     */
    private MySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Get Instance
     *
     * @param context object calling this class
     * @return instance of MySingleton class
     */
    public static synchronized MySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    /**
     * Creates Request to Queue
     *
     * @return request queue object
     */
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds request to queue
     *
     * @param req type of request
     * @param <T> generic object
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}