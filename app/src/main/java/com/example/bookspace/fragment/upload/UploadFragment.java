package com.example.bookspace.fragment.upload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.app.SessionManager;
import com.example.bookspace.custom.CustomRadioButton;
import com.example.bookspace.custom.UploadLabelView;
import com.example.bookspace.model.model_class.remote.BookCategory;
import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.BookInfo;
import com.example.bookspace.model.model_class.remote.BookMode;
import com.example.bookspace.model.model_class.remote.ExpectedBook;
import com.example.bookspace.model.model_class.remote.User;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.view_model.SharedViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.bookspace.enum_class.Category.ACADEMIC;
import static com.example.bookspace.enum_class.Category.NON_ACADEMIC;
import static com.example.bookspace.enum_class.Condition.FAIR;
import static com.example.bookspace.enum_class.Condition.NEW;
import static com.example.bookspace.enum_class.Condition.USED;
import static com.example.bookspace.enum_class.Mode.DONATE;
import static com.example.bookspace.enum_class.Mode.EXCHANGE;
import static com.example.bookspace.enum_class.Mode.EXCHANGE_WITH_CASH;
import static com.example.bookspace.enum_class.Mode.RENT;
import static com.example.bookspace.enum_class.Mode.SELL;

public class UploadFragment extends Fragment {

    private UploadLabelView photoLabel,
            modeLabel,
            expectedBookLabel,
            bookDetailsLabel,
            categoryLabel,
            locationLabel,
            bookConditionLabel;

    private CustomRadioButton sellRB,
            donateRB,
            rentRB,
            exchangeRB,
            exchangeWithCashRB,
            newBookRB,
            usedBookRB,
            fairBookRB;

    private ImageView selectImage1,
            selectImage2,
            selectImage3,
            expectedSelctPhot1,
            expectedSelctPhot2,
            expectedSelctPhot3;

    private TextView photoWarning,
            modeWarning,
            expectedBookWarning,
            bookDetailsWarning,
            categoryWarning,
            locationWarning,
            conditionWarning;

    private TextInputLayout expectedBookNameEL,
            expectedAuthorNameEL,
            expectedBookPriceEL,
            bookPriceEl;

    private TextInputEditText expectedBookNameET,
            expectedAuthorNameET,
            expectedBookPriceET,
            bookaNameET,
            authorNameET,
            publicationYearET,
            bookEditionET,
            isbnNumberET,
            bookPriceET;

    private Button bookUploadButton;
    private Spinner categorySpinner, citySpinner;
    private MaterialButtonToggleGroup toggleGroup;

    private String image_location;
    private String image_location1 = null;
    private String image_location2 = null;
    private String image_location3 = null;
    private String expected_image_location1 = null;
    private String expected_image_location2 = null;
    private String expected_image_location3 = null;

    private Uri photoURI;
    private int flag = 0;

    private String mode = "";
    private String condition = "";
    private String location = "";
    private String category = "";
    private String mainCategory = "";
    private String name = null;
    private String author = null;
    private String price = null;
    private String bookPrice = null;
    private String bookName = null;
    private String bookAuthor = null;
    private String bookPublicationYear = null;
    private String bookEdition = null;
    private String isbnNUmber = null;

    private NavController navController;
    private SharedViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView(view);
        initImageButtonListener();
        initCustomRadioButtonListener();
        initSpinner();
        clearRB();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkStoragePermission();
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        bookUploadButton.setOnClickListener(event -> {
            if (isValidateAllField()) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    placeForUpload();
                } else showSnackBar("No internet connection!");
            }
        });
    }

    private void placeForUpload() {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookName(bookName);
        bookInfo.setBookAuthorName(bookAuthor);
        bookInfo.setBookPrice(bookPrice);
        bookInfo.setBookPublicationYear(bookPublicationYear);
        bookInfo.setBookEdition(bookEdition);
        bookInfo.setBookLocation(location);
        bookInfo.setBookCondition(condition);
        bookInfo.setIsbnNumber(isbnNUmber);
        bookInfo.setBookCondition(condition);
        bookInfo.setImageLocation1(image_location1);
        bookInfo.setImageLocation2(image_location2);
        bookInfo.setImageLocation3(image_location3);
        bookInfo.setDateAndTime(getCurrentDateAndTime());

        BookCategory bookCategory = new BookCategory();
        bookCategory.setMainCategory(mainCategory);
        bookCategory.setSubCategory(category);

        BookMode bookMode = new BookMode();
        bookMode.setBookMode(mode);

        ExpectedBook expectedBook = new ExpectedBook();
        expectedBook.setBoonName(name);
        expectedBook.setBookAuthorName(author);
        expectedBook.setBookPrice(price);
        expectedBook.setImageLocation1(expected_image_location1);
        expectedBook.setImageLocation2(expected_image_location2);
        expectedBook.setImageLocation3(expected_image_location3);

        User user = SessionManager.getInstance(requireActivity().getApplication()).getUserDetails();
        assert (user != null);

        BookDetails bookDetails = new BookDetails();
        bookDetails.setOwner(user);
        bookDetails.setBookInfo(bookInfo);
        bookDetails.setBookCategory(bookCategory);
        bookDetails.setBookMode(bookMode);
        bookDetails.setExpectedBook(expectedBook);

        viewModel.setUploadingBook(bookDetails);

        clearField();
        initSpinner();
        navController.navigate(R.id.action_uploadFragment_to_reviewFragment);
    }

    private void clearField() {
        image_location1 = null;
        image_location2 = null;
        image_location3 = null;
        expected_image_location1 = null;
        expected_image_location2 = null;
        expected_image_location3 = null;
        mode = "";
        condition = "";
        location = "";
        category = "";
        mainCategory = "";
        expectedBookNameET.setText(null);
        expectedAuthorNameET.setText(null);
        expectedBookPriceET.setText(null);
        bookaNameET.setText(null);
        authorNameET.setText(null);
        publicationYearET.setText(null);
        bookEditionET.setText(null);
        isbnNumberET.setText(null);
        bookPriceET.setText(null);
        clearRB();
    }

    private void clearRB() {
        sellRB.setChecked(false);
        rentRB.setChecked(false);
        donateRB.setChecked(false);
        exchangeRB.setChecked(false);
        exchangeWithCashRB.setChecked(false);
        usedBookRB.setChecked(false);
        newBookRB.setChecked(false);
        fairBookRB.setChecked(false);
    }

    private void clearExpectedField() {
        expected_image_location1 = null;
        expected_image_location2 = null;
        expected_image_location3 = null;
        expectedSelctPhot1.setImageResource(R.drawable.add_image);
        expectedSelctPhot2.setImageResource(R.drawable.add_image);
        expectedSelctPhot3.setImageResource(R.drawable.add_image);
        expectedBookNameET.setText(null);
        expectedAuthorNameET.setText(null);
        expectedBookPriceET.setText(null);
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> academicAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.academic_category_spinner, android.R.layout.simple_spinner_item);
        academicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> nonAcademic = ArrayAdapter.createFromResource(requireActivity(),
                R.array.non_academic_category_spinner, android.R.layout.simple_spinner_item);
        nonAcademic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            switch (group.getCheckedButtonId()) {
                case R.id.academicMB:
                    mainCategory = ACADEMIC.getCategory();
                    categorySpinner.setVisibility(View.VISIBLE);
                    categorySpinner.setAdapter(academicAdapter);
                    return;
                case R.id.nonAcademicMB:
                    mainCategory = NON_ACADEMIC.getCategory();
                    categorySpinner.setVisibility(View.VISIBLE);
                    categorySpinner.setAdapter(nonAcademic);
                    return;
            }
        });

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.city_list, android.R.layout.simple_spinner_item);
        academicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                location = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void checkStoragePermission() {

        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
    }

    private void initView(View view) {
        selectImage1 = view.findViewById(R.id.select_image1);
        selectImage2 = view.findViewById(R.id.select_image2);
        selectImage3 = view.findViewById(R.id.select_image3);

        photoLabel = view.findViewById(R.id.photo_label);
        modeLabel = view.findViewById(R.id.select_mode_label);
        expectedBookLabel = view.findViewById(R.id.expectBookLabel);
        bookDetailsLabel = view.findViewById(R.id.book_details_label);
        categoryLabel = view.findViewById(R.id.selectCategoryLabel);
        bookConditionLabel = view.findViewById(R.id.bookConditionLabel);
        locationLabel = view.findViewById(R.id.selectLocationLabel);

        sellRB = view.findViewById(R.id.sell_radio_bt);
        donateRB = view.findViewById(R.id.donate_radio_bt);
        rentRB = view.findViewById(R.id.rent_radio_bt);
        exchangeRB = view.findViewById(R.id.exchange_radio_bt);
        exchangeWithCashRB = view.findViewById(R.id.exchange_with_cash_radio_bt);
        newBookRB = view.findViewById(R.id.newConditionRadioButton);
        usedBookRB = view.findViewById(R.id.usedConditionRadioButton);
        fairBookRB = view.findViewById(R.id.likeFairRadioButton);

        expectedSelctPhot1 = view.findViewById(R.id.expected_image1);
        expectedSelctPhot2 = view.findViewById(R.id.expected_image2);
        expectedSelctPhot3 = view.findViewById(R.id.expected_image3);

        photoWarning = view.findViewById(R.id.photo_warning_tv);
        modeWarning = view.findViewById(R.id.mode_warning);
        expectedBookWarning = view.findViewById(R.id.expected_book_warning);
        bookDetailsWarning = view.findViewById(R.id.bookDetailsWarning);
        categoryWarning = view.findViewById(R.id.selectCategoryWarning);
        locationWarning = view.findViewById(R.id.selectLocationWarning);
        conditionWarning = view.findViewById(R.id.bookConditionWarning);

        expectedBookNameEL = view.findViewById(R.id.expectedBookNameTextInputLayout);
        expectedAuthorNameEL = view.findViewById(R.id.expectedAuthorNameTextInputLayout);
        expectedBookPriceEL = view.findViewById(R.id.expectedPriceTextInputLayout);
        bookPriceEl = view.findViewById(R.id.bookPriceTextInputLayout);

        expectedBookNameET = view.findViewById(R.id.expectedBookNameET);
        expectedAuthorNameET = view.findViewById(R.id.expectedAuthorNameET);
        expectedBookPriceET = view.findViewById(R.id.expectedPriceET);
        bookaNameET = view.findViewById(R.id.bookNameET);
        authorNameET = view.findViewById(R.id.authorET);
        publicationYearET = view.findViewById(R.id.publicationET);
        bookEditionET = view.findViewById(R.id.editionET);
        isbnNumberET = view.findViewById(R.id.isbnET);
        bookPriceET = view.findViewById(R.id.priceET);

        citySpinner = view.findViewById(R.id.selectLocationSpinner);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        toggleGroup = view.findViewById(R.id.selectCategoryMaterialButtonToggleGroup);

        bookUploadButton = view.findViewById(R.id.uploadBT);
    }

    private void initCustomRadioButtonListener() {
        sellRB.setListener(view -> {
            mode = SELL.getMode();
            donateRB.setChecked(false);
            rentRB.setChecked(false);
            exchangeWithCashRB.setChecked(false);
            exchangeRB.setChecked(false);
            bookPriceEl.setVisibility(View.VISIBLE);
            bookPriceEl.setHint("Book Price");
            expectedBookLabel.setVisibility(View.GONE);
            expectedSelctPhot1.setVisibility(View.GONE);
            expectedSelctPhot2.setVisibility(View.GONE);
            expectedSelctPhot3.setVisibility(View.GONE);
            expectedAuthorNameEL.setVisibility(View.GONE);
            expectedBookNameEL.setVisibility(View.GONE);
            expectedBookPriceEL.setVisibility(View.GONE);
            expectedBookWarning.setVisibility(View.GONE);
            clearExpectedField();
        });
        donateRB.setListener(view -> {
            mode = DONATE.getMode();
            sellRB.setChecked(false);
            rentRB.setChecked(false);
            exchangeWithCashRB.setChecked(false);
            exchangeRB.setChecked(false);
            bookPriceEl.setVisibility(View.GONE);
            expectedBookLabel.setVisibility(View.GONE);
            expectedSelctPhot1.setVisibility(View.GONE);
            expectedSelctPhot2.setVisibility(View.GONE);
            expectedSelctPhot3.setVisibility(View.GONE);
            expectedAuthorNameEL.setVisibility(View.GONE);
            expectedBookNameEL.setVisibility(View.GONE);
            expectedBookPriceEL.setVisibility(View.GONE);
            expectedBookWarning.setVisibility(View.GONE);
            clearExpectedField();
        });
        rentRB.setListener(view -> {
            mode = RENT.getMode();
            sellRB.setChecked(false);
            donateRB.setChecked(false);
            exchangeWithCashRB.setChecked(false);
            exchangeRB.setChecked(false);
            bookPriceEl.setVisibility(View.VISIBLE);
            bookPriceEl.setHint("Rent Amount");
            expectedBookLabel.setVisibility(View.GONE);
            expectedSelctPhot1.setVisibility(View.GONE);
            expectedSelctPhot2.setVisibility(View.GONE);
            expectedSelctPhot3.setVisibility(View.GONE);
            expectedAuthorNameEL.setVisibility(View.GONE);
            expectedBookNameEL.setVisibility(View.GONE);
            expectedBookPriceEL.setVisibility(View.GONE);
            expectedBookWarning.setVisibility(View.GONE);
            clearExpectedField();
        });
        exchangeRB.setListener(view -> {
            mode = EXCHANGE.getMode();
            sellRB.setChecked(false);
            donateRB.setChecked(false);
            rentRB.setChecked(false);
            exchangeWithCashRB.setChecked(false);
            bookPriceEl.setVisibility(View.GONE);
            expectedBookLabel.setVisibility(View.VISIBLE);
            expectedSelctPhot1.setVisibility(View.VISIBLE);
            expectedSelctPhot2.setVisibility(View.VISIBLE);
            expectedSelctPhot3.setVisibility(View.VISIBLE);
            expectedAuthorNameEL.setVisibility(View.VISIBLE);
            expectedBookNameEL.setVisibility(View.VISIBLE);
            expectedBookPriceEL.setVisibility(View.GONE);

        });
        exchangeWithCashRB.setListener(view -> {
            mode = EXCHANGE_WITH_CASH.getMode();
            sellRB.setChecked(false);
            donateRB.setChecked(false);
            rentRB.setChecked(false);
            exchangeRB.setChecked(false);
            bookPriceEl.setVisibility(View.GONE);
            expectedBookLabel.setVisibility(View.VISIBLE);
            expectedSelctPhot1.setVisibility(View.VISIBLE);
            expectedSelctPhot2.setVisibility(View.VISIBLE);
            expectedSelctPhot3.setVisibility(View.VISIBLE);
            expectedAuthorNameEL.setVisibility(View.VISIBLE);
            expectedBookNameEL.setVisibility(View.VISIBLE);
            expectedBookPriceEL.setVisibility(View.VISIBLE);

        });
        newBookRB.setListener(view -> {
            condition = NEW.getCondition();
            usedBookRB.setChecked(false);
            fairBookRB.setChecked(false);
        });
        usedBookRB.setListener(view -> {
            condition = USED.getCondition();
            newBookRB.setChecked(false);
            fairBookRB.setChecked(false);
        });
        fairBookRB.setListener(view -> {
            condition = FAIR.getCondition();
            newBookRB.setChecked(false);
            usedBookRB.setChecked(false);
        });
    }

    private void initImageButtonListener() {
        selectImage1.setOnClickListener(view -> {
            if (image_location1 == null) {
                flag = 1;
                selectPhoto();
            }
        });
        selectImage2.setOnClickListener(view -> {
            if (image_location2 == null) {
                flag = 2;
                selectPhoto();
            }
        });
        selectImage3.setOnClickListener(view -> {
            if (image_location3 == null) {
                flag = 3;
                selectPhoto();
            }
        });
        expectedSelctPhot1.setOnClickListener(view -> {
            if (expected_image_location1 == null) {
                flag = 4;
                selectPhoto();
            }
        });
        expectedSelctPhot2.setOnClickListener(view -> {
            if (expected_image_location2 == null) {
                flag = 5;
                selectPhoto();
            }
        });
        expectedSelctPhot3.setOnClickListener(view -> {
            if (expected_image_location3 == null) {
                flag = 6;
                selectPhoto();
            }
        });

    }

    private void selectPhoto() {
        android.app.AlertDialog.Builder alertdialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertdialogBuilder.setTitle("Select Photo");
        alertdialogBuilder.setMessage("Choice any Option !! ");
        alertdialogBuilder.setIcon(R.drawable.camera);

        alertdialogBuilder.setNeutralButton("Camera", (dialogInterface, i) -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                File imageFile = null;
                try {
                    imageFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (imageFile != null) {
                    photoURI = FileProvider.getUriForFile(getActivity(), "com.example.bookspace", imageFile);
                    image_location = photoURI.toString();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, 00);
                }
            }
            dialogInterface.dismiss();
        });
        alertdialogBuilder.setPositiveButton("Gallary", (dialogInterface, i) -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 99);
            dialogInterface.dismiss();
        });
        android.app.AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 00) {

                if (flag == 1) {
                    loadImage(selectImage1);
                    image_location1 = image_location;
                } else if (flag == 2) {
                    loadImage(selectImage2);
                    image_location2 = image_location;
                } else if (flag == 3) {
                    loadImage(selectImage3);
                    image_location3 = image_location;
                } else if (flag == 4) {
                    loadImage(expectedSelctPhot1);
                    expected_image_location1 = image_location;
                } else if (flag == 5) {
                    loadImage(expectedSelctPhot2);
                    expected_image_location2 = image_location;
                } else if (flag == 6) {
                    loadImage(expectedSelctPhot3);
                    expected_image_location3 = image_location;
                }

            } else if (requestCode == 99) {

                if (flag == 1) {
                    Uri uri = data.getData();
                    image_location = uri.toString();
                    loadImage(selectImage1);
                    image_location1 = image_location;
                } else if (flag == 2) {
                    Uri uri = data.getData();
                    image_location = uri.toString();
                    loadImage(selectImage2);
                    image_location2 = image_location;
                } else if (flag == 3) {
                    Uri uri = data.getData();
                    image_location = uri.toString();
                    loadImage(selectImage3);
                    image_location3 = image_location;
                } else if (flag == 4) {
                    Uri uri = data.getData();
                    image_location = uri.toString();
                    loadImage(expectedSelctPhot1);
                    expected_image_location1 = image_location;
                } else if (flag == 5) {
                    Uri uri = data.getData();
                    image_location = uri.toString();
                    loadImage(expectedSelctPhot2);
                    expected_image_location2 = image_location;
                } else if (flag == 6) {
                    Uri uri = data.getData();
                    image_location = uri.toString();
                    loadImage(expectedSelctPhot3);
                    expected_image_location3 = image_location;
                }
            }
        }
    }

    private void loadImage(ImageView imageView) {
        Picasso.get().load(image_location)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.ic_warning)
                .into(imageView);
    }

    private boolean isValidateAllField() {
        boolean flag = true;

        Drawable warningIcon = getResources().getDrawable(R.drawable.ic_warning);
        Drawable checkIcon = getResources().getDrawable(R.drawable.ic_check);

        try {
            name = expectedBookNameET.getText().toString();
            author = expectedAuthorNameET.getText().toString();
            price = expectedBookPriceET.getText().toString();
            bookPrice = bookPriceET.getText().toString();
            bookAuthor = authorNameET.getText().toString();
            bookName = bookaNameET.getText().toString();
            bookPublicationYear = publicationYearET.getText().toString();
            bookEdition = bookEditionET.getText().toString();
            isbnNUmber = isbnNumberET.getText().toString();
        } catch (Exception e) {

        }

        if (image_location1 == null && image_location2 == null && image_location3 == null) {
            photoWarning.setVisibility(View.VISIBLE);
            photoLabel.setLabelIcon(warningIcon);
            flag = false;
        } else {
            photoWarning.setVisibility(View.GONE);
            photoLabel.setLabelIcon(checkIcon);
        }

        if (mode.isEmpty()) {
            modeWarning.setVisibility(View.VISIBLE);
            modeLabel.setLabelIcon(warningIcon);
            flag = false;
        } else {
            modeWarning.setVisibility(View.GONE);
            modeLabel.setLabelIcon(checkIcon);

            if (mode.equals(EXCHANGE.getMode())) {

                if (expected_image_location1 == null && expected_image_location2 == null && expected_image_location3 == null) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    flag = false;
                }
                if (name.isEmpty()) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    expectedBookNameET.setError("Required");
                    flag = false;
                }
                if (author.isEmpty()) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    expectedAuthorNameET.setError("Required");
                    flag = false;
                }
                if (!name.isEmpty() && !author.isEmpty() && (expected_image_location1 != null || expected_image_location2 != null || expected_image_location3 != null)) {
                    expectedBookWarning.setVisibility(View.GONE);
                    expectedBookLabel.setLabelIcon(checkIcon);
                }

            } else if (mode.equals(EXCHANGE_WITH_CASH.getMode())) {
                if (expected_image_location1 == null && expected_image_location2 == null && expected_image_location3 == null) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    flag = false;
                }
                if (name.isEmpty()) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    expectedBookNameET.setError("Required");
                    flag = false;
                }
                if (author.isEmpty()) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    expectedAuthorNameET.setError("Required");
                    flag = false;
                }
                if (price.isEmpty()) {
                    expectedBookWarning.setVisibility(View.VISIBLE);
                    expectedBookLabel.setLabelIcon(warningIcon);
                    expectedBookPriceET.setError("Required");
                    flag = false;
                }
                if (!name.isEmpty() && !author.isEmpty() && !price.isEmpty() && (expected_image_location1 != null || expected_image_location2 != null || expected_image_location3 != null)) {
                    expectedBookWarning.setVisibility(View.GONE);
                    expectedBookLabel.setLabelIcon(checkIcon);
                }

            } else if (mode.equals(DONATE.getMode())) {
                expectedBookWarning.setVisibility(View.GONE);
                expectedBookLabel.setLabelIcon(checkIcon);
            } else if (mode.equals(SELL.getMode()) || mode.equals(RENT.getMode())) {
                expectedBookWarning.setVisibility(View.GONE);
                expectedBookLabel.setLabelIcon(checkIcon);
                if (bookPrice.isEmpty()) {
                    bookPriceET.setError("Required!");
                    bookDetailsWarning.setVisibility(View.VISIBLE);
                    bookDetailsLabel.setLabelIcon(warningIcon);
                    flag = false;
                }
            }
        }

        if (bookName.isEmpty()) {
            bookDetailsWarning.setVisibility(View.VISIBLE);
            bookDetailsLabel.setLabelIcon(warningIcon);
            bookaNameET.setError("Required");
            flag = false;
        }

        if (bookAuthor.isEmpty()) {
            bookDetailsWarning.setVisibility(View.VISIBLE);
            bookDetailsLabel.setLabelIcon(warningIcon);
            authorNameET.setError("Required");
            flag = false;
        }

        if (bookEdition.isEmpty()) {
            bookDetailsWarning.setVisibility(View.VISIBLE);
            bookDetailsLabel.setLabelIcon(warningIcon);
            bookEditionET.setError("Required");
            flag = false;
        }

        if (bookPublicationYear.isEmpty() || Integer.parseInt(bookPublicationYear) > 2020) {
            bookDetailsWarning.setVisibility(View.VISIBLE);
            bookDetailsLabel.setLabelIcon(warningIcon);
            publicationYearET.setError("Invalid");
            flag = false;
        }

        if (mode.equals(SELL.getMode()) || mode.equals(RENT.getMode())) {
            if (!bookName.isEmpty() && !bookAuthor.isEmpty() && !bookEdition.isEmpty() && !bookPublicationYear.isEmpty() && !bookPrice.isEmpty()) {
                bookDetailsWarning.setVisibility(View.GONE);
                bookDetailsLabel.setLabelIcon(checkIcon);
            }
        } else if (mode.equals(EXCHANGE.getMode()) || mode.equals(EXCHANGE_WITH_CASH.getMode())) {
            if (!bookName.isEmpty() && !bookAuthor.isEmpty() && !bookEdition.isEmpty() && !bookPublicationYear.isEmpty()) {
                bookDetailsWarning.setVisibility(View.GONE);
                bookDetailsLabel.setLabelIcon(checkIcon);
            }
        }

        if (category.isEmpty() || category.equals("Select Category")) {
            categoryWarning.setVisibility(View.VISIBLE);
            categoryLabel.setLabelIcon(warningIcon);
            flag = false;
        } else {
            categoryWarning.setVisibility(View.GONE);
            categoryLabel.setLabelIcon(checkIcon);
        }

        if (location.isEmpty() || location.equals("Select Location")) {
            locationWarning.setVisibility(View.VISIBLE);
            locationLabel.setLabelIcon(warningIcon);
            flag = false;
        } else {
            locationWarning.setVisibility(View.GONE);
            locationLabel.setLabelIcon(checkIcon);
        }

        if (condition.isEmpty()) {
            conditionWarning.setVisibility(View.VISIBLE);
            bookConditionLabel.setLabelIcon(warningIcon);
            flag = false;
        } else {
            conditionWarning.setVisibility(View.GONE);
            bookConditionLabel.setLabelIcon(checkIcon);
        }
        return flag;
    }

    private String getCurrentDateAndTime() {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        return date.toString();
    }

    private void showSnackBar(String message) {
        Snackbar.make(Objects.requireNonNull(this.getView()), message, Snackbar.LENGTH_SHORT).show();
    }
}
