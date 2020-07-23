package com.example.bookspace.fragment.home;

import android.os.Bundle;
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
import com.example.bookspace.adapter.ExchangeAdapter;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.model.local.repository.LocalDataRepository;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.home.ExchangeViewModel;

public class ExchangeFragment extends Fragment {
    private ExchangeViewModel mViewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LocalDataRepository localDataRepository;
    private ExchangeAdapter adapter;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private TextView emptyList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.exchangeRecId);
        progressBar = view.findViewById(R.id.exchangeProgressBar);
        navController = Navigation.findNavController(view);
        emptyList = view.findViewById(R.id.emptyListTV);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        mViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(ExchangeViewModel.class);
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        adapter = new ExchangeAdapter(sharedViewModel, navController);

        subscribeObserver();
        initRecyclerView();
    }

    private void subscribeObserver() {
        mViewModel.observeBookList().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        emptyList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        emptyList.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        assert (resource.data != null);
                        if (resource.data.isEmpty()) emptyList.setVisibility(View.VISIBLE);
                        else {
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
