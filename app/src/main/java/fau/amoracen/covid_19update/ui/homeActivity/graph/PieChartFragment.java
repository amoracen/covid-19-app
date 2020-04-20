package fau.amoracen.covid_19update.ui.homeActivity.graph;

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

public class PieChartFragment extends Fragment {

    private PieChart pieChart;

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
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.90f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.colorLightBackground));
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.colorDarkGreen));
    }

    public void createPieChart(String activeCases, String recovered, String deaths) {
        ArrayList<PieEntry> yValue = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        yValue.add(new PieEntry(Float.parseFloat(activeCases), formatNumber(activeCases)));
        colors.add(getResources().getColor(R.color.holo_orange_light));
        yValue.add(new PieEntry(Float.parseFloat(recovered), formatNumber(recovered)));
        colors.add(getResources().getColor(R.color.holo_green_light));
        yValue.add(new PieEntry(Float.parseFloat(deaths), formatNumber(deaths)));
        colors.add(getResources().getColor(R.color.holo_red_light));


        /*Description description = new Description();
        description.setText("This is an example fo a description");
        description.setTextSize(15f);
        pieChart.setDescription(description);*/
        pieChart.animateX(1000, Easing.EaseInOutBack);

        PieDataSet dataSet = new PieDataSet(yValue, "");
        //dataSet.setHighlightEnabled(true);
        //dataSet.setAutomaticallyDisableSliceSpacing(true);
        dataSet.setSliceSpace(4);
        dataSet.setSelectionShift(7f);
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(getResources().getColor(R.color.colorDarkGreen));

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setTextSize(14f);
        ArrayList<LegendEntry> legendEntries = new ArrayList<>();
        LegendEntry activeCasesLegend = new LegendEntry();
        activeCasesLegend.label = "Active Cases";
        activeCasesLegend.formColor = getResources().getColor(R.color.holo_orange_light);
        activeCasesLegend.formSize = 14;
        legendEntries.add(activeCasesLegend);
        LegendEntry recoveredLegend = new LegendEntry();
        recoveredLegend.label = "Recovered";
        recoveredLegend.formColor = getResources().getColor(R.color.holo_green_light);
        recoveredLegend.formSize = 14;
        legendEntries.add(recoveredLegend);
        LegendEntry deathsLegend = new LegendEntry();
        deathsLegend.label = "Deaths";
        deathsLegend.formColor = getResources().getColor(R.color.holo_red_light);
        deathsLegend.formSize = 14;
        legendEntries.add(deathsLegend);
        legend.setCustom(legendEntries);


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
