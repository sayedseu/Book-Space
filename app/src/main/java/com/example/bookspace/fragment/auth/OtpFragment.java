package com.example.bookspace.fragment.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.model.model_class.remote.User;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.utils.Utils;
import com.example.bookspace.utils.Validation;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.auth.OtpViewModel;
import com.google.android.material.snackbar.Snackbar;

public class OtpFragment extends Fragment {
    private static final String TAG = "sayed";
    private EditText inputMobileNumber, inputOtp;
    private Button btnGetOtp, btnVerifyOtp;
    private ConstraintLayout layoutInput, layoutVerify;
    private OtpViewModel otpViewModel;
    private SharedViewModel sharedViewModel;
    private User registeringUser;
    private AlertDialog alertDialog;
    private NavController navController;
    private String updatingPhoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        inputMobileNumber = view.findViewById(R.id.editTextInputMobile);
        inputOtp = view.findViewById(R.id.editTextOTP);
        btnGetOtp = view.findViewById(R.id.buttonGetOTP);
        btnVerifyOtp = view.findViewById(R.id.buttonVerify);
        layoutInput = view.findViewById(R.id.getOTPLayout);
        layoutVerify = view.findViewById(R.id.verifyOTPLayout);

        if (getArgumentsData() == Utils.SIGNUP_FRAGMENT) {
            layoutVerify.setVisibility(View.VISIBLE);
            layoutInput.setVisibility(View.INVISIBLE);
        } else if (getArgumentsData() == Utils.SIGNIN_FRAGMENT) {
            layoutInput.setVisibility(View.VISIBLE);
            layoutVerify.setVisibility(View.INVISIBLE);
        }

        initListener();
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        otpViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(OtpViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(SharedViewModel.class);
        subscribeObserver();
        initBackPress();
    }

    private void initBackPress() {
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.popBackStack();
                        otpViewModel.clearObserver();
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

    private void initListener() {
        btnGetOtp.setOnClickListener(click -> {
            updatingPhoneNumber = inputMobileNumber.getText().toString();
            if (Validation.isNumberValid(updatingPhoneNumber)) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    otpViewModel.sendOtp(updatingPhoneNumber);
                } else shoSnackBar("No internet connection!");
            } else inputMobileNumber.setError("Invalid");
        });
        btnVerifyOtp.setOnClickListener(click -> {
            String otp = inputOtp.getText().toString().trim();
            if (otp.length() == 4) {
                if (getArgumentsData() == Utils.SIGNUP_FRAGMENT) {
                    if (NetworkInfo.hasNetwork(requireContext())) {
                        assert (registeringUser != null);
                        otpViewModel.verifyOtp(registeringUser.getPhoneNumber(), otp);
                    } else shoSnackBar("No internet connection!");
                } else if (getArgumentsData() == Utils.SIGNIN_FRAGMENT) {
                    if (NetworkInfo.hasNetwork(requireContext())) {
                        assert (updatingPhoneNumber != null);
                        otpViewModel.verifyOtp(updatingPhoneNumber, otp);
                    } else shoSnackBar("No internet connection!");
                }
            } else inputOtp.setError("Wrong");
        });
    }

    private void subscribeObserver() {
        sharedViewModel.observeRegisteringUser()
                .observe(getViewLifecycleOwner(), user -> {
                    if (user != null) registeringUser = user;
                });

        otpViewModel.observeVerifyOtp().removeObservers(getViewLifecycleOwner());
        otpViewModel.observeVerifyOtp().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("Something Went Wrong!");
                        otpViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        assert (resource.data != null);
                        if (resource.data) {
                            if (getArgumentsData() == Utils.SIGNUP_FRAGMENT)
                                otpViewModel.registerUser(registeringUser);
                            else if (getArgumentsData() == Utils.SIGNIN_FRAGMENT) {
                                showProgressDialog(false);
                                gotoResetPasswordFragment();
                            }
                            break;
                        } else {
                            showProgressDialog(false);
                            shoSnackBar("Wrong Otp Code!");
                            otpViewModel.clearObserver();
                            break;
                        }
                }
            }
        });

        otpViewModel.observeRegisterResult().removeObservers(getViewLifecycleOwner());
        otpViewModel.observeRegisterResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("Something Went Wrong!");
                        otpViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        assert (resource.data != null);
                        if (resource.data >= 1) {
                            showDialog();
                            break;
                        } else {
                            shoSnackBar("Wrong Otp Code!");
                            otpViewModel.clearObserver();
                            break;
                        }
                }
            }
        });

        otpViewModel.observeSendOtp().removeObservers(getViewLifecycleOwner());
        otpViewModel.observeSendOtp().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("Invalid Phone Number!");
                        break;
                    case SUCCESS:
                        assert (resource.data != null);
                        if (resource.data) {
                            showProgressDialog(false);
                            layoutInput.setVisibility(View.INVISIBLE);
                            layoutVerify.setVisibility(View.VISIBLE);
                            break;
                        }
                }
            }
        });
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            navController.navigate(R.id.action_otpFragment_to_loginFragment);
            otpViewModel.clearObserver();
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int getArgumentsData() {
        try {
            return getArguments().getInt("otp");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void gotoResetPasswordFragment() {
        assert (updatingPhoneNumber != null);
        Bundle bundle = new Bundle();
        bundle.putString(Utils.RESET_PASSWORD_KEY, updatingPhoneNumber);
        navController.navigate(R.id.action_otpFragment_to_resetPasswordFragment, bundle);
        otpViewModel.clearObserver();
    }
}
