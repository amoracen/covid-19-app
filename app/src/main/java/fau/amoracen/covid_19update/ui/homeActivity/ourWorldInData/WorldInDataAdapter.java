package fau.amoracen.covid_19update.ui.homeActivity.ourWorldInData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fau.amoracen.covid_19update.R;

/**
 * WorldInDataAdapter manages the list of graphs
 */
public class WorldInDataAdapter extends RecyclerView.Adapter<WorldInDataAdapter.WorldInDataAdapterViewHolder> {
    private ArrayList<String> graphs;

    /**
     * Constructor
     *
     * @param graphs list of graphs
     */
    WorldInDataAdapter(ArrayList<String> graphs) {
        this.graphs = graphs;
    }

    @NonNull
    @Override
    public WorldInDataAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.our_world_in_data_fragment_row, parent, false);
        return new WorldInDataAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorldInDataAdapterViewHolder holder, int position) {
        String graph = graphs.get(position);
        holder.bind(graph);
    }

    @Override
    public int getItemCount() {
        return graphs.size();
    }

    /**
     * Manages the new layout, used to set the values for the WebView
     */
    static class WorldInDataAdapterViewHolder extends RecyclerView.ViewHolder {
        private WebView webView;
        private int height, width;

        /**
         * Constructor
         *
         * @param itemView a
         */
        @SuppressLint("SetJavaScriptEnabled")
        WorldInDataAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
            webView.setInitialScale(1);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i("OverrideUrlLoading", "True");
                    return true;
                }
            });
            DisplayMetrics displaymetrics = itemView.getContext().getResources().getDisplayMetrics();
            height = displaymetrics.heightPixels;
            height = 550;
            width = displaymetrics.widthPixels;
            Log.i("Height", String.valueOf(height));
            Log.i("Width", String.valueOf(width));
        }

        /**
         * Set WebView Link
         *
         * @param graph a string
         */
        void bind(String graph) {
            String currentURL = "<!DOCTYPE html><html> <head> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> " +
                    "<link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> " +
                    "<body style=\"background:white;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe style=\"background:white;\" width=' " +
                    width + "' height='" + height + "' src=\"" + graph + "\" frameborder=\"10\"></iframe> </body> </html> ";
            webView.loadDataWithBaseURL("https://ourworldindata.org/", currentURL, "text/html", "UTF-8", null);
        }
    }
}
