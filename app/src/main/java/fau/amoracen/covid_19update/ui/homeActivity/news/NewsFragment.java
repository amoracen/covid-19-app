package fau.amoracen.covid_19update.ui.homeActivity.news;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.NewsArticles;
import fau.amoracen.covid_19update.service.APIRequest;
import fau.amoracen.covid_19update.service.MySingleton;

public class NewsFragment extends Fragment {
    private RecyclerView NewsRecyclerView;
    private ProgressBar progressBar;
    private TextView messageTextView;

    /**
     * Add a listener to the menu to search
     *
     * @param menu     a menu object
     * @param inflater inflate with the new menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    return false;
                }
                makeRequest(query.trim());
                hideKeyboard(getView());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        messageTextView = view.findViewById(R.id.errorTextView);
        makeRequest("COVID");
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest(String topic) {
        String url = "https://newsapi.org/v2/everything?q=" + topic + "&from=2020-04-16&sortBy=publishedAt&apiKey=" + getContext().getString(R.string.news_api_key) + "&pageSize=50&page=1&language=en";
        APIRequest request = new APIRequest<>(url, NewsArticles.class, new Response.Listener<NewsArticles>() {
            @Override
            public void onResponse(NewsArticles response) {
                progressBar.setVisibility(View.GONE);
                messageTextView.setText(getString(R.string.latest_news));
                updateUI(response.getArticles());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                messageTextView.setText(getString(R.string.error_news));
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
        NewsRecyclerView.removeAllViews();
        NewsAdapter adapter = new NewsAdapter(articles);
        NewsRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        NewsRecyclerView.setLayoutManager(linearLayoutManager);
        NewsRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Hide Keyboard form View
     *
     * @param view current View
     */
    private void hideKeyboard(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
