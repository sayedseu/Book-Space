package com.example.bookspace.fragment.auth;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.enum_class.Education;
import com.example.bookspace.model.model_class.remote.User;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.utils.Utils;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.auth.SignupViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import static com.example.bookspace.utils.Validation.isNumberValid;
import static com.example.bookspace.utils.Validation.isValidEmail;

public class SignupFragment extends Fragment {
    private static final String TAG = "sayed";
    private SignupViewModel signupViewModel;
    private SharedViewModel sharedViewModel;
    private TextView alreadyHaveAccount;
    private Button signUpBT;
    private NavController navController;
    private TextInputEditText nameET, phoneET, emailET, passwordET, confirmPasswordET;
    private String name = "", email = "", phone = "", password = "", confirmPassword = "";
    private RadioGroup studentQuestionRG, educationLevelRG;
    private boolean isStudent = false;
    private String educationLevel = Education.NONE.getEducation();
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView(view);
        initButton();
        initBackPress();
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        signupViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(SignupViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(SharedViewModel.class);
        subscribeObserver();
    }

    private void initView(View view) {
        alreadyHaveAccount = view.findViewById(R.id.alreadyHaveAccountBT);
        signUpBT = view.findViewById(R.id.signUpBT);
        nameET = view.findViewById(R.id.userName_textInputEditText);
        emailET = view.findViewById(R.id.email_textInputEditText);
        phoneET = view.findViewById(R.id.phone_textInputEditText);
        passwordET = view.findViewById(R.id.password_textInputEditText);
        confirmPasswordET = view.findViewById(R.id.confirmPassword_textInputEditText);
        studentQuestionRG = view.findViewById(R.id.studentQuestionRG);
        educationLevelRG = view.findViewById(R.id.educationLevelRG);
    }

    private void initProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.progress_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
    }

    private void initBackPress() {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.popBackStack();
                        signupViewModel.clearObserver();
                    }
                });
    }

    private void initButton() {
        signUpBT.setOnClickListener(view -> {
            if (validateField()) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    signupViewModel.checkDuplicateUser(phone, email);
                } else shoSnackBar("No internet connection!");
            }
        });
        alreadyHaveAccount.setOnClickListener(v -> {
            navController.popBackStack();
            clearField();
        });

        studentQuestionRG.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radio_yes:
                    isStudent = true;
                    return;
                case R.id.radio_no:
                    Log.i(TAG, "initButton: no");
                    isStudent = false;
                    return;
                default:
                    isStudent = false;
            }
        });

        educationLevelRG.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radio_school:
                    educationLevel = Education.SCHOOL.getEducation();
                    return;
                case R.id.radio_collage:
                    educationLevel = Education.COLLAGE.getEducation();
                    return;
                case R.id.radio_university:
                    educationLevel = Education.UNIVERSITY.getEducation();
                    return;
                case R.id.radio_none:
                    educationLevel = Education.NONE.getEducation();
                    return;
                default:
                    educationLevel = Education.NONE.getEducation();
            }
        });
    }

    private void subscribeObserver() {
        signupViewModel.observeSendOtpResult().removeObservers(getViewLifecycleOwner());
        signupViewModel.observeSendOtpResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("Invalid Phone Number!");
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        assert (resource.data != null);
                        if (resource.data) {
                            goToOtpFragment();
                            break;
                        }
                }
            }
        });

        signupViewModel.observeCheckDuplicateUser().removeObservers(getViewLifecycleOwner());
        signupViewModel.observeCheckDuplicateUser().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("Something Went Wrong!");
                        signupViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        assert (resource.data != null);
                        if (resource.data) {
                            signupViewModel.sendOtp(phone);
                            break;
                        } else {
                            showProgressDialog(false);
                            shoSnackBar("Phone Number or Email Already Used!");
                            signupViewModel.clearObserver();
                            break;
                        }
                }
            }
        });
    }

    private void goToOtpFragment() {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        user.setStudent(isStudent);
        user.setEducationLevel(educationLevel);
        sharedViewModel.setRegisteringUser(user);
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.OTP_KEY, Utils.SIGNUP_FRAGMENT);
        navController.navigate(R.id.action_signupFragment_to_otpFragment, bundle);
        signupViewModel.clearObserver();
    }

    private void shoSnackBar(String message) {
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showProgressDialog(boolean isShow) {
        if (isShow) alertDialog.show();
        else {
            if (alertDialog.isShowing()) alertDialog.dismiss();
        }
    }

    private void clearField() {
        nameET.setText(null);
        emailET.setText(null);
        phoneET.setText(null);
        passwordET.setText(null);
        confirmPasswordET.setText(null);
    }

    private boolean validateField() {
        boolean flag = true;
        try {
            name = nameET.getText().toString();
            phone = phoneET.getText().toString();
            email = emailET.getText().toString();
            password = passwordET.getText().toString();
            confirmPassword = confirmPasswordET.getText().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (name.trim().isEmpty()) {
            nameET.setError("Required");
            flag = false;
        }
        if (phone.isEmpty() || !isNumberValid(phone.trim())) {
            phoneET.setError("Invalid");
            flag = false;
        }
        if (email.trim().isEmpty() || !isValidEmail(email.trim())) {
            emailET.setError("Invalid");
            flag = false;
        }
        if (password.trim().isEmpty()) {
            passwordET.setError("Required");
            flag = false;
        } else if (confirmPassword.trim().isEmpty()) {
            confirmPasswordET.setError("Required");
            flag = false;
        } else if (!password.trim().equals(confirmPassword.trim())) {
            confirmPasswordET.setError("Password not match");
            flag = false;
        }
        return flag;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearField();
    }
}