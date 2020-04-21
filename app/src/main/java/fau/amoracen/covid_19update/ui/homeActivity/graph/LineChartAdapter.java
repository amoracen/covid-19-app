package fau.amoracen.covid_19update.ui.homeActivity.graph;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fau.amoracen.covid_19update.R;

/**
 * LineChartAdapter manages the list of charts
 */
public class LineChartAdapter extends RecyclerView.Adapter<LineChartAdapterViewHolder> {
    private ArrayList<ArrayList> allCases;
    private List<String> xAxisList;

    /**
     * Constructor
     *
     * @param allCases a list of all cases
     */
    LineChartAdapter(ArrayList<ArrayList> allCases, List<String> xAxisList) {
        this.allCases = allCases;
        this.xAxisList = xAxisList;
    }

    @NonNull
    @Override
    public LineChartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.line_chart_row, parent, false);
        return new LineChartAdapterViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull LineChartAdapterViewHolder holder, int position) {
        ArrayList data = allCases.get(position);
        String type;
        if (position == 0) {
            type = "Cases";
        } else if (position == 1) {
            type = "Recovered";
        } else {
            type = "Deaths";
        }
        holder.bind(type, xAxisList, data);
    }

    @Override
    public int getItemCount() {
        return allCases.size();
    }
}

/**
 * Manages the Chart, used to set the values from the Cases list
 */
class LineChartAdapterViewHolder extends RecyclerView.ViewHolder implements OnChartValueSelectedListener {
    private LineChart lineChart;
    private TextView titleTextView, selectedTextView;
    private View view;
    private List<String> xAxisList;
    private ArrayList caseData;

    /**
     * Constructor
     *
     * @param itemView a chart in the list
     */
    LineChartAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        lineChart = itemView.findViewById(R.id.lineChart);
        selectedTextView = itemView.findViewById(R.id.selectedTextView);
        titleTextView = itemView.findViewById(R.id.titleTextView);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setLabelCount(6, true);
        lineChart.setOnChartValueSelectedListener(this);
    }

    /**
     * Add Data to the line Chart
     *
     * @param caseData a List object
     */
    void bind(String type, List<String> xAxisList, ArrayList caseData) {
        this.xAxisList = xAxisList;
        this.caseData = caseData;
        titleTextView.append(" " + type);

        LineDataSet set1 = new LineDataSet(caseData, "Cases");
        set1.setFillAlpha(100);
        set1.setFillColor(view.getResources().getColor(R.color.holo_orange_light));
        set1.setColor(view.getResources().getColor(R.color.holo_red_light));
        set1.setCircleColor(view.getResources().getColor(R.color.colorDarkGreen));
        set1.setDrawCircles(true);
        set1.setDrawCircleHole(true);
        set1.setLineWidth(4f);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setDrawValues(true);
        set1.setDrawHighlightIndicators(true);
        set1.setHighLightColor(Color.RED);
        set1.setHighlightLineWidth(3f);
        set1.setFormSize(15.f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        data.setDrawValues(false);

        lineChart.setData(data);
        lineChart.invalidate();

        MyXAxisValueFormatter myXAxisValueFormatter = new MyXAxisValueFormatter(xAxisList);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(myXAxisValueFormatter);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.setVisibility(View.VISIBLE);
    }

    /**
     * Listener
     *
     * @param e entry in the chart
     * @param h none
     */
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

