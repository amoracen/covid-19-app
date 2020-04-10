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

import java.util.List;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;
import fau.amoracen.covid_19update.service.APIRequestCountries;
import fau.amoracen.covid_19update.service.MySingleton;

public class CountriesFragment extends Fragment {
    private TextView resultTextView;
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

        resultTextView = view.findViewById(R.id.novelTextView);
        //String url = "https://corona.lmao.ninja/countries?sort=country";
        String url = "https://corona.lmao.ninja/countries?sort=cases";
        makeRequest(url);
        countriesRecyclerView = view.findViewById(R.id.countriesRecyclerView);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
    }


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
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Start the Request to the API
     *
     * @param url a string
     */
    private void makeRequest(String url) {
        APIRequestCountries request = new APIRequestCountries<>(url, CountryData.class, new Response.Listener<List<CountryData>>() {
            @Override
            public void onResponse(List<CountryData> response) {
                updateUI(response);
                //resultTextView.setText(size);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                resultTextView.setText("Search Failed");
            }
        });
        // Add a request to your RequestQueue.
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void updateUI(List<CountryData> countries) {
        adapter = new CountriesAdapter(countries);
        countriesRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        countriesRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }
}
