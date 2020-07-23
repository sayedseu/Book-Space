package com.example.bookspace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.custom.ImageRoundCorners;
import com.example.bookspace.model.model_class.remote.BookInfo;
import com.example.bookspace.model.model_class.remote.Interested;
import com.example.bookspace.view_model.interested.InterestedViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InterestedAdapter extends RecyclerView.Adapter<InterestedAdapter.InterestedViewHolder> {
    private List<Interested> list;
    private InterestedViewModel viewModel;
    private Context context;

    public InterestedAdapter(InterestedViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public InterestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_interested_item, parent, false);
        return new InterestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestedViewHolder holder, int position) {
        if (list != null) {
            Interested interested = list.get(position);
            BookInfo data = interested.getBookDetails().getBookInfo();
            if (data != null) {
                holder.bookName.setText(data.getBookName());
                holder.authorName.setText(data.getBookAuthorName());
                holder.mode.setText(interested.getBookDetails().getBookMode().getBookMode());
                if (data.getImageLocation1() != null) {
                    holder.loadPhoto(data.getImageLocation1());
                } else if (data.getImageLocation2() != null) {
                    holder.loadPhoto(data.getImageLocation2());
                } else if (data.getImageLocation3() != null) {
                    holder.loadPhoto(data.getImageLocation3());
                }
            }
            holder.deleteIcon.setOnClickListener(click -> {
                showDialog(interested.getId());
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public void setList(List<Interested> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private void showDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Do yun want to delete this.");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            viewModel.deleteInterestedBook(id);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    static class InterestedViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName, authorName, mode;
        private ImageView bookPhoto, deleteIcon;
        private ProgressBar progressBar;

        InterestedViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.interestedBookNameTV);
            authorName = itemView.findViewById(R.id.interestedAuthorNameTV);
            mode = itemView.findViewById(R.id.interestedModeTV);
            bookPhoto = itemView.findViewById(R.id.interestedPhotoIV);
            deleteIcon = itemView.findViewById(R.id.interedtedDeleteIconIV);
            progressBar = itemView.findViewById(R.id.interestedProgressBar);
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
