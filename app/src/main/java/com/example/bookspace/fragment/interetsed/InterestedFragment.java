package com.example.bookspace.fragment.interetsed;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.adapter.InterestedAdapter;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.SessionManager;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.view_model.interested.InterestedViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;


public class InterestedFragment extends Fragment {
    private static final String TAG = "sayed";
    private InterestedViewModel interestedViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyList;
    private InterestedAdapter adapter;
    private SessionManager sessionManager;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interested, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.interestedRec);
        progressBar = view.findViewById(R.id.interestedProgressBar);
        emptyList = view.findViewById(R.id.interestedEmptyTV);
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        interestedViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(InterestedViewModel.class);
        sessionManager = SessionManager.getInstance(requireContext());
        adapter = new InterestedAdapter(interestedViewModel, requireContext());
        initRecyclerView();
        subscribeObserver();
        subscribeDeletingObserver();
    }


    private void subscribeObserver() {
        assert (sessionManager.getUserDetails() != null);
        interestedViewModel.getInterestedList(sessionManager.getUserDetails().getUserId()).observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressBar(true);
                        break;
                    case ERROR:
                        showProgressBar(false);
                        showEmptyList(true);
                        adapter.setList(null);
                        break;
                    case SUCCESS:
                        showProgressBar(false);
                        assert (resource.data != null);
                        if (resource.data.isEmpty()) {
                            adapter.setList(null);
                            showEmptyList(true);
                        } else {
                            showEmptyList(false);
                            adapter.setList(resource.data);
                        }
                        break;
                }
            }
        });
    }

    private void subscribeDeletingObserver() {
        interestedViewModel.observerDeleting().removeObservers(getViewLifecycleOwner());
        interestedViewModel.observerDeleting().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        showSnackBar("Something went wrong. Please try again later.");
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        assert (resource.data != null);
                        if (resource.data) {
                            showSnackBar("Deleted successful!");
                            subscribeObserver();
                            interestedViewModel.clearObserver();
                        } else showSnackBar("Something went wrong. Please try again later.");
                        break;
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }

    private void showProgressBar(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showEmptyList(boolean isShow) {
        if (isShow) {
            emptyList.setVisibility(View.VISIBLE);
        } else {
            emptyList.setVisibility(View.GONE);
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(Objects.requireNonNull(this.getView()), message, Snackbar.LENGTH_SHORT).show();
    }

    private void initProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
    }

    private void showProgressDialog(boolean isShow) {
        if (isShow) alertDialog.show();
        else {
            if (alertDialog.isShowing()) alertDialog.dismiss();
        }
    }

}
