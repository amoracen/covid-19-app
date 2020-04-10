package fau.amoracen.covid_19update.ui.homeActivity.countries;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;

public class CountryActivity extends AppCompatActivity {
    TextView CountryTextView;
    TextView NewConfirmedTextView;
    TextView TotalConfirmedTextView;
    TextView NewDeathsTextView;
    TextView TotalDeathsTextView;
    TextView NewRecoveredTextView;
    TextView TotalRecoveredTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        Intent intent = getIntent();
        CountryData country = (CountryData) intent.getExtras().getSerializable("Country");
        imageView = findViewById(R.id.imageView);
        CountryTextView = findViewById(R.id.CountryTextView);
        TotalConfirmedTextView = findViewById(R.id.CasesTextView);
        TotalDeathsTextView = findViewById(R.id.deathsTextView);
        TotalRecoveredTextView = findViewById(R.id.TotalRecoveredTextView);

        CountryTextView.setText("Country: " + country.getCountry());
        TotalConfirmedTextView.setText("Total Confirmed: " + country.getCases());
        TotalDeathsTextView.setText("Total Deaths: " + country.getDeaths());
        TotalRecoveredTextView.setText("Total Recovered: " + country.getRecovered());
        showImage(country.getCountryInfo().getFlag());
    }

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width * 2 / 3).centerInside().into(imageView);
        }
    }
}
