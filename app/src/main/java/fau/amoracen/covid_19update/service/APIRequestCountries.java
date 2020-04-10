package fau.amoracen.covid_19update.service;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import fau.amoracen.covid_19update.data.CountryData;

public class APIRequestCountries<T> extends Request<List<CountryData>> {
    private final Gson gson = new Gson();
    private Class<T> clazz;
    private final Response.Listener<List<CountryData>> listener;


    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public APIRequestCountries(String url, Class<T> clazz, Response.Listener<List<CountryData>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(List<CountryData> response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<List<CountryData>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data);
            Type collectionType = new TypeToken<List<CountryData>>(){}.getType();
            List<CountryData> lcs = (List<CountryData>) gson.fromJson( json , collectionType);
            return Response.success(lcs, HttpHeaderParser.parseCacheHeaders(response));

        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}