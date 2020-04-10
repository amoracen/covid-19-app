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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.CountryData;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesAdapterViewHolder> implements Filterable {
    private List<CountryData> countries;
    private ArrayList<CountryData> countriesFull;

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

    public class CountriesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView CountryTextView;
        TextView TotalConfirmedTextView;
        TextView NewConfirmedTextView;
        TextView TotalRecoveredTextView;
        TextView ActiveTextView;
        TextView TotalDeathsTextView;
        TextView NewDeathsTextView;
        TextView CriticalTextView;
        TextView casesPerOneMillionTextView;
        TextView deathsPerOneMillionTextView;
        TextView testTextView;
        TextView testsPerOneMillionTextView;
        ImageView imageView;


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

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            CountryData country = countries.get(position);
            Toast.makeText(itemView.getContext(), "TODO", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), CountryActivity.class);
             intent.putExtra("Country", country);
             view.getContext().startActivity(intent);
        }

        void bind(CountryData country) {
            CountryTextView.setText(country.getCountry());
            showImage(country.getCountryInfo().getFlag());
            NewConfirmedTextView.setText(formatNumber(country.getTodayCases()));
            TotalConfirmedTextView.setText(formatNumber(country.getCases()));
            TotalRecoveredTextView.setText(formatNumber(country.getRecovered()));
            ActiveTextView.setText(formatNumber(country.getActive()));
            TotalDeathsTextView.setText(formatNumber(country.getDeaths()));
            NewDeathsTextView.setText(formatNumber(country.getTodayDeaths()));
            CriticalTextView.setText(formatNumber(country.getCritical()));
            casesPerOneMillionTextView.setText(formatNumberTwoDecimalPlaces(country.getCasesPerOneMillion()));
            deathsPerOneMillionTextView.setText(formatNumberTwoDecimalPlaces(country.getDeathsPerOneMillion()));
            testTextView.setText(formatNumber(country.getTests()));
            testsPerOneMillionTextView.setText(formatNumberTwoDecimalPlaces(country.getTestsPerOneMillion()));
        }

        private void showImage(String flag) {
            if (flag != null && !flag.isEmpty()) {
                Picasso.get().load(flag).resize(30, 30).centerInside().into(imageView);
            }
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
}
