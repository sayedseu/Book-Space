package com.example.bookspace.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.custom.ImageRoundCorners;
import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.BookInfo;
import com.example.bookspace.view_model.SharedViewModel;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryBookViewerAdapter extends RecyclerView.Adapter<CategoryBookViewerAdapter.CategoryBookViewHolder> {
    private List<BookDetails> bookDetailsList;
    private SharedViewModel viewModel;
    private NavController navController;

    public CategoryBookViewerAdapter(SharedViewModel viewModel, NavController navController) {
        this.viewModel = viewModel;
        this.navController = navController;
    }


    @NonNull
    @Override
    public CategoryBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book_card, parent, false);
        return new CategoryBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryBookViewHolder holder, int position) {
        if (bookDetailsList != null) {
            BookInfo bookInfo = bookDetailsList.get(position).getBookInfo();

            if (bookInfo.getImageLocation1() != null) {
                holder.showImage(bookInfo.getImageLocation1());
            } else if (bookInfo.getImageLocation2() != null) {
                holder.showImage(bookInfo.getImageLocation2());
            } else if (bookInfo.getImageLocation3() != null) {
                holder.showImage(bookInfo.getImageLocation3());
            }

            holder.bookName.setText(bookInfo.getBookName());

            holder.cardView.setOnClickListener(view -> {
                viewModel.setCurrentViewedBook(bookDetailsList.get(position));
                navController.navigate(R.id.action_categoryDetails_to_bookDetailsFragment);
            });
        }
    }


    @Override
    public int getItemCount() {
        if (bookDetailsList == null) {
            return 0;
        } else {
            return bookDetailsList.size();
        }
    }

    public void setData(List<BookDetails> bookDetailsList) {
        this.bookDetailsList = bookDetailsList;
        notifyDataSetChanged();
    }


    static class CategoryBookViewHolder extends RecyclerView.ViewHolder {
        private ImageView bookImage;
        private TextView bookName;
        private ProgressBar progressBar;
        private MaterialCardView cardView;

        CategoryBookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.nameTV);
            bookImage = itemView.findViewById(R.id.bookIV);
            progressBar = itemView.findViewById(R.id.imageLoaderPogressBar);
            cardView = itemView.findViewById(R.id.parent);
        }

        void showImage(String url) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(url)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.ic_warning)
                    .transform(new ImageRoundCorners())
                    .into(bookImage, new Callback() {
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
