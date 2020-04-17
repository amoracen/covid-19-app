package fau.amoracen.covid_19update.ui.homeActivity.us_states;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.USStatesData;

/**
 * USStatesAdapter manages the list of states
 */
public class USStatesAdapter extends RecyclerView.Adapter<USStatesAdapter.USStatesAdapterViewHolder> implements Filterable {
    private List<USStatesData> usStates;
    private ArrayList<USStatesData> usStatesFull;

    /**
     * Constructor
     *
     * @param usStates a list of states
     */
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

    /**
     * Filter the state name for the search button
     */
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

    /**
     * Manages the table row, used to set the values from the USStatesData class
     */
    public class USStatesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView stateTextView, casesTextView, todayCasesTextView, deathsTextView;
        private TextView todayDeathsTextView, activeTextView, testsTextView, testsPerOneMillionTextView;

        /**
         * Constructor
         *
         * @param itemView a row in the table
         */
        USStatesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            casesTextView = itemView.findViewById(R.id.casesTextView);
            todayCasesTextView = itemView.findViewById(R.id.todayCasesTextView);
            deathsTextView = itemView.findViewById(R.id.deathsTextView);
            todayDeathsTextView = itemView.findViewById(R.id.todayDeathsTextView);
            activeTextView = itemView.findViewById(R.id.activeTextView);
            testsTextView = itemView.findViewById(R.id.testTextView);
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
            USStatesData usState = usStates.get(position);
            Intent intent = new Intent(view.getContext(), USStatesActivity.class);
            intent.putExtra("USState", usState);
            view.getContext().startActivity(intent);
        }

        /**
         * Add Data to the row
         *
         * @param usStatesData a USStatesData object
         */
        void bind(USStatesData usStatesData) {
            stateTextView.setText(usStatesData.getState());
            casesTextView.setText(usStatesData.getCases());
            todayCasesTextView.setText(usStatesData.getTodayCases());
            deathsTextView.setText(usStatesData.getDeaths());
            todayDeathsTextView.setText(usStatesData.getTodayDeaths());
            activeTextView.setText(usStatesData.getActive());
            testsTextView.setText(usStatesData.getTests());
            testsPerOneMillionTextView.setText(usStatesData.getTestsPerOneMillion());
        }
    }
}
