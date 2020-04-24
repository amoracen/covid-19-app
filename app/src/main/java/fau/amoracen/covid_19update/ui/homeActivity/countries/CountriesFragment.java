package fau.amoracen.covid_19update.ui.homeActivity.countries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;
import fau.amoracen.covid_19update.database.SQLiteDatabaseUtil;
import fau.amoracen.covid_19update.service.APIRequestList;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Manages request to the API to get Countries Stats
 * Updates UI
 */
public class CountriesFragment extends Fragment {
    private TextView dataUpdatedTextView;
    private RecyclerView countriesRecyclerView;
    private HorizontalScrollView horizontalScrollView;
    private CountriesAdapter adapter;
    private ProgressBar progressBar;
    private long timeDataWasUpdated;
    private SQLiteDatabaseUtil sqLiteDatabaseUtil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.countries_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*SQLite Database*/
        sqLiteDatabaseUtil = new SQLiteDatabaseUtil(Objects.requireNonNull(getContext()), "Stats");
        String query = "CREATE TABLE IF NOT EXISTS CountryData (country VARCHAR,updated  VARCHAR, cases VARCHAR, todayCases VARCHAR,deaths VARCHAR," +
                "todayDeaths VARCHAR,recovered VARCHAR,active VARCHAR,critical VARCHAR,casesPerOneMillion VARCHAR,deathsPerOneMillion VARCHAR," +
                "tests VARCHAR,testsPerOneMillion VARCHAR,flag VARCHAR,iso2 VARCHAR,iso3 VARCHAR)";
        sqLiteDatabaseUtil.createTable(query);

        dataUpdatedTextView = view.findViewById(R.id.updatedTextView);
        dataUpdatedTextView.setVisibility(View.INVISIBLE);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        countriesRecyclerView = view.findViewById(R.id.countriesRecyclerView);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        makeRequest();
    }

    /**
     * Set the date and time the data was updated
     *
     * @param dateSTR a string
     */
    private void setDataUpdatedTextView(String dateSTR) {
        //Get date
        timeDataWasUpdated = Long.parseLong(dateSTR);
        Date date = new Date(timeDataWasUpdated);
        String updatedDate = new SimpleDateFormat("MMM dd yyyy hh:mm:ss zzz", Locale.getDefault()).format(date);
        dataUpdatedTextView.setText(getString(R.string.data_updated, updatedDate));
        dataUpdatedTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Add a listener to the menu to refresh the data, and search the table
     *
     * @param menu     a menu object
     * @param inflater inflate with the new menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter == null) return false;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                long currentTime = System.currentTimeMillis();
                //900 Seconds or 15 minutes to milliseconds
                if (currentTime >= (timeDataWasUpdated + 900 * 1000)) {
                    makeRequest();
                } else {
                    Toast.makeText(getContext(), "The Data is up to Date", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest() {
        Type collectionType = new TypeToken<List<CountryData>>() {
        }.getType();
        APIRequestList request = new APIRequestList<>(CountryData.URL, collectionType, new Response.Listener<List<CountryData>>() {
            @Override
            public void onResponse(List<CountryData> response) {
                setDataUpdatedTextView(response.get(0).getUpdated());
                updateUI(response);
                /*SAVE to SQLiteDatabase*/
                sqLiteDatabaseUtil.checkCountryDataTable(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                List<CountryData> response = sqLiteDatabaseUtil.getDataFromCountryDataTable();

                if (response != null && !response.isEmpty()) {
                    setDataUpdatedTextView(response.get(0).getUpdated());
                    Toast.makeText(getContext(), "Update Failed, Using Last Known Stats", Toast.LENGTH_LONG).show();
                    updateUI(response);
                } else {
                    dataUpdatedTextView.setText(getString(R.string.data_updated, "Failed"));
                    dataUpdatedTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        // Add a request to your RequestQueue.
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * Update UI
     *
     * @param countries a list of countries returned by the API
     */
    private void updateUI(List<CountryData> countries) {
        progressBar.setVisibility(View.GONE);
        adapter = new CountriesAdapter(countries);
        countriesRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        countriesRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (sqLiteDatabaseUtil != null) {
            sqLiteDatabaseUtil.close();
        }
    }
}
