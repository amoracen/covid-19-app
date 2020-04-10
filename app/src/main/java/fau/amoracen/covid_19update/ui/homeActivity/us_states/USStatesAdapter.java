package fau.amoracen.covid_19update.ui.homeActivity.us_states;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.USStatesData;

public class USStatesAdapter extends RecyclerView.Adapter<USStatesAdapter.USStatesAdapterViewHolder> implements Filterable {
    private List<USStatesData> usStates;
    private ArrayList<USStatesData> usStatesFull;

    USStatesAdapter(List<USStatesData> usStates) {
        this.usStates = usStates;
        this.usStatesFull = new ArrayList<>(usStates);
    }

    @NonNull
    @Override
    public USStatesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.us_state_summary_row, parent, false);
        return new USStatesAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull USStatesAdapterViewHolder holder, int position) {
        USStatesData usState = usStates.get(position);
        holder.bind(usState);
    }

    @Override
    public int getItemCount() {
        return usStates.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<USStatesData> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(usStatesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (USStatesData item : usStatesFull) {
                    if (item.getState().toLowerCase().contains(filterPattern)) {
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
            usStates.clear();
            usStates.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class USStatesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stateTextView;
        TextView casesTextView;
        TextView todayCasesTextView;
        TextView deathsTextView;
        TextView todaydeathsTextView;
        TextView activeTextView;
        TextView testsTextView;
        TextView testsPerOneMillionTextView;


        USStatesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            casesTextView = itemView.findViewById(R.id.casesTextView);
            todayCasesTextView = itemView.findViewById(R.id.todayCasesTextView);
            deathsTextView = itemView.findViewById(R.id.deathsTextView);
            todaydeathsTextView = itemView.findViewById(R.id.todayDeathsTextView);
            activeTextView = itemView.findViewById(R.id.activeTextView);
            testsTextView = itemView.findViewById(R.id.testTextView);
            testsPerOneMillionTextView = itemView.findViewById(R.id.testsPerOneMillionTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            USStatesData usState = usStates.get(position);
            Toast.makeText(itemView.getContext(), "TODO", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(view.getContext(), USStatesActivity.class);
            intent.putExtra("USState", usState);
            view.getContext().startActivity(intent);
        }

        void bind(USStatesData usStatesData) {
            stateTextView.setText(usStatesData.getState());
            casesTextView.setText(formatNumber(usStatesData.getCases()));
            todayCasesTextView.setText(formatNumber(usStatesData.getTodayCases()));
            deathsTextView.setText(formatNumber(usStatesData.getDeaths()));
            todaydeathsTextView.setText(formatNumber(usStatesData.getTodayDeaths()));
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
}
