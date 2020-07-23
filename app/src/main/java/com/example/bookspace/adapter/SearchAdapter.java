package com.example.bookspace.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.custom.ImageRoundCorners;
import com.example.bookspace.fragment.search.SearchFragment;
import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.BookInfo;
import com.example.bookspace.view_model.SharedViewModel;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    private NavController navController;
    private SearchFragment searchFragment;
    private SharedViewModel sharedViewModel;
    private List<BookDetails> mainList;
    private List<BookDetails> searchedResultList = new ArrayList<>();
    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();

            if (mainList == null) {
                results.values = null;
                return results;
            } else if (mainList.isEmpty()) {
                results.values = null;
                return results;
            }

            List<BookDetails> filteredList = new ArrayList<>();

            String filterPattern = charSequence.toString().toLowerCase().trim();

            for (BookDetails bookDetails : mainList) {
                String bookName = bookDetails.getBookInfo().getBookName().toLowerCase().trim();
                String authorName = bookDetails.getBookInfo().getBookAuthorName().toLowerCase().trim();

                if (bookName.contains(filterPattern) || authorName.contains(filterPattern)) {
                    filteredList.add(bookDetails);
                }
            }
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values == null) {
                notifyResult();
                return;
            }
            searchedResultList.clear();
            searchedResultList.addAll((List) filterResults.values);
            notifyResult();
        }
    };

    public SearchAdapter(NavController navController, SearchFragment searchFragment, SharedViewModel sharedViewModel) {
        this.navController = navController;
        this.searchFragment = searchFragment;
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        if (searchedResultList != null) {
            BookDetails bookDetails = searchedResultList.get(position);
            BookInfo data = bookDetails.getBookInfo();
            if (data != null) {
                holder.bookName.setText(data.getBookName());
                holder.authorName.setText(data.getBookAuthorName());
                if (data.getImageLocation1() != null) {
                    holder.loadPhoto(data.getImageLocation1());
                } else if (data.getImageLocation2() != null) {
                    holder.loadPhoto(data.getImageLocation2());
                } else if (data.getImageLocation3() != null) {
                    holder.loadPhoto(data.getImageLocation3());
                }
            }
        }
        holder.cardView.setOnClickListener(click -> {
            sharedViewModel.setCurrentViewedBook(searchedResultList.get(position));
            navController.navigate(R.id.action_searchFragment_to_bookDetailsFragment);
        });
    }

    @Override
    public int getItemCount() {
        if (searchedResultList.isEmpty()) {
            return 0;
        }
        return searchedResultList.size();
    }

    public void setData(List<BookDetails> bookDetailsList) {
        mainList = bookDetailsList;
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private void notifyResult() {
        searchFragment.showProgressBar(false);
        if (searchedResultList.isEmpty()) {
            searchFragment.showSearchResult(true, "Nothing Found");
            notifyDataSetChanged();
        } else {
            searchFragment.showSearchResult(true, "Searching Result");
            notifyDataSetChanged();
        }
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName, authorName;
        private ImageView bookPhoto;
        private ProgressBar progressBar;
        private MaterialCardView cardView;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.searchBookNameTV);
            authorName = itemView.findViewById(R.id.searchAuthorNameTV);
            bookPhoto = itemView.findViewById(R.id.searchPhotoIV);
            progressBar = itemView.findViewById(R.id.searchImageProgressBar);
            cardView = itemView.findViewById(R.id.searchItemParentView);
        }

        void loadPhoto(String url) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(url)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.ic_warning)
                    .transform(new ImageRoundCorners())
                    .into(bookPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

}
