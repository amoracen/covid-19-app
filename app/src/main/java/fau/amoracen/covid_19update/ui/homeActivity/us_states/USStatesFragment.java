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
import android.widget.ProgressBar;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.USStatesData;
import fau.amoracen.covid_19update.service.APIRequestList;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Manages request to the API to get US States Stats
 * Updates UI
 */
public class USStatesFragment extends Fragment {
    private TextView dataUpdatedTextView;
    private RecyclerView UsStatesRecyclerView;
    private HorizontalScrollView horizontalScrollView;
    private USStatesAdapter adapter;
    private ProgressBar progressBar;

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

        dataUpdatedTextView = view.findViewById(R.id.updatedTextView);
        dataUpdatedTextView.setVisibility(View.INVISIBLE);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        UsStatesRecyclerView = view.findViewById(R.id.usStatesRecyclerView);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        makeRequest();
    }

    /**
     * Set the date and time the data was updated
     */
    private void setDataUpdatedTextView() {
        //Get date
        Calendar calendar = Calendar.getInstance();
        String updatedDate = new SimpleDateFormat("MMM dd yyyy hh:mm:ss zzz", Locale.getDefault()).format(calendar.getTime());
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
        Type collectionType = new TypeToken<List<USStatesData>>() {
        }.getType();
        APIRequestList request = new APIRequestList<>(USStatesData.URL, collectionType, new Response.Listener<List<USStatesData>>() {
            @Override
            public void onResponse(List<USStatesData> response) {
                setDataUpdatedTextView();
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
     * @param usStates a list of states returned by the API
     */
    private void updateUI(List<USStatesData> usStates) {
        progressBar.setVisibility(View.GONE);
        adapter = new USStatesAdapter(usStates);
        UsStatesRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        UsStatesRecyclerView.setLayoutManager(linearLayoutManager);
        horizontalScrollView.setVisibility(View.VISIBLE);
    }
}
