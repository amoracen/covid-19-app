package fau.amoracen.covid_19update.ui.homeActivity.world;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Manages request to the API to get Global Stats
 * Updates UI
 */
public class WorldFragment extends Fragment {
    private TextView dataUpdatedTextView;
    private TextView totalCasesTextView, todayCasesTextView;
    private TextView deathsTextView, todayDeathsTextView;
    private TextView recoveredTextView, activeTextView;
    private TextView criticalTextView, casePerOneMillionTextView;
    private TextView deathsPerOneMillionTextView, testsTextView;
    private TextView testPerOneMillionTextView, affectedCountriesTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_world_stats, container, false);
    }

    /**
     * Add a listener to the menu to refresh the data
     *
     * @param menu     a menu object
     * @param inflater inflate with the new menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                makeRequest();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataUpdatedTextView = view.findViewById(R.id.updatedTextView);
        totalCasesTextView = view.findViewById(R.id.casesTextView);
        todayCasesTextView = view.findViewById(R.id.todayCasesTextView);
        deathsTextView = view.findViewById(R.id.deathsTextView);
        todayDeathsTextView = view.findViewById(R.id.newDeathsTextView);
        recoveredTextView = view.findViewById(R.id.recoveredTextView);
        activeTextView = view.findViewById(R.id.activeTextView);
        criticalTextView = view.findViewById(R.id.criticalTextView);
        casePerOneMillionTextView = view.findViewById(R.id.casesPerOneMillionTextView);
        deathsPerOneMillionTextView = view.findViewById(R.id.deathsPerOneMillionTextView);
        testsTextView = view.findViewById(R.id.testTextView);
        testPerOneMillionTextView = view.findViewById(R.id.testsPerOneMillionTextView);
        affectedCountriesTextView = view.findViewById(R.id.affectedCountriesTextView);
        makeRequest();
    }

    /**
     * Update UI
     *
     * @param response a GlobalStats object
     */
    private void updateUI(GlobalStats response) {
        //Get date
        long seconds = Long.parseLong(response.getUpdated());
        Date date = new Date(seconds);
        String updatedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz", Locale.getDefault()).format(date);
        dataUpdatedTextView.append("\n" + updatedDate);
        //Cases
        totalCasesTextView.append("\n" + response.getCases());
        //New Cases
        todayCasesTextView.append("\n" + response.getTodayCases());
        //Deaths
        deathsTextView.append("\n" + response.getDeaths());
        //New Deaths
        todayDeathsTextView.append("\n" + response.getTodayDeaths());
        //Recovered
        recoveredTextView.append("\n" + response.getRecovered());
        //Active Cases
        activeTextView.append("\n" + response.getActive());
        //Critical
        criticalTextView.append("\n" + response.getCritical());
        //Cases Per one
        casePerOneMillionTextView.append("\n" + response.getCasesPerOneMillion());
        //Deaths Per One
        deathsPerOneMillionTextView.append("\n" + response.getDeathsPerOneMillion());
        //Tests
        testsTextView.append("\n" + response.getTests());
        //Test Per One
        testPerOneMillionTextView.append("\n" + response.getTestsPerOneMillion());
        //Affected Countries
        affectedCountriesTextView.append("\n" + response.getAffectedCountries());
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest() {
        APIRequest request = new APIRequest<>(GlobalStats.URL, GlobalStats.class, new Response.Listener<GlobalStats>() {
            @Override
            public void onResponse(GlobalStats response) {
                updateUI(response);
                /*TODO SAVE SQLite*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*TODO Handle Error*/
                dataUpdatedTextView.setText("Search Failed");
            }
        });
        // Add a request to your RequestQueue.
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
