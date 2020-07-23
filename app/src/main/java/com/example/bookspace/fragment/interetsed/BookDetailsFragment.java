package com.example.bookspace.fragment.interetsed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.bookspace.R;
import com.example.bookspace.adapter.ImagePagerAdapter;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.SessionManager;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.model.model_class.remote.BookCategory;
import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.BookInfo;
import com.example.bookspace.model.model_class.remote.BookMode;
import com.example.bookspace.model.model_class.remote.ExpectedBook;
import com.example.bookspace.model.model_class.remote.Interested;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.interested.BookDetailsViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.bookspace.enum_class.Mode.EXCHANGE;
import static com.example.bookspace.enum_class.Mode.EXCHANGE_WITH_CASH;
import static com.example.bookspace.enum_class.Mode.RENT;
import static com.example.bookspace.enum_class.Mode.SELL;

public class BookDetailsFragment extends Fragment {

    private static final String TAG = "sayed";
    private BookDetailsViewModel bookDetailsViewModel;
    private SharedViewModel sharedViewModel;
    private NavController navController;
    private BookDetails bookDetails;
    private ViewPager viewPager, expectedViewPager;
    private LinearLayout sliderDotsPanel, expectedSliderDotsPanel;
    private TextView bookName, authorName, category, price, location, mode, condition, edition, year, date;
    private TextView expectedBookName, expectedAuthorName, expectedPrice, expectedLabel;
    private ExtendedFloatingActionButton interestedButton;
    private View divider;
    private SessionManager sessionManager;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView(view);
        initButton();
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        bookDetailsViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(BookDetailsViewModel.class);
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        sessionManager = SessionManager.getInstance(requireContext());
        subscribeObserver();
    }

    private void initButton() {
        interestedButton.setOnClickListener(view -> {
            if (NetworkInfo.hasNetwork(requireContext())) {
                bookDetailsViewModel.insertInterestedBook(getInterested());
            } else showSnackBar("Ne internet connection");
        });
    }

    private Interested getInterested() {
        Interested interested = new Interested();
        interested.setBookDetails(bookDetails);
        interested.setUser(sessionManager.getUserDetails());
        return interested;
    }

    private void subscribeObserver() {
        sharedViewModel.observeCurrentViewedBook().observe(getViewLifecycleOwner(), bookDetails -> {
            if (bookDetails != null) {
                this.bookDetails = bookDetails;
                setUI();
            }
        });

        bookDetailsViewModel.observeInserting().removeObservers(getViewLifecycleOwner());
        bookDetailsViewModel.observeInserting().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        showSnackBar("Something went wrong. Please try again later.");
                        bookDetailsViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        assert (resource.data != null);
                        if (resource.data) showDialog();
                        else {
                            showSnackBar("Already show interested on this book");
                            bookDetailsViewModel.clearObserver();
                            bookDetailsViewModel.clearObserver();
                        }
                        break;
                }
            }
        });

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

    private void showSnackBar(String message) {
        Snackbar.make(Objects.requireNonNull(this.getView()), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
//            navController.navigate(R.id.action_bookDetailsFragment_to_interestedFragment);
            bookDetailsViewModel.clearObserver();
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initView(View view) {
        viewPager = view.findViewById(R.id.bookDetailsViewPager);
        expectedViewPager = view.findViewById(R.id.expectedBookViewPager);
        sliderDotsPanel = view.findViewById(R.id.SliderDots);
        expectedSliderDotsPanel = view.findViewById(R.id.expectedSliderDots);
        divider = view.findViewById(R.id.divider);
        bookName = view.findViewById(R.id.nameTV);
        authorName = view.findViewById(R.id.authorNameTV);
        category = view.findViewById(R.id.categoryTV);
        price = view.findViewById(R.id.priceTV);
        location = view.findViewById(R.id.locationTV);
        date = view.findViewById(R.id.dateTV);
        mode = view.findViewById(R.id.modeTV);
        condition = view.findViewById(R.id.conditionTV);
        edition = view.findViewById(R.id.editionTV);
        year = view.findViewById(R.id.yearTV);
        expectedBookName = view.findViewById(R.id.expectedBookNameTV);
        expectedAuthorName = view.findViewById(R.id.expectedAuthorNameTV);
        expectedPrice = view.findViewById(R.id.expectedPriceTV);
        expectedLabel = view.findViewById(R.id.ownerExpectedBookLabelTV);
        interestedButton = view.findViewById(R.id.interestedBT);
    }

    private void setUI() {
        BookInfo bookInfo = bookDetails.getBookInfo();
        BookMode bookMode = bookDetails.getBookMode();
        BookCategory bookCategory = bookDetails.getBookCategory();
        ExpectedBook expectedBook = bookDetails.getExpectedBook();

        hideExpectedBookView(true);
        initViewPager(getImageUrl(bookInfo));
        setBookUI(bookInfo, bookCategory, bookMode);

        String mode = bookDetails.getBookMode().getBookMode();
        if (mode.equals(EXCHANGE.getMode()) || mode.equals(EXCHANGE_WITH_CASH.getMode())) {
            if (bookDetails.getExpectedBook() != null) {
                hideExpectedBookView(false);
                initExpectedViewPager(getExpectedImageUrl(expectedBook));
                setExpectedBookUI(expectedBook, mode);
            }
        }
    }

    private void setBookUI(BookInfo bookInfo, BookCategory bookCategory, BookMode bookMode) {
        category.setText(bookCategory.getSubCategory());
        date.setText(bookInfo.getDateAndTime());
        location.setText(bookInfo.getBookLocation() + ",Dhaka");
        mode.setText(bookMode.getBookMode());
        bookName.setText(bookInfo.getBookName());
        authorName.setText(bookInfo.getBookAuthorName());
        edition.setText("Edition: " + bookInfo.getBookEdition());
        year.setText("Year: " + bookInfo.getBookPublicationYear());
        condition.setText("Condition: " + bookInfo.getBookCondition());
        if (bookMode.getBookMode().equals(SELL.getMode()) || bookMode.getBookMode().equals(RENT.getMode())) {
            hideBookPrice(false);
            price.setText("\u09F3 " + bookInfo.getBookPrice() + "/-");
        } else {
            hideBookPrice(true);
        }
    }

    private void setExpectedBookUI(ExpectedBook expectedBook, String mode) {
        if (mode.equals(EXCHANGE.getMode())) {
            hideExpectedPrice(true);
            expectedBookName.setText(expectedBook.getBoonName());
            expectedAuthorName.setText(expectedBook.getBookAuthorName());
        } else {
            hideExpectedPrice(false);
            expectedBookName.setText(expectedBook.getBoonName());
            expectedAuthorName.setText(expectedBook.getBookAuthorName());
            expectedPrice.setText("\u09F3 " + expectedBook.getBookPrice() + "/-");
        }
    }

    private void hideBookPrice(boolean isHide) {
        if (isHide) {
            price.setVisibility(View.GONE);
        } else {
            price.setVisibility(View.VISIBLE);
        }
    }

    private void hideExpectedPrice(boolean isHide) {
        if (isHide) {
            expectedPrice.setVisibility(View.GONE);
        } else {
            expectedPrice.setVisibility(View.VISIBLE);
        }
    }

    private void hideExpectedBookView(boolean isHide) {
        if (isHide) {
            divider.setVisibility(View.GONE);
            expectedLabel.setVisibility(View.GONE);
            expectedViewPager.setVisibility(View.GONE);
            expectedSliderDotsPanel.setVisibility(View.GONE);
            expectedAuthorName.setVisibility(View.GONE);
            expectedBookName.setVisibility(View.GONE);
            expectedPrice.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
            expectedLabel.setVisibility(View.VISIBLE);
            expectedViewPager.setVisibility(View.VISIBLE);
            expectedSliderDotsPanel.setVisibility(View.VISIBLE);
            expectedAuthorName.setVisibility(View.VISIBLE);
            expectedBookName.setVisibility(View.VISIBLE);
            expectedPrice.setVisibility(View.VISIBLE);
        }
    }

    private List<String> getImageUrl(BookInfo bookInfo) {
        List<String> imageUrls = new ArrayList<>();
        if (bookInfo.getImageLocation1() != null) {
            imageUrls.add(bookInfo.getImageLocation1());
        }
        if (bookInfo.getImageLocation2() != null) {
            imageUrls.add(bookInfo.getImageLocation2());
        }
        if (bookInfo.getImageLocation3() != null) {
            imageUrls.add(bookInfo.getImageLocation3());
        }
        return imageUrls;
    }

    private List<String> getExpectedImageUrl(ExpectedBook expectedBook) {
        List<String> imageUrls = new ArrayList<>();
        if (expectedBook.getImageLocation1() != null) {
            imageUrls.add(expectedBook.getImageLocation1());
        }
        if (expectedBook.getImageLocation2() != null) {
            imageUrls.add(expectedBook.getImageLocation2());
        }
        if (expectedBook.getImageLocation3() != null) {
            imageUrls.add(expectedBook.getImageLocation3());
        }
        return imageUrls;
    }

    private void initViewPager(List<String> imageUrls) {
        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), imageUrls);
        viewPager.setAdapter(adapter);
        int dotCount = adapter.getCount();
        ImageView[] dots = new ImageView[dotCount];
        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(requireContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.non_active_dots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.active_dots));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.non_active_dots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.active_dots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initExpectedViewPager(List<String> imageUrls) {
        ImagePagerAdapter adapter = new ImagePagerAdapter(requireContext(), imageUrls);
        expectedViewPager.setAdapter(adapter);
        int dotCount = adapter.getCount();
        ImageView[] dots = new ImageView[dotCount];
        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(requireContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.non_active_dots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            expectedSliderDotsPanel.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.active_dots));
        expectedViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.non_active_dots));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(), R.drawable.active_dots));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
