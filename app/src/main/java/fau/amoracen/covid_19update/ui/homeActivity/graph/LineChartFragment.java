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
import com.github.mikephil.charting.data.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.database.SQLiteDatabaseUtil;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Creates a Line Chart using  cases history, recovered cases history, and deaths history
 */
public class LineChartFragment extends Fragment {

    private ArrayList<Entry> casesYValue, deathsYValue, recoveredYValue;
    private ArrayList<ArrayList> allCases;
    private RecyclerView lineChartRecyclerView;
    private JSONObject casesJSONObject, recoveredJSONObject, deathsJSONObject;
    private String days, country;
    private SQLiteDatabaseUtil sqLiteDatabaseUtil;
    private TextView errorTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChartRecyclerView = view.findViewById(R.id.lineChartRecyclerView);
        errorTextView = view.findViewById(R.id.errorTextView);
        casesYValue = new ArrayList<>();
        deathsYValue = new ArrayList<>();
        recoveredYValue = new ArrayList<>();
        allCases = new ArrayList<>();
        /*SQLite Database*/
        sqLiteDatabaseUtil = new SQLiteDatabaseUtil(Objects.requireNonNull(getContext()), "Stats");
        String query = "CREATE TABLE IF NOT EXISTS HistoricalAll (dates_values  VARCHAR)";
        String query2 = "CREATE TABLE IF NOT EXISTS HistoricalAllDays (days  VARCHAR)";
        String query3 = "CREATE TABLE IF NOT EXISTS HistoricalCountry (dates_values  VARCHAR,country VARCHAR)";
        String query4 = "CREATE TABLE IF NOT EXISTS HistoricalCountryDays (days  VARCHAR)";
        sqLiteDatabaseUtil.createTable(query);
        sqLiteDatabaseUtil.createTable(query2);
        sqLiteDatabaseUtil.createTable(query3);
        sqLiteDatabaseUtil.createTable(query4);
    }

    /**
     * Clear all data
     */
    private void clearAll() {
        casesYValue.clear();
        deathsYValue.clear();
        recoveredYValue.clear();
        allCases.clear();
    }

    public void makeRequest(final String type, String url) {
        clearAll();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        errorTextView.setVisibility(View.GONE);
                        if (type.equals("Country")) {
                            getDataCountry(response);
                            /*SAVE to SQLiteDatabase*/
                            try {
                                String country = response.getString("country");
                                sqLiteDatabaseUtil.checkHistoricalCountryTable(getDays(), country.toLowerCase(), response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            getDataAll(response);
                            /*SAVE to SQLiteDatabase*/
                            sqLiteDatabaseUtil.checkHistoricalAllTable(getDays(), response.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String savedResponse = null;
                        String days = null;
                        try {
                            if (type.equals("Country")) {
                                days = sqLiteDatabaseUtil.getDataFromHistoricalCountryDaysTable();
                                savedResponse = sqLiteDatabaseUtil.getDataFromHistoricalCountryTable(getCountry().toLowerCase());
                                if (savedResponse != null && days != null) {
                                    setDays(days);
                                    JSONObject response = new JSONObject(savedResponse);
                                    getDataCountry(response);
                                }
                            } else {
                                days = sqLiteDatabaseUtil.getDataFromHistoricalAllDaysTable();
                                savedResponse = sqLiteDatabaseUtil.getDataFromHistoricalAllTable();
                                if (savedResponse != null && days != null) {
                                    setDays(days);
                                    JSONObject response = new JSONObject(savedResponse);
                                    getDataAll(response);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (savedResponse == null || days == null) {
                            Log.i("Tag", "HistoricalCountry NOT Found:" + getCountry());
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText(getString(R.string.update_failed));
                        } else {
                            Toast.makeText(getContext(), "Line Chart Update Failed, Using Last Known Stats", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Create multiple charts
     *
     * @param xAxisList a list used for the x Axis labels
     */
    private void createGraphs(List<String> xAxisList) {
        LineChartAdapter adapter = new LineChartAdapter(allCases, xAxisList);
        lineChartRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lineChartRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * Get Data from the API response for Country Stats
     *
     * @param response a JSONObject
     */
    private void getDataCountry(JSONObject response) {
        try {
            String country = response.getString("country");
            JSONObject timeline = response.getJSONObject("timeline");
            //Get Cases
            casesJSONObject = timeline.getJSONObject("cases");
            //Get Deaths
            deathsJSONObject = timeline.getJSONObject("deaths");
            //Get Recovered
            recoveredJSONObject = timeline.getJSONObject("recovered");
            Iterator<String> casesKeysIterator = casesJSONObject.keys();
            setAllCases(casesKeysIterator);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Data from the API response for WorldWide Stats
     *
     * @param response a JSONObject
     */
    private void getDataAll(JSONObject response) {
        try {
            //Get Cases
            casesJSONObject = response.getJSONObject("cases");
            //Get Deaths
            deathsJSONObject = response.getJSONObject("deaths");
            //Get Recovered
            recoveredJSONObject = response.getJSONObject("recovered");
            Iterator<String> casesKeysIterator = casesJSONObject.keys();
            setAllCases(casesKeysIterator);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Values for all charts
     *
     * @param casesKeysIterator an iterator with the key values
     */
    private void setAllCases(Iterator<String> casesKeysIterator) {
        int count = 0;
        List<String> xAxisList = new ArrayList<>();
        try {
            while (casesKeysIterator.hasNext()) {
                if (!casesKeysIterator.hasNext()) break;

                String caseKey = casesKeysIterator.next();
                casesYValue.add(new Entry(count, Float.parseFloat(casesJSONObject.getString(caseKey))));
                deathsYValue.add(new Entry(count, Float.parseFloat(deathsJSONObject.getString(caseKey))));
                recoveredYValue.add(new Entry(count, Float.parseFloat(recoveredJSONObject.getString(caseKey))));
                count += 1;
                //Format X Axis Labels
                xAxisList.add(caseKey.replaceFirst("/20", ""));
                //Remove two Days if asking for too many days
                if (Integer.parseInt(days) >= 25) {
                    for (int i = 0; i < 2; i++) {
                        if (casesKeysIterator.hasNext()) casesKeysIterator.next();
                    }
                }
            }
            allCases.add(casesYValue);
            allCases.add(recoveredYValue);
            allCases.add(deathsYValue);
            /*Call Line Chart Adapter*/
            createGraphs(xAxisList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Number of days
     *
     * @param days a string
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     * Get Number of days
     */
    private String getDays() {
        return days;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (sqLiteDatabaseUtil != null) {
            sqLiteDatabaseUtil.close();
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
