package fau.amoracen.covid_19update.ui.homeActivity.graph;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fau.amoracen.covid_19update.R;

/**
 * Creates a Bar Chart
 */
public class BarChartAdapter extends RecyclerView.Adapter<BarChartAdapter.BarChartAdapterViewHolder> {

    private ArrayList<ArrayList> allCases;
    private List<String> xAxisList;

    /**
     * Constructor
     *
     * @param allCases a list of all cases
     */
    BarChartAdapter(ArrayList<ArrayList> allCases, List<String> xAxisList) {
        this.allCases = allCases;
        this.xAxisList = xAxisList;
    }

    @NonNull
    @Override
    public BarChartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_bar_chart_list_item, parent, false);
        return new BarChartAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BarChartAdapterViewHolder holder, int position) {
        ArrayList data = allCases.get(position);
        String type;
        if (position == 0) {
            type = "New Daily Cases";
        } else if (position == 1) {
            type = "New Daily Deaths";
        } else if (position == 2) {
            type = "Total Cases";
        } else {
            type = "Total Deaths";
        }
        holder.bind(type, xAxisList, data);
    }

    @Override
    public int getItemCount() {
        return allCases.size();
    }

    /**
     * Manages the Bar Chart, used to set the values from the Cases list
     */
    static class BarChartAdapterViewHolder extends RecyclerView.ViewHolder implements OnChartValueSelectedListener {
        private BarChart barChart;
        private TextView titleTextView, selectedTextView;
        private List<String> xAxisList;
        private ArrayList caseData;
        private View view;

        /**
         * Constructor
         *
         * @param itemView a chart in the list
         */
        BarChartAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            barChart = itemView.findViewById(R.id.barChart);
            selectedTextView = itemView.findViewById(R.id.selectedTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            barChart.getDescription().setEnabled(false);
            barChart.setDrawGridBackground(false);
            barChart.getAxisRight().setEnabled(false);
            barChart.setPinchZoom(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
            barChart.setTouchEnabled(true);
            barChart.setOnChartValueSelectedListener(this);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(true);

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(15f);
        }

        /**
         * Add Data to the bar Chart
         *
         * @param caseData a List object
         */
        void bind(String type, List<String> xAxisList, ArrayList caseData) {
            this.xAxisList = xAxisList;
            this.caseData = caseData;
            titleTextView.setText(type);

            BarDataSet set1 = new BarDataSet(caseData, type);
            if (type.equals("New Daily Cases") || type.equals("Total Cases")) {
                set1.setColors(view.getResources().getColor(R.color.holo_orange_light));
                set1.setHighLightColor(view.getResources().getColor(R.color.holo_red_light));
            } else if (type.equals("New Daily Deaths") || type.equals("Total Deaths")) {
                set1.setColors(view.getResources().getColor(R.color.holo_red_light));
                set1.setHighLightColor(Color.BLACK);
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.70f);
            data.setDrawValues(false);

            MyXAxisValueFormatter myXAxisValueFormatter = new MyXAxisValueFormatter(xAxisList);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(myXAxisValueFormatter);
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

            barChart.setData(data);
            barChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
            barChart.setFitBars(true);
            barChart.animateXY(1000, 1000);
            barChart.setVisibility(View.VISIBLE);
        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            int position = caseData.indexOf(e);
            selectedTextView.setText("Date: " + xAxisList.get(position) + " Cases: " + formatNumber(String.valueOf(e.getY())));
            selectedTextView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNothingSelected() {

        }

        /**
         * Format Value
         *
         * @param number a string
         * @return string of the formatted value
         */
        private String formatNumber(String number) {
            if (number == null) return "0";
            return String.format(Locale.getDefault(), "%,.0f", Double.parseDouble(number));
        }


    }

    /**
     * Custom Class to set X Axis Labels
     */
    public static class MyXAxisValueFormatter extends IndexAxisValueFormatter {
        List<String> values;

        MyXAxisValueFormatter(List<String> values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return String.valueOf(values.get((int) value));
        }
    }

}
