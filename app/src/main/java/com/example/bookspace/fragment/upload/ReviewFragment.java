package com.example.bookspace.fragment.upload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.custom.CustomTextView;
import com.example.bookspace.custom.ImageRoundCorners;
import com.example.bookspace.model.model_class.remote.BookCategory;
import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.BookInfo;
import com.example.bookspace.model.model_class.remote.BookMode;
import com.example.bookspace.model.model_class.remote.ExpectedBook;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.upload.ReviewViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.example.bookspace.enum_class.Mode.DONATE;
import static com.example.bookspace.enum_class.Mode.EXCHANGE;
import static com.example.bookspace.enum_class.Mode.EXCHANGE_WITH_CASH;
import static com.example.bookspace.enum_class.Mode.RENT;
import static com.example.bookspace.enum_class.Mode.SELL;

public class ReviewFragment extends Fragment {
    private ImageView photo1, photo2, photo3, expectedPhoto1, expectedPhoto2, expectedPhoto3;
    private CustomTextView bookName, bookAuthor, bookPrice, bookPublication, bookEdition,
            bookCategory, bookCondition, bookLocation, bookIsbn, bookMode,
            expectedBookName, expectedBookAuthor, expectedBookPrice;
    private Button confirmButton;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private ReviewViewModel reviewViewModel;
    private BookDetails bookDetails;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initBackPress();
        initView(view);
        initListener();
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        reviewViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(ReviewViewModel.class);
        subscribeObserver();
    }

    private void initListener() {
        confirmButton.setOnClickListener(event -> {
            if (NetworkInfo.hasNetwork(requireContext())) {
                assert (bookDetails != null);
                reviewViewModel.uploadBook(bookDetails);
            } else showSnackBar("No internet connection!");
        });
    }

    private void initBackPress() {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.popBackStack();
                    }
                });
    }

    private void subscribeObserver() {
        sharedViewModel.observeUploadingBook().observe(getViewLifecycleOwner(), bookDetails -> {
            if (bookDetails != null) {
                this.bookDetails = bookDetails;
                setUpUi(bookDetails);
            }
        });

        reviewViewModel.observeUploadingResult().removeObservers(getViewLifecycleOwner());
        reviewViewModel.observeUploadingResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        showSnackBar("Something went wrong.Please try again letter!");
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        assert (resource.data != null);
                        if (resource.data) showSuccessDialog();
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

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            navController.navigate(R.id.action_reviewFragment_to_homeFragment);
            reviewViewModel.clearObserver();
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initView(View view) {
        photo1 = view.findViewById(R.id.photo1);
        photo2 = view.findViewById(R.id.photo2);
        photo3 = view.findViewById(R.id.photo3);
        expectedPhoto1 = view.findViewById(R.id.expectedPhoto1);
        expectedPhoto2 = view.findViewById(R.id.expectedPhoto2);
        expectedPhoto3 = view.findViewById(R.id.expectedPhoto3);
        bookName = view.findViewById(R.id.bookName);
        bookAuthor = view.findViewById(R.id.authorName);
        bookCategory = view.findViewById(R.id.bookCategory);
        bookPrice = view.findViewById(R.id.bookPrice);
        bookCondition = view.findViewById(R.id.bookCondition);
        bookEdition = view.findViewById(R.id.bookEdition);
        bookIsbn = view.findViewById(R.id.bookIsbnNumber);
        bookPublication = view.findViewById(R.id.bookPublicationYear);
        bookMode = view.findViewById(R.id.bookMode);
        bookLocation = view.findViewById(R.id.bookLocation);
        expectedBookName = view.findViewById(R.id.expectedBookName);
        expectedBookAuthor = view.findViewById(R.id.expectedBookAuthor);
        expectedBookPrice = view.findViewById(R.id.expectedBookPrice);
        confirmButton = view.findViewById(R.id.confirmBT);
    }

    private void setUpUi(BookDetails bookDetails) {
        BookInfo bookInfo = bookDetails.getBookInfo();
        BookMode mode = bookDetails.getBookMode();
        BookCategory category = bookDetails.getBookCategory();
        ExpectedBook expectedBook = bookDetails.getExpectedBook();

        if (bookInfo.getImageLocation1() != null) {
            loadImage(bookInfo.getImageLocation1(), photo1);
        }
        if (bookInfo.getImageLocation2() != null) {
            loadImage(bookInfo.getImageLocation2(), photo2);
        }
        if (bookInfo.getImageLocation3() != null) {
            loadImage(bookInfo.getImageLocation3(), photo3);
        }

        bookName.setLabelValue(bookInfo.getBookName());
        bookAuthor.setLabelValue(bookInfo.getBookAuthorName());
        bookPublication.setLabelValue(bookInfo.getBookPublicationYear());
        bookMode.setLabelValue(mode.getBookMode());
        bookCategory.setLabelValue(category.getSubCategory());
        bookCondition.setLabelValue(bookInfo.getBookCondition());
        bookEdition.setLabelValue(bookInfo.getBookEdition());
        bookLocation.setLabelValue(bookInfo.getBookLocation());
        bookIsbn.setLabelValue(bookInfo.getIsbnNumber());

        if (mode.getBookMode().equals(SELL.getMode())) {
            bookPrice.setLabelName("Book Price:");
            bookPrice.setLabelValue(bookInfo.getBookPrice());
        } else if (mode.getBookMode().equals(RENT.getMode())) {
            bookPrice.setLabelName("Rent Amount:");
            bookPrice.setLabelValue(bookInfo.getBookPrice());
        } else if (mode.getBookMode().equals(DONATE.getMode())) {
            bookPrice.setVisibility(View.GONE);
        } else if (mode.getBookMode().equals(EXCHANGE.getMode()) || mode.getBookMode().equals(EXCHANGE_WITH_CASH.getMode())) {
            expectedPhoto1.setVisibility(View.VISIBLE);
            expectedPhoto2.setVisibility(View.VISIBLE);
            expectedPhoto3.setVisibility(View.VISIBLE);
            if (expectedBook.getImageLocation1() != null) {
                loadImage(expectedBook.getImageLocation1(), expectedPhoto1);
            }
            if (expectedBook.getImageLocation2() != null) {
                loadImage(expectedBook.getImageLocation2(), expectedPhoto2);
            }
            if (expectedBook.getImageLocation3() != null) {
                loadImage(expectedBook.getImageLocation3(), expectedPhoto3);
            }
            bookPrice.setVisibility(View.GONE);
            expectedBookName.setVisibility(View.VISIBLE);
            expectedBookName.setLabelValue(expectedBook.getBoonName());
            expectedBookAuthor.setVisibility(View.VISIBLE);
            expectedBookAuthor.setLabelValue(expectedBook.getBookAuthorName());
            if (mode.getBookMode().equals(EXCHANGE_WITH_CASH.getMode())) {
                expectedBookPrice.setVisibility(View.VISIBLE);
                expectedBookPrice.setLabelValue(expectedBook.getBookPrice());
            }
        }
    }

    private void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.ic_warning)
                .transform(new ImageRoundCorners())
                .into(imageView);
    }
}
