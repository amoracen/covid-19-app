package fau.amoracen.covid_19update.ui.homeActivity.us_states;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.USStatesData;
import fau.amoracen.covid_19update.service.APIRequest;
import fau.amoracen.covid_19update.service.MySingleton;

/**
 * Updates UI with  State Data
 */

public class USStatesActivity extends AppCompatActivity {
    private TextView stateTextView, casesTextView, todayCasesTextView, deathsTextView, dataUpdatedTextView;
    private TextView todayDeathsTextView, activeTextView, testsTextView, testsPerOneMillionTextView;
    private USStatesData state;

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
            makeRequest(USStatesData.URLState + state.getState());
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_state);

        /*Get Data From Table*/
        Intent intent = getIntent();
        state = (USStatesData) intent.getExtras().getSerializable("USState");

        dataUpdatedTextView = findViewById(R.id.updatedTextView);
        stateTextView = findViewById(R.id.stateTextView);
        casesTextView = findViewById(R.id.CasesTextView);
        todayCasesTextView = findViewById(R.id.todayCasesTextView);
        deathsTextView = findViewById(R.id.deathsTextView);
        todayDeathsTextView = findViewById(R.id.todayDeathsTextView);
        activeTextView = findViewById(R.id.activeTextView);
        testsTextView = findViewById(R.id.testTextView);
        testsPerOneMillionTextView = findViewById(R.id.testsPerOneMillionTextView);

        if (state == null) {
            finish();
        } else {
            updateUI(state);
        }
    }

    /**
     * Start the Request to the API
     */
    private void makeRequest(String url) {
        APIRequest request = new APIRequest<>(url, USStatesData.class, new Response.Listener<USStatesData>() {
            @Override
            public void onResponse(USStatesData response) {
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
     * @param usStatesData state returned by the API
     */
    private void updateUI(USStatesData usStatesData) {
        //Get date
        Calendar calendar = Calendar.getInstance();
        String updatedDate = new SimpleDateFormat("MMM dd yyyy hh:mm:ss zzz", Locale.getDefault()).format(calendar.getTime());
        dataUpdatedTextView.setText(getString(R.string.data_updated, updatedDate));
        stateTextView.setText(usStatesData.getState());
        casesTextView.setText(getString(R.string.total_confirmed_cases, usStatesData.getCases()));
        todayCasesTextView.setText(getString(R.string.new_cases, usStatesData.getTodayCases()));
        deathsTextView.setText(getString(R.string.deaths, usStatesData.getDeaths()));
        todayDeathsTextView.setText(getString(R.string.new_deaths, usStatesData.getTodayDeaths()));
        activeTextView.setText(getString(R.string.active_cases, usStatesData.getActive()));
        testsTextView.setText(getString(R.string.total_test, usStatesData.getTests()));
        testsPerOneMillionTextView.setText(getString(R.string.test_per_one_million, usStatesData.getTestsPerOneMillion()));
    }
}
