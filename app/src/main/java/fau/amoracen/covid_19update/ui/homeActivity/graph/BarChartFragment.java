package fau.amoracen.covid_19update.ui.homeActivity.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fau.amoracen.covid_19update.R;
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
        newDailyCasesList = new ArrayList<>();
        newDailyDeathsList = new ArrayList<>();
        totalCasesList = new ArrayList<>();
        totalDeathsList = new ArrayList<>();
        //totalRecoveriesList = new ArrayList<>();
        allCases = new ArrayList<>();
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
        String url = "https://thevirustracker.com/free-api?countryTimeline=" + country;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        clearAll();
                        getDataCountry(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //selectedTextView.setText("Search Failed \n");
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
            /*Remove first days until 3/25/20*/
            boolean dateFound = false;
            while (casesKeysIterator.hasNext() && !dateFound) {
                String caseKey = casesKeysIterator.next();
                if (caseKey.equals("3/25/20")) {
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
                if (!caseKey.equals("4/20/20") && !caseKey.equals("3/20/20")) {
                    xAxisList.add(caseKey.replace("/20", ""));
                } else {
                    if (caseKey.equals("4/20/20")) {
                        xAxisList.add("4/20");
                    } else {
                        xAxisList.add("3/20");
                    }
                }
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
}
