package fau.amoracen.covid_19update.ui.homeActivity.graph;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.data.BarEntry;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.database.SQLiteDatabaseUtil;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Creates a Bar Chart using newDailyCases, newDailyDeaths totalCases, and totalDeaths
 */
public class BarChartFragment extends Fragment {
    private JSONObject allData;
    private List<String> xAxisList;
    private ArrayList<BarEntry> newDailyCasesList, newDailyDeathsList, totalCasesList, totalRecoveriesList, totalDeathsList;
    private ArrayList<ArrayList> allCases;
    private RecyclerView barChartRecyclerView;
    private SQLiteDatabaseUtil sqLiteDatabaseUtil;
    private int days;
    private String country;
    private TextView errorTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_bar_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChartRecyclerView = view.findViewById(R.id.barChartRecyclerView);
        errorTextView = view.findViewById(R.id.errorTextView);
        newDailyCasesList = new ArrayList<>();
        newDailyDeathsList = new ArrayList<>();
        totalCasesList = new ArrayList<>();
        totalDeathsList = new ArrayList<>();
        //totalRecoveriesList = new ArrayList<>();
        allCases = new ArrayList<>();
        /*SQLite Database*/
        sqLiteDatabaseUtil = new SQLiteDatabaseUtil(Objects.requireNonNull(getContext()), "Stats");
        String query = "CREATE TABLE IF NOT EXISTS CountryTimeline (dates_values  VARCHAR,country VARCHAR)";
        String query2 = "CREATE TABLE IF NOT EXISTS CountryTimelineDays (days  INTEGER)";
        sqLiteDatabaseUtil.createTable(query);
        sqLiteDatabaseUtil.createTable(query2);
        JodaTimeAndroid.init(getContext());
    }

    /**
     * Clear all list before adding the data
     */
    private void clearAll() {
        newDailyCasesList.clear();
        newDailyDeathsList.clear();
        totalCasesList.clear();
        totalDeathsList.clear();
        allCases.clear();
    }

    /**
     * Make request to the API
     *
     * @param country a string
     */
    public void makeRequest(String country) {
        clearAll();
        String url = "https://thevirustracker.com/free-api?countryTimeline=" + country;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        errorTextView.setVisibility(View.GONE);
                        getDataCountry(response);
                        /*SAVE to SQLiteDatabase*/
                        sqLiteDatabaseUtil.checkCountryTimelineTable(getDays(), getCountry().toLowerCase(), response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String savedResponse = null;
                        int days = 0;
                        try {
                            days = sqLiteDatabaseUtil.getDataFromCountryTimelineDaysTable();
                            savedResponse = sqLiteDatabaseUtil.getDataFromCountryTimelineTable(getCountry().toLowerCase());
                            if (savedResponse != null && days != 0) {
                                setDays(days);
                                JSONObject response = new JSONObject(savedResponse);
                                getDataCountry(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (savedResponse == null || days == 0) {
                            Log.i("Tag", "CountryTimeline NOT Found:" + getCountry());
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText(getString(R.string.update_failed));
                        } else {
                            Toast.makeText(getContext(), "Bar Chart Update Failed, Using Last Known Stats", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Get data from the response
     *
     * @param response JSONObject
     */
    private void getDataCountry(JSONObject response) {
        try {
            JSONArray timelineitems = response.getJSONArray("timelineitems");
            allData = (JSONObject) timelineitems.get(0);
            Iterator<String> casesKeysIterator = allData.keys();
            setAllCases(casesKeysIterator);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Values for all bar charts
     *
     * @param casesKeysIterator an iterator with the key values
     */
    private void setAllCases(Iterator<String> casesKeysIterator) {
        int count = 0;
        xAxisList = new ArrayList<>();
        try {
            /*Remove first days until*/
            DateTime now = new DateTime();
            DateTime weeksAgo = now.minusDays(getDays() + 1);
            Date weekAgoDate = weeksAgo.toDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yy", Locale.getDefault());
            String formattedDate = dateFormat.format(weekAgoDate);
            boolean dateFound = false;
            while (casesKeysIterator.hasNext() && !dateFound) {
                String caseKey = casesKeysIterator.next();
                if (caseKey.equals(formattedDate)) {
                    dateFound = true;
                }
            }

            while (casesKeysIterator.hasNext()) {
                String caseKey = casesKeysIterator.next();
                JSONObject date = allData.getJSONObject(caseKey);
                int newDailyCases = Integer.parseInt(date.getString("new_daily_cases"));
                int newDailyDeaths = Integer.parseInt(date.getString("new_daily_deaths"));
                int totalCases = Integer.parseInt(date.getString("total_cases"));
                int totalDeaths = Integer.parseInt(date.getString("total_deaths"));

                newDailyCasesList.add(new BarEntry(count, Float.parseFloat(String.valueOf(Math.abs(newDailyCases)))));
                newDailyDeathsList.add(new BarEntry(count, Float.parseFloat(String.valueOf(Math.abs(newDailyDeaths)))));
                totalCasesList.add(new BarEntry(count, Float.parseFloat(String.valueOf(Math.abs(totalCases)))));
                totalDeathsList.add(new BarEntry(count, Float.parseFloat(String.valueOf(Math.abs(totalDeaths)))));
                //totalRecoveriesList.add(new BarEntry(count, Float.parseFloat(date.getString("total_recoveries"))));
                count += 1;
                //Format X Axis Labels
                xAxisList.add(caseKey.replaceFirst("/20", ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (!totalDeathsList.isEmpty()) {
                allCases.add(newDailyCasesList);
                allCases.add(newDailyDeathsList);
                allCases.add(totalCasesList);
                allCases.add(totalDeathsList);
                /*Call Bar Chart Adapter*/
                createBarCharts();
            }
        }
    }

    /**
     * Create multiple bar charts
     */
    private void createBarCharts() {
        BarChartAdapter adapter = new BarChartAdapter(allCases, xAxisList);
        barChartRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        barChartRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (sqLiteDatabaseUtil != null) {
            sqLiteDatabaseUtil.close();
        }
    }

    /**
     * Set Number of days
     *
     * @param days a string
     */
    public void setDays(int days) {
        this.days = days;
    }

    /**
     * Get Number of days
     */
    private int getDays() {
        return days;
    }

    /**
     * Set country
     *
     * @param country a string
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get country
     */
    public String getCountry() {
        return country;
    }
}
