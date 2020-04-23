package fau.amoracen.covid_19update.ui.homeActivity.news;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import java.util.Arrays;
import java.util.List;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.data.NewsArticles;

/**
 * NewsAdapter manages the list of articles
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> implements Filterable {
    private List<NewsArticles.Articles> articles;
    private ArrayList<NewsArticles.Articles> articlesFull;

    /**
     * Constructor
     *
     * @param articles a list of articles
     */
    NewsAdapter(NewsArticles.Articles[] articles) {
        this.articles = Arrays.asList(articles);
        this.articlesFull = new ArrayList<>(this.articles);
    }

    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_fragment_row, parent, false);
        return new NewsAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        NewsArticles.Articles article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    /**
     * Filter the title for the search button
     */
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NewsArticles.Articles> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(articlesFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (NewsArticles.Articles item : articlesFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
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
            articles.clear();
            articles.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    /**
     * Manages the new layout, used to set the values from the Articles class
     */
    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleTextView, authorTextView, publishedTextView, descriptionTextView, content;
        private ImageView imageView;
        private View itemView;

        /**
         * Constructor
         *
         * @param itemView a row in the table
         */
        NewsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            publishedTextView = itemView.findViewById(R.id.publishedTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            content = itemView.findViewById(R.id.contentTextView);
            imageView = itemView.findViewById(R.id.newsImageView);
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
            NewsArticles.Articles article = articles.get(position);
            Intent intent = new Intent(view.getContext(), NewsActivity.class);
            intent.putExtra("News", article);
            view.getContext().startActivity(intent);
        }

        /**
         * Add Data to the row
         *
         * @param articles an Articles object
         */
        void bind(NewsArticles.Articles articles) {
            showImage(articles.getUrlToImage());
            titleTextView.setText(articles.getTitle());
            if (articles.getAuthor() == null) {
                authorTextView.setVisibility(View.GONE);
            } else {
                authorTextView.setText(itemView.getContext().getString(R.string.author, articles.getAuthor()));
            }
            if (articles.getPublishedAt() == null) {
                publishedTextView.setVisibility(View.GONE);
            } else {
                publishedTextView.setText(itemView.getContext().getString(R.string.published_1_s, articles.getPublishedAt()));
            }
            if (articles.getDescription() == null || articles.getDescription().isEmpty()) {
                descriptionTextView.setVisibility(View.GONE);
            } else {
                descriptionTextView.setText(articles.getDescription());
            }
            if (articles.getContent() == null || articles.getContent().isEmpty()) {
                content.setVisibility(View.GONE);
            } else {
                content.setText(articles.getContent());
            }
        }

        /**
         * Show Image using Picasso library
         *
         * @param url URL
         */
        private void showImage(String url) {
            if (url != null && !url.isEmpty()) {
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.get().load(url).resize(width, width * 2 / 3).centerInside().into(imageView);
            }
        }
    }
}
