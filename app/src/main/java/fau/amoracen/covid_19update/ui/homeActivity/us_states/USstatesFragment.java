package fau.amoracen.covid_19update.ui.homeActivity.us_states;

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
import java.util.List;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.USStatesData;
import fau.amoracen.covid_19update.service.APIRequestList;
import fau.amoracen.covid_19update.service.MySingleton;

public class USstatesFragment extends Fragment {
    private TextView resultTextView;
    private RecyclerView UsStatesRecyclerView;
    private HorizontalScrollView horizontalScrollView;
    private USStatesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_us_states, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultTextView = view.findViewById(R.id.usTextView);
        String urlUS = "https://corona.lmao.ninja/states";
        makeRequest(urlUS);
        UsStatesRecyclerView = view.findViewById(R.id.usStatesRecyclerView);
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
        Type collectionType = new TypeToken<List<USStatesData>>() {
        }.getType();
        APIRequestList request = new APIRequestList<>(url,collectionType, new Response.Listener<List<USStatesData>>() {
            @Override
            public void onResponse(List<USStatesData> response) {
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

    private void updateUI(List<USStatesData> usStates) {
        adapter = new USStatesAdapter(usStates);
        UsStatesRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        UsStatesRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }
}
