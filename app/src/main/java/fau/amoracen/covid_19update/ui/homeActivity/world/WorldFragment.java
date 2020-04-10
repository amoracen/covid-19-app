package fau.amoracen.covid_19update.ui.homeActivity.world;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.GlobalStats;
import fau.amoracen.covid_19update.service.APIRequest;
import fau.amoracen.covid_19update.service.APIRequestCountries;
import fau.amoracen.covid_19update.service.MySingleton;

public class WorldFragment extends Fragment {
    private TextView resultTextView;
    private TextView updatedTextView;
    private TextView totalCasesTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_world_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultTextView = view.findViewById(R.id.welcomeTextView);
        updatedTextView = view.findViewById(R.id.updatedTextView);
        totalCasesTextView = view.findViewById(R.id.totalCasesTextView);
        String url = "https://corona.lmao.ninja/all";
        makeRequest(url);
    }

    /**
     * Start the Request to the API
     *
     * @param url a string
     */
    private void makeRequest(String url) {
        APIRequest request = new APIRequest<>(url, GlobalStats.class, new Response.Listener<GlobalStats>() {
            @Override
            public void onResponse(GlobalStats response) {
                //resultTextView.setText(response.toString());
                long seconds = Long.parseLong(response.getUpdated());
                Date date = new Date(seconds);
                String updatedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz", Locale.getDefault()).format(date);
                updatedTextView.setText("Data Updated: \n" + updatedDate);
                totalCasesTextView.setText("Total Cases: " + formatNumber(response.getCases()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTextView.setText("Search Failed");
            }
        });
        // Add a request to your RequestQueue.
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    public String formatNumber(String number) {
        if (number == null) {
            return "0";
        }
        double num = Double.parseDouble(number);
        return String.format(Locale.getDefault(), "%,.0f", num);
    }
}
