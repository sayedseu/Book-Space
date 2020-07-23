package com.example.bookspace.fragment.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.utils.Utils;
import com.example.bookspace.view_model.auth.ResetPasswordViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ResetPasswordFragment extends Fragment {

    private static final String TAG = "sayed";
    private Button resetBT;
    private TextInputEditText passwordET, confirmPasswordET;
    private ResetPasswordViewModel resetPasswordViewModel;
    private NavController navController;
    private String password = "", confirmPassword = "";
    private String phoneNumber = "";
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        passwordET = view.findViewById(R.id.reset_password_textInputEditText);
        confirmPasswordET = view.findViewById(R.id.reset_confirmPassword_textInputEditText);
        resetBT = view.findViewById(R.id.resetPasswordBt);
        navController = Navigation.findNavController(view);
        initListener();
        initProgressDialog();

        assert getArguments() != null;
        phoneNumber = getArguments().getString(Utils.RESET_PASSWORD_KEY);
        Log.i(TAG, "onViewCreated: " + phoneNumber);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        resetPasswordViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(ResetPasswordViewModel.class);
        subscribeObserver();

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.navigate(R.id.action_resetPasswordFragment_to_loginFragment);
                        resetPasswordViewModel.clearObserver();
                    }
                });
    }

    private void initListener() {
        resetBT.setOnClickListener(click -> {
            if (isFieldValid()) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    resetPasswordViewModel.resetPassword(phoneNumber, password);
                } else shoSnackBar("No internet connection!");
            }
        });
    }

    private void subscribeObserver() {
        resetPasswordViewModel.observeResetPasswordResult().removeObservers(getViewLifecycleOwner());
        resetPasswordViewModel.observeResetPasswordResult().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("User not found");
                        resetPasswordViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        assert (resource.data != null);
                        if (resource.data) showDialog();
                }
            }
        });
    }

    private boolean isFieldValid() {
        boolean flag = true;
        try {
            password = passwordET.getText().toString();
            confirmPassword = confirmPasswordET.getText().toString();
        } catch (Exception e) {
        }

        if (password.isEmpty()) {
            passwordET.setError("Required");
            flag = false;
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordET.setError("Required");
            flag = false;
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordET.setError("Not Match");
            flag = false;
        }
        return flag;
    }

    private void shoSnackBar(String message) {
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

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            navController.navigate(R.id.action_resetPasswordFragment_to_loginFragment);
            resetPasswordViewModel.clearObserver();
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
