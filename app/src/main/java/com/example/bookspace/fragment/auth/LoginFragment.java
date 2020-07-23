package com.example.bookspace.fragment.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.activity.MainActivity;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.SessionManager;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.utils.Utils;
import com.example.bookspace.view_model.auth.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private static final String TAG = "sayed";
    private TextView doNotHaveAnAccount, forgotPasswordBT;
    private Button loginBT;
    private ImageView googleBT, facebookBT;
    private TextInputEditText numberET, passwordET;
    private NavController navController;
    private LoginViewModel loginViewModel;
    private String username = "", password = "";
    private AlertDialog alertDialog;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView(view);
        initListener();
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        loginViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(LoginViewModel.class);
        sessionManager = SessionManager.getInstance(requireActivity().getApplicationContext());
        subscribeObserver();
    }

    private void subscribeObserver() {
        loginViewModel.observeAuthenticateResult().removeObservers(getViewLifecycleOwner());
        loginViewModel.observeAuthenticateResult().observe(getViewLifecycleOwner(), userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        shoSnackBar("Invalid user name or password!");
                        loginViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        if (userResource.data != null) {
                            Log.i(TAG, "subscribeObserver: " + userResource.data.toString());
                            sessionManager.createLoginSession(userResource.data);
                            startActivity(new Intent(requireActivity(), MainActivity.class));
                            requireActivity().finish();
                            break;
                        } else {
                            shoSnackBar("Invalid user name or password!");
                            loginViewModel.clearObserver();
                        }
                        break;

                }
            }
        });
    }

    private void initView(View view) {
        doNotHaveAnAccount = view.findViewById(R.id.doNotHaveAccountBT);
        loginBT = view.findViewById(R.id.signUpBT);
        googleBT = view.findViewById(R.id.googleBT);
        facebookBT = view.findViewById(R.id.facebookBT);
        numberET = view.findViewById(R.id.number_textInputEditText);
        passwordET = view.findViewById(R.id.password_textInputEditText);
        forgotPasswordBT = view.findViewById(R.id.forgotPasswordBT);
    }

    private void initListener() {
        loginBT.setOnClickListener(view -> {
            if (validateField()) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    loginViewModel.authenticate(username, password);
                } else shoSnackBar("No internet connection!");
            }
        });

        doNotHaveAnAccount.setOnClickListener(view -> {
            navController.navigate(R.id.action_loginFragment_to_signupFragment);
            loginViewModel.clearObserver();
        });

        forgotPasswordBT.setOnClickListener(click -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Utils.OTP_KEY, Utils.SIGNIN_FRAGMENT);
            navController.navigate(R.id.action_loginFragment_to_otpFragment, bundle);
            loginViewModel.clearObserver();
        });
    }

    private boolean validateField() {
        boolean flag = true;

        try {
            username = numberET.getText().toString();
            password = passwordET.getText().toString();
        } catch (NullPointerException e) {

        }

        if (username.trim().isEmpty()) {
            numberET.setError("Required");
            flag = false;
        }
        if (password.trim().isEmpty()) {
            passwordET.setError("Required");
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

}
