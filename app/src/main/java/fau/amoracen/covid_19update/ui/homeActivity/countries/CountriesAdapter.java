package fau.amoracen.covid_19update.ui.homeActivity.countries;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;

/**
 * CountriesAdapter manages the list of countries
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesAdapterViewHolder> implements Filterable {
    private List<CountryData> countries;
    private ArrayList<CountryData> countriesFull;

    /**
     * Constructor
     *
     * @param countries a list of countries
     */
    CountriesAdapter(List<CountryData> countries) {
        this.countries = countries;
        this.countriesFull = new ArrayList<>(countries);
    }

    @NonNull
    @Override
    public CountriesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.country_summary_row, parent, false);
        return new CountriesAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesAdapterViewHolder holder, int position) {
        CountryData country = countries.get(position);
        holder.bind(country);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    /**
     * Filter the country name for the search button
     */
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<CountryData> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(countriesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (CountryData item : countriesFull) {
                    if (item.getCountry().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            countries.clear();
            countries.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    /**
     * Manages the table row, used to set the values from the Country class
     */
    public class CountriesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView CountryTextView, TotalConfirmedTextView, NewConfirmedTextView, TotalRecoveredTextView;
        private TextView ActiveTextView, TotalDeathsTextView, NewDeathsTextView, CriticalTextView;
        private TextView casesPerOneMillionTextView, deathsPerOneMillionTextView, testTextView, testsPerOneMillionTextView;
        private ImageView imageView;

        /**
         * Constructor
         *
         * @param itemView a row in the table
         */
        CountriesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            CountryTextView = itemView.findViewById(R.id.CountryTextView);
            NewConfirmedTextView = itemView.findViewById(R.id.todayCasesTextView);
            TotalConfirmedTextView = itemView.findViewById(R.id.CasesTextView);
            TotalRecoveredTextView = itemView.findViewById(R.id.TotalRecoveredTextView);
            ActiveTextView = itemView.findViewById(R.id.activeTextView);
            TotalDeathsTextView = itemView.findViewById(R.id.deathsTextView);
            NewDeathsTextView = itemView.findViewById(R.id.todayDeathsTextView);
            CriticalTextView = itemView.findViewById(R.id.criticalTextView);
            casesPerOneMillionTextView = itemView.findViewById(R.id.casesPerOneMillionTextView);
            deathsPerOneMillionTextView = itemView.findViewById(R.id.deathsPerOneMillionTextView);
            testTextView = itemView.findViewById(R.id.testTextView);
            testsPerOneMillionTextView = itemView.findViewById(R.id.testsPerOneMillionTextView);
            itemView.setOnClickListener(this);
        }

        /**
         * Listener, Starts Intent
         *
         * @param view a row in the table
         */
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            CountryData country = countries.get(position);
            Intent intent = new Intent(view.getContext(), CountryActivity.class);
            intent.putExtra("Country", country);
            view.getContext().startActivity(intent);
        }

        /**
         * Add Data to the row
         *
         * @param country a CountryData object
         */
        void bind(CountryData country) {
            showImage(country.getCountryInfo().getFlag());
            String countryName = country.getCountry();
            if (countryName.length() > 10) {
                countryName = country.getCountryInfo().getIso3();
                if (countryName == null || countryName.isEmpty()) {
                    countryName = country.getCountry().substring(0, 9);
                }
            }
            CountryTextView.setText(countryName);
            NewConfirmedTextView.setText(country.getTodayCases());
            TotalConfirmedTextView.setText(country.getCases());
            TotalRecoveredTextView.setText(country.getRecovered());
            ActiveTextView.setText(country.getActive());
            TotalDeathsTextView.setText(country.getDeaths());
            NewDeathsTextView.setText(country.getTodayDeaths());
            CriticalTextView.setText(country.getCritical());
            casesPerOneMillionTextView.setText(country.getCasesPerOneMillion());
            deathsPerOneMillionTextView.setText(country.getDeathsPerOneMillion());
            testTextView.setText(country.getTests());
            testsPerOneMillionTextView.setText(country.getTestsPerOneMillion());
        }

        /**
         * Show Image using Picasso library
         *
         * @param flag URL of the flag
         */
        private void showImage(String flag) {
            if (flag != null && !flag.isEmpty()) {
                Picasso.get().load(flag).resize(60, 60).centerInside().into(imageView);
            }
        }
    }
}
