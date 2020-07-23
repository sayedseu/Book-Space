package com.example.bookspace.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.adapter.DonateAdapter;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.model.local.repository.LocalDataRepository;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.home.DonateViewModel;

public class DonateFragment extends Fragment {
    private static final String TAG = "sayed";
    private DonateViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LocalDataRepository localDataRepository;
    private DonateAdapter adapter;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private TextView emptyList;

    public static DonateFragment newInstance() {
        return new DonateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_donate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.donateRecId);
        progressBar = view.findViewById(R.id.donateProgressBar);
        navController = Navigation.findNavController(view);
        emptyList = view.findViewById(R.id.emptyListTV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        mViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(DonateViewModel.class);
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        adapter = new DonateAdapter(sharedViewModel, navController);

        subscribeObserver();
        initRecyclerView();
    }

    private void subscribeObserver() {
        mViewModel.observeBookList().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        Log.i(TAG, "subscribeObserver: loading");
                        emptyList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        Log.i(TAG, "subscribeObserver: error");
                        progressBar.setVisibility(View.GONE);
                        emptyList.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        assert (resource.data != null);
                        Log.i(TAG, "subscribeObserver: empty");
                        if (resource.data.isEmpty()) emptyList.setVisibility(View.VISIBLE);
                        else {
                            Log.i(TAG, "subscribeObserver: success");
                            emptyList.setVisibility(View.GONE);
                            adapter.setData(resource.data);
                        }
                        break;
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(adapter);
    }
}
