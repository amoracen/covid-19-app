package fau.amoracen.covid_19update.ui.homeActivity.ourWorldInData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fau.amoracen.covid_19update.R;

/**
 * Manages multiple WebView Graphs
 */
public class WorldInDataFragment extends Fragment {
    private RecyclerView graphsRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.our_world_in_data_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graphsRecyclerView = view.findViewById(R.id.graphsRecyclerView);
        ArrayList<String> graphs = new ArrayList<>();
        /*Total Confirmed Cases World*/
        graphs.add("grapher/total-cases-covid-19");
        /*Daily confirmed COVID-19 cases*/
        graphs.add("grapher/daily-covid-cases-3-day-average?country=FRA+ESP+GBR+USA");
        /*Daily confirmed COVID-19 deaths*/
        graphs.add("grapher/daily-deaths-covid-19?country=USA+OWID_WRL");
        /*Daily confirmed COVID-19 cases per million people*/
        graphs.add("grapher/new-covid-cases-per-million?region=NorthAmerica");
        /*Daily confirmed COVID-19 deaths per million people,*/
        graphs.add("grapher/new-covid-deaths-per-million?region=NorthAmerica");
        /*Number of COVID-19 tests per confirmed case*/
        graphs.add("grapher/number-of-covid-19-tests-per-confirmed-case");
        /*Daily and total confirmed COVID-19 deaths, World*/
        graphs.add("grapher/total-daily-covid-deaths?yScale=log");
        updateUI(graphs);
    }


    private void updateUI(ArrayList<String> graphs) {
        WorldInDataAdapter adapter = new WorldInDataAdapter(graphs);
        graphsRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        graphsRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
