package fau.amoracen.covid_19update.ui.homeActivity.countries;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;
import fau.amoracen.covid_19update.service.APIRequest;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Updates UI with a Country Data
 */
public class CountryActivity extends AppCompatActivity {
    private TextView CountryTextView, TotalConfirmedTextView, NewConfirmedTextView, TotalRecoveredTextView;
    private TextView ActiveTextView, TotalDeathsTextView, NewDeathsTextView, CriticalTextView, dataUpdatedTextView;
    private TextView casesPerOneMillionTextView, deathsPerOneMillionTextView, testTextView, testsPerOneMillionTextView;
    private ImageView imageView;
    private CountryData country;

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_refresh) {
            makeRequest(CountryData.URLCountry + country.getCountry());
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        /*Get Data From Table*/
        Intent intent = getIntent();
        country = (CountryData) Objects.requireNonNull(intent.getExtras()).getSerializable("Country");

        imageView = findViewById(R.id.imageView);
        CountryTextView = findViewById(R.id.CountryTextView);
        dataUpdatedTextView = findViewById(R.id.updatedTextView);
        NewConfirmedTextView = findViewById(R.id.todayCasesTextView);
        TotalConfirmedTextView = findViewById(R.id.CasesTextView);
        TotalRecoveredTextView = findViewById(R.id.TotalRecoveredTextView);
        ActiveTextView = findViewById(R.id.activeTextView);
        TotalDeathsTextView = findViewById(R.id.deathsTextView);
        NewDeathsTextView = findViewById(R.id.todayDeathsTextView);
        CriticalTextView = findViewById(R.id.criticalTextView);
        casesPerOneMillionTextView = findViewById(R.id.casesPerOneMillionTextView);
        deathsPerOneMillionTextView = findViewById(R.id.deathsPerOneMillionTextView);
        testTextView = findViewById(R.id.testTextView);
        testsPerOneMillionTextView = findViewById(R.id.testsPerOneMillionTextView);
        if (country == null) {
            finish();
        } else {
            updateUI(country);
        }
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest(String url) {
        APIRequest request = new APIRequest<>(url, CountryData.class, new Response.Listener<CountryData>() {
            @Override
            public void onResponse(CountryData response) {
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * Update UI
     *
     * @param country country returned by the API
     */
    private void updateUI(CountryData country) {
        showImage(country.getCountryInfo().getFlag());
        //Get date
        long seconds = Long.parseLong(country.getUpdated());
        Date date = new Date(seconds);
        String updatedDate = new SimpleDateFormat("dd MMM yyyy hh:mm:ss zzz", Locale.getDefault()).format(date);
        dataUpdatedTextView.setText(getString(R.string.data_updated, updatedDate));
        CountryTextView.setText(country.getCountry());
        NewConfirmedTextView.setText(getString(R.string.new_cases, country.getTodayCases()));
        TotalConfirmedTextView.setText(getString(R.string.total_confirmed_cases, country.getCases()));
        TotalRecoveredTextView.setText(getString(R.string.recovered, country.getRecovered()));
        ActiveTextView.setText(getString(R.string.active_cases, country.getActive()));
        TotalDeathsTextView.setText(getString(R.string.deaths, country.getDeaths()));
        NewDeathsTextView.setText(getString(R.string.new_deaths, country.getTodayDeaths()));
        CriticalTextView.setText(getString(R.string.critical_cases, country.getCritical()));
        casesPerOneMillionTextView.setText(getString(R.string.cases_per_one_million, country.getCasesPerOneMillion()));
        deathsPerOneMillionTextView.setText(getString(R.string.deaths_per_one_million, country.getDeathsPerOneMillion()));
        testTextView.setText(getString(R.string.total_test, country.getTests()));
        testsPerOneMillionTextView.setText(getString(R.string.test_per_one_million, country.getTestsPerOneMillion()));
    }

    /**
     * Show Image using Picasso library
     *
     * @param url URL of the flag
     */
    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(200, 150).centerInside().into(imageView);
        }
    }
}
