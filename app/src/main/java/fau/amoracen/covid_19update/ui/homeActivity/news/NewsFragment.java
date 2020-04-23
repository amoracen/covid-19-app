package fau.amoracen.covid_19update.ui.homeActivity.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Calendar;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.NewsArticles;
import fau.amoracen.covid_19update.service.APIRequest;
import fau.amoracen.covid_19update.service.MySingleton;

public class NewsFragment extends Fragment {
    private RecyclerView NewsRecyclerView;
    private NewsAdapter adapter;
    private long timeDataWasUpdated;

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
                //adapter.getFilter().filter(newText);
                return false;
            }
        });
        MenuItem refreshItem = menu.findItem(R.id.action_refresh);
        refreshItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                long currentTime = System.currentTimeMillis();
                //600 Seconds or 10 minutes to milliseconds
                if (currentTime >= (timeDataWasUpdated + 600 * 1000)) {
                    makeRequest();
                } else {
                    Toast.makeText(getContext(), "News are up to Date", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.news_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        makeRequest();
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest() {
        //String url = "https://newsapi.org/v2/top-headlines?country=us&q=COVID&from=2020-04-16&sortBy=publishedAt&apiKey=" + getContext().getString(R.string.news_api_key) + "&pageSize=15&page=1";
        String url = "https://newsapi.org/v2/everything?q=COVID&from=2020-04-16&sortBy=publishedAt&apiKey=" + getContext().getString(R.string.news_api_key) + "&pageSize=25&page=1&language=en";
        //Get date
        Calendar calendar = Calendar.getInstance();
        timeDataWasUpdated = calendar.getTimeInMillis();
        APIRequest request = new APIRequest<>(url, NewsArticles.class, new Response.Listener<NewsArticles>() {
            @Override
            public void onResponse(NewsArticles response) {
                updateUI(response.getArticles());
                /*TODO SAVE SQLite*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                /*TODO Handle Error*/
                Toast.makeText(getContext(), "Search Failed", Toast.LENGTH_LONG).show();
            }
        });
        // Add a request to your RequestQueue.
        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * Update UI
     *
     * @param articles a list of articles returned by the API
     */
    private void updateUI(NewsArticles.Articles[] articles) {
        Log.i("Articles", String.valueOf(articles.length));
        adapter = new NewsAdapter(articles);
        NewsRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        NewsRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
