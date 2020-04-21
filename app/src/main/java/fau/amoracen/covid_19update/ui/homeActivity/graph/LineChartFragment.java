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
import com.github.mikephil.charting.data.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Creates a Line Chart using  cases history, recovered cases history, and deaths history
 */
public class LineChartFragment extends Fragment {

    private ArrayList<Entry> casesYValue, deathsYValue, recoveredYValue;
    private ArrayList<ArrayList> allCases;
    private RecyclerView lineChartRecyclerView;
    private JSONObject casesJSONObject, recoveredJSONObject, deathsJSONObject;

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
        casesYValue = new ArrayList<>();
        deathsYValue = new ArrayList<>();
        recoveredYValue = new ArrayList<>();
        allCases = new ArrayList<>();
    }

    public void makeRequest(final String type, String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (type.equals("Country")) {
                            getDataCountry(response);
                        } else {
                            getDataAll(response);
                        }
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
                if (!caseKey.equals("4/20/20") && !caseKey.equals("3/20/20")) {
                    xAxisList.add(caseKey.replace("/20", ""));
                } else {
                    if (caseKey.equals("4/20/20")) {
                        xAxisList.add("4/20");
                    } else {
                        xAxisList.add("3/20");
                    }
                }
                //Remove two Days
                for (int i = 0; i < 2; i++) {
                    if (casesKeysIterator.hasNext()) casesKeysIterator.next();
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
}
