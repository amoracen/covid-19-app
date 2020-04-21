package fau.amoracen.covid_19update.ui.homeActivity.graph;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Locale;

import fau.amoracen.covid_19update.R;

/**
 * Creates a Pie Chart using active cases, recovered cases, and deaths
 */
public class PieChartFragment extends Fragment {

    private PieChart pieChart;
    private ArrayList<PieEntry> yValue;
    private ArrayList<Integer> colors;
    private ArrayList<LegendEntry> legendEntries;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = view.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(2, 2, 2, 35);
        pieChart.setDragDecelerationFrictionCoef(0.90f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(getResources().getColor(R.color.colorLightBackground));
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.colorDarkGreen));
        pieChart.setRotationEnabled(false);
        pieChart.animateX(1000, Easing.EaseInOutBack);
        //Pie Chart Values
        yValue = new ArrayList<>();
        colors = new ArrayList<>();
        legendEntries = new ArrayList<>();
    }

    /**
     * Set Pie Entries
     *
     * @param cases a string representing the type of case
     * @param label a string used for the legend
     * @param color an integer representing the color
     */
    private void setPieEntry(String cases, String label, int color) {
        yValue.add(new PieEntry(Float.parseFloat(cases)));
        colors.add(color);
        LegendEntry legendEntry = new LegendEntry();
        legendEntry.label = label + ": \n" + formatNumber(cases);
        legendEntry.formColor = color;
        legendEntry.formSize = 15f;
        legendEntries.add(legendEntry);
    }

    /**
     * Create a Pie Chart
     *
     * @param context     application context
     * @param activeCases a string
     * @param recovered   a string
     * @param deaths      a string
     */
    public void createPieChart(Context context, String activeCases, String recovered, String deaths) {
        pieChart.clear();
        yValue.clear();
        colors.clear();
        legendEntries.clear();

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(15f);
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(true);
        legend.setXEntrySpace(25f);
        legend.setYEntrySpace(5f);
        //Active Cases
        setPieEntry(activeCases, "Active", context.getResources().getColor(R.color.holo_orange_light));
        //Recovered Cases
        setPieEntry(recovered, "Recovered", context.getResources().getColor(R.color.holo_green_light));
        //Deaths
        setPieEntry(deaths, "Deaths", context.getResources().getColor(R.color.holo_red_light));

        legend.setCustom(legendEntries);
        /*Description description = new Description();
        description.setText("This is an example fo a description");
        description.setTextSize(15f);
        pieChart.setDescription(description);*/
        PieDataSet dataSet = new PieDataSet(yValue, "");
        dataSet.setSliceSpace(4);
        dataSet.setHighlightEnabled(true);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(getResources().getColor(R.color.colorDarkGreen));
        data.setValueFormatter(new PercentFormatter(pieChart));


        pieChart.setData(data);
        pieChart.setVisibility(View.VISIBLE);
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
