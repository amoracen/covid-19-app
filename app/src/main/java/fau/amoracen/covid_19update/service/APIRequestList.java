package fau.amoracen.covid_19update.service;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Manages response from the API
 *
 * @param <T>
 */
public class APIRequestList<T> extends Request<List<T>> {
    private final Gson gson = new Gson();
    private final Response.Listener<List<T>> listener;
    private Type collectionType;


    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url            URL of the request to make
     * @param collectionType Relevant class object, for Gson's reflection
     */
    public APIRequestList(String url, Type collectionType, Response.Listener<List<T>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
        this.collectionType = collectionType;
    }

    /**
     * Send response to listener
     *
     * @param response a list of object
     */
    @Override
    protected void deliverResponse(List<T> response) {
        listener.onResponse(response);
    }

    /**
     * Parse response to a list of objects
     *
     * @param response
     * @return a list
     */
    @Override
    protected Response<List<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data);
            List<T> dataList = gson.fromJson(json, collectionType);
            return Response.success(dataList, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}