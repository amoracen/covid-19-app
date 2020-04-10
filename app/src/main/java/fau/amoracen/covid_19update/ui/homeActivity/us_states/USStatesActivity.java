package fau.amoracen.covid_19update.ui.homeActivity.us_states;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.USStatesData;

public class USStatesActivity extends AppCompatActivity {
    TextView stateTextView;
    TextView casesTextView;
    TextView todayCasesTextView;
    TextView deathsTextView;
    TextView todaydeathsTextView;
    TextView activeTextView;
    TextView testsTextView;
    TextView testsPerOneMillionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_us_state);
        Intent intent = getIntent();
        USStatesData usStatesData = (USStatesData) intent.getExtras().getSerializable("USState");
        stateTextView = findViewById(R.id.stateTextView);
        casesTextView = findViewById(R.id.casesTextView);
        todayCasesTextView = findViewById(R.id.todayCasesTextView);
        deathsTextView = findViewById(R.id.deathsTextView);
        todaydeathsTextView = findViewById(R.id.todayDeathsTextView);
        activeTextView = findViewById(R.id.activeTextView);
        testsTextView = findViewById(R.id.testTextView);
        testsPerOneMillionTextView = findViewById(R.id.testsPerOneMillionTextView);

        stateTextView.setText(usStatesData.getState());
        casesTextView.setText(formatNumber(usStatesData.getCases()));
        todayCasesTextView.setText(formatNumber(usStatesData.getTodayCases()));
        deathsTextView.setText(formatNumber(usStatesData.getDeaths()));
        deathsTextView.setText(formatNumber(usStatesData.getTodayDeaths()));
        activeTextView.setText(formatNumber(usStatesData.getActive()));
        testsTextView.setText(formatNumber(usStatesData.getTests()));
        testsPerOneMillionTextView.setText(formatNumberTwoDecimalPlaces(usStatesData.getTestsPerOneMillion()));
    }

    public String formatNumber(String number) {
        if (number == null) {
            return "0";
        }
        double num = Double.parseDouble(number);
        return String.format(Locale.getDefault(), "%,.0f", num);
    }

    public String formatNumberTwoDecimalPlaces(String number) {
        if (number == null) {
            return "0";
        }
        double num = Double.parseDouble(number);
        return String.format(Locale.getDefault(), "%,.2f", num);
    }
}
