package com.example.bookspace.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspace.R;
import com.example.bookspace.adapter.SearchAdapter;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.search.SearchViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchFragment extends Fragment {
    private static final String TAG = "sayed";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private NavController navController;
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView searchResult;
    private ImageView upButton;
    private SearchViewModel searchViewModel;
    private SearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        searchView = view.findViewById(R.id.searchView);
        upButton = view.findViewById(R.id.upButton);
        searchResult = view.findViewById(R.id.searchResultTV);
        recyclerView = view.findViewById(R.id.searchRec);
        progressBar = view.findViewById(R.id.searchProgressBar);
        initSearchView();
        initBackPressListener();
        initRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        searchViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(SearchViewModel.class);
        SharedViewModel sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        adapter = new SearchAdapter(navController, this, sharedViewModel);
        initRecyclerView();
        subscribeObserver();
        subscribeSearchObservable();
    }

    private void subscribeObserver() {
        searchViewModel.retrieve().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressBar(true);
                        break;
                    case ERROR:
                        showProgressBar(false);
                        break;
                    case SUCCESS:
                        showProgressBar(false);
                        assert (resource.data != null);
                        if (!resource.data.isEmpty()) {
                            adapter.setData(resource.data);
                        }
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

    public void showProgressBar(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showSearchResult(boolean isShow, String text) {
        if (isShow) {
            searchResult.setVisibility(View.VISIBLE);
            searchResult.setText(text);
        } else {
            searchResult.setVisibility(View.GONE);
        }
    }

    private void initBackPressListener() {
        upButton.setOnClickListener(click -> {
            searchView.onActionViewCollapsed();
            navController.popBackStack();
        });
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.popBackStack();
                    }
                });
    }

    private void initSearchView() {
        searchView.setQueryHint("Search Books");
        searchView.onActionViewExpanded();
    }

    private Observable<String> getSearchObservable() {
        PublishSubject<String> subject = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return true;
            }
        });
        return subject;
    }

    private void subscribeSearchObservable() {
        disposable.add(getSearchObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<String>>) Observable::just)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    adapter.getFilter().filter(s);
                    showProgressBar(true);
                }));
    }


    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }
}
