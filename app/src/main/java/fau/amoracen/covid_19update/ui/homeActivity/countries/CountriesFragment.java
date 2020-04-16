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
import android.widget.TextView;

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

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_countries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataUpdatedTextView = view.findViewById(R.id.updatedTextView);
        dataUpdatedTextView.setVisibility(View.INVISIBLE);
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
        long seconds = Long.parseLong(dateSTR);
        Date date = new Date(seconds);
        String updatedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz", Locale.getDefault()).format(date);
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
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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
                /*TODO SAVE SQLite*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                /*TODO Handle Error*/
                dataUpdatedTextView.setText("Search Failed");
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
        adapter = new CountriesAdapter(countries);
        countriesRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        countriesRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }
}
