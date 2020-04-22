package fau.amoracen.covid_19update.ui.homeActivity.world;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.GlobalStats;
import fau.amoracen.covid_19update.service.APIRequest;
import fau.amoracen.covid_19update.service.MySingleton;
import fau.amoracen.covid_19update.service.SQLiteDatabaseUtil;
import fau.amoracen.covid_19update.ui.homeActivity.graph.LineChartFragment;
import fau.amoracen.covid_19update.ui.homeActivity.graph.PieChartFragment;

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
    private PieChartFragment pieChartFragment;
    private FrameLayout pieChartFragmentLayout;
    private LineChartFragment lineChartFragment;
    private FrameLayout lineChartFragmentLayout;
    private ProgressBar progressBar;
    private LinearLayout worldLinearLayout;
    private long timeDataWasUpdated;
    private SQLiteDatabaseUtil sqLiteDatabaseUtil;

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
                long currentTime = System.currentTimeMillis();
                //660 Seconds or 11 minutes to milliseconds
                if (currentTime >= (timeDataWasUpdated + 660 * 1000)) {
                    makeRequest();
                } else {
                    Toast.makeText(getContext(), "The Data is up to Date", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*SQLite Database*/
        sqLiteDatabaseUtil = new SQLiteDatabaseUtil(Objects.requireNonNull(getContext()), "GlobalStats");
        String query = "CREATE TABLE IF NOT EXISTS GlobalStats (updated VARCHAR, cases VARCHAR,todayCases VARCHAR,deaths VARCHAR,todayDeaths VARCHAR," +
                "recovered VARCHAR,active VARCHAR,critical VARCHAR,casesPerOneMillion VARCHAR,deathsPerOneMillion VARCHAR,tests VARCHAR,testsPerOneMillion VARCHAR,affectedCountries VARCHAR)";
        sqLiteDatabaseUtil.createTable(query);

        progressBar = view.findViewById(R.id.progressBar);
        worldLinearLayout = view.findViewById(R.id.worldwideLinerLayout);
        progressBar.setVisibility(View.VISIBLE);
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
        pieChartFragmentLayout = view.findViewById(R.id.container_pie_chart);
        pieChartFragment = new PieChartFragment();
        lineChartFragmentLayout = view.findViewById(R.id.container_line_chart);
        lineChartFragment = new LineChartFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.container_pie_chart, pieChartFragment).commit();
        getChildFragmentManager().beginTransaction().replace(R.id.container_line_chart, lineChartFragment).commit();
    }

    /**
     * Update UI
     *
     * @param response a GlobalStats object
     */
    private void updateUI(final GlobalStats response) {
        //Get date
        timeDataWasUpdated = Long.parseLong(response.getUpdated());
        Date date = new Date(timeDataWasUpdated);
        String updatedDate = new SimpleDateFormat("MMM dd yyyy hh:mm:ss zzz", Locale.getDefault()).format(date);
        dataUpdatedTextView.setText(getString(R.string.data_updated, updatedDate));
        //Cases
        totalCasesTextView.setText(getString(R.string.total_confirmed_cases, response.getCases()));
        //New Cases
        todayCasesTextView.setText(getString(R.string.new_cases, response.getTodayCases()));
        //Deaths
        deathsTextView.setText(getString(R.string.deaths, response.getDeaths()));
        //New Deaths
        todayDeathsTextView.setText(getString(R.string.new_deaths, response.getTodayDeaths()));
        //Recovered
        recoveredTextView.setText(getString(R.string.recovered, response.getRecovered()));
        //Active Cases
        activeTextView.setText(getString(R.string.active_cases, response.getActive()));
        //Critical
        criticalTextView.setText(getString(R.string.critical_cases, response.getCritical()));
        //Cases Per one
        casePerOneMillionTextView.setText(getString(R.string.cases_per_one_million, response.getCasesPerOneMillion()));
        //Deaths Per One
        deathsPerOneMillionTextView.setText(getString(R.string.deaths_per_one_million, response.getDeathsPerOneMillion()));
        //Tests
        testsTextView.setText(getString(R.string.total_test, response.getTests()));
        //Test Per One
        testPerOneMillionTextView.setText(getString(R.string.test_per_one_million, response.getTestsPerOneMillion()));
        //Affected Countries
        affectedCountriesTextView.setText(getString(R.string.affected_countries, response.getAffectedCountries()));
        //Create Pie Chart
        int TIME_OUT = 500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pieChartFragment.createPieChart(Objects.requireNonNull(getContext()), response.getActiveNoFormat(), response.getRecoveredNoFormat(), response.getDeathsNoFormat());
                String url = "https://corona.lmao.ninja/v2/historical/all?lastdays=40";
                lineChartFragment.makeRequest("All", url);
                pieChartFragmentLayout.setVisibility(View.VISIBLE);
                lineChartFragmentLayout.setVisibility(View.VISIBLE);
            }
        }, TIME_OUT);
        progressBar.setVisibility(View.GONE);
        worldLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest() {
        APIRequest request = new APIRequest<>(GlobalStats.URL, GlobalStats.class, new Response.Listener<GlobalStats>() {
            @Override
            public void onResponse(GlobalStats response) {
                updateUI(response);
                /*SAVE to SQLiteDatabase*/
                sqLiteDatabaseUtil.checkGlobalStatsTable(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                GlobalStats savedInDatabase = sqLiteDatabaseUtil.getDataFromGlobalStatsTable();

                if (savedInDatabase != null && savedInDatabase.getUpdated() != null) {
                    Toast.makeText(getContext(), "Update Failed, Using Last Known Stats", Toast.LENGTH_LONG).show();
                    updateUI(savedInDatabase);
                } else {
                    dataUpdatedTextView.setText(getString(R.string.data_updated, "Failed"));
                    worldLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // Add a request to your RequestQueue.
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
