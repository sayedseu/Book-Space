package com.example.bookspace.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.app.Injection;
import com.example.bookspace.app.SessionManager;
import com.example.bookspace.app.ViewModelProviderFactory;
import com.example.bookspace.enum_class.Education;
import com.example.bookspace.model.model_class.remote.User;
import com.example.bookspace.utils.NetworkInfo;
import com.example.bookspace.view_model.profile.UpdateProfileViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class UpdateProfileFragment extends Fragment {
    private static final String TAG = "sayed";
    private SessionManager sessionManager;
    private User user;
    private NavController navController;
    private UpdateProfileViewModel updateProfileViewModel;
    private TextInputEditText nameET, passwordET, confirmPasswordET;
    private String name = "", password = "", confirmPassword = "";
    private RadioGroup studentQuestionRG, educationLevelRG;
    private boolean isStudent;
    private String educationLevel;
    private Button updateProfileBT, updatePasswordBT;
    private AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView(view);
        initListenerButton();
        initProgressDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviderFactory providerFactory = Injection.provideViewModelProviderFactory(requireActivity().getApplication());
        updateProfileViewModel = new ViewModelProvider(requireActivity(), providerFactory).get(UpdateProfileViewModel.class);
        sessionManager = SessionManager.getInstance(requireContext());
        setUI(sessionManager.getUserDetails());
        subscribeObserver();
    }

    private void subscribeObserver() {
        updateProfileViewModel.observerProfileUpdating().removeObservers(getViewLifecycleOwner());
        updateProfileViewModel.observerProfileUpdating().observe(getViewLifecycleOwner(), userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        showSnackBar("Something went wrong. Please try again later");
                        updateProfileViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        if (userResource.data != null) {
                            sessionManager.createLoginSession(userResource.data);
                            showDialog();
                        } else {
                            showSnackBar("Something went wrong. Please try again later");
                            updateProfileViewModel.clearObserver();
                        }
                        break;
                }
            }
        });

        updateProfileViewModel.observerPasswordUpdating().removeObservers(getViewLifecycleOwner());
        updateProfileViewModel.observerPasswordUpdating().observe(getViewLifecycleOwner(), userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case LOADING:
                        showProgressDialog(true);
                        break;
                    case ERROR:
                        showProgressDialog(false);
                        showSnackBar("Something went wrong. Please try again later");
                        updateProfileViewModel.clearObserver();
                        break;
                    case SUCCESS:
                        showProgressDialog(false);
                        if (userResource.data != null) {
                            sessionManager.createLoginSession(userResource.data);
                            showDialog();
                        } else {
                            showSnackBar("Something went wrong. Please try again later");
                            updateProfileViewModel.clearObserver();
                        }
                        break;
                }
            }
        });
    }

    private void initListenerButton() {
        updateProfileBT.setOnClickListener(click -> {
            if (validateProfileFlied()) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    User userDetails = sessionManager.getUserDetails();
                    userDetails.setName(name);
                    userDetails.setStudent(isStudent);
                    userDetails.setEducationLevel(educationLevel);
                    updateProfileViewModel.updateProfile(userDetails);
                } else showSnackBar("No internet connection!");
            }
        });

        updatePasswordBT.setOnClickListener(click -> {
            if (validatePasswordFlied()) {
                if (NetworkInfo.hasNetwork(requireContext())) {
                    updateProfileViewModel.updatePassword(sessionManager.getUserDetails().getPhoneNumber(), password);
                } else showSnackBar("No internet connection!");
            }
        });

        studentQuestionRG.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radio_yes:
                    isStudent = true;
                    return;
                case R.id.radio_no:
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


    private void initView(View view) {
        updateProfileBT = view.findViewById(R.id.updateProfileBT);
        updatePasswordBT = view.findViewById(R.id.updatePasswordBT);
        nameET = view.findViewById(R.id.profileNameET);
        passwordET = view.findViewById(R.id.profilePasswordET);
        confirmPasswordET = view.findViewById(R.id.profileConfirmPasswordET);
        studentQuestionRG = view.findViewById(R.id.studentQuestionRG);
        educationLevelRG = view.findViewById(R.id.educationLevelRG);
    }

    private void setUI(User user) {
        isStudent = user.getStudent();
        educationLevel = user.getEducationLevel();
        nameET.setText(user.getName());
        if (isStudent) studentQuestionRG.check(R.id.radio_yes);
        else studentQuestionRG.check(R.id.radio_no);
        if (educationLevel.equals(Education.SCHOOL.getEducation()))
            educationLevelRG.check(R.id.radio_school);
        else if (educationLevel.equals(Education.COLLAGE.getEducation()))
            educationLevelRG.check(R.id.radio_university);
        else if (educationLevel.equals(Education.UNIVERSITY.getEducation()))
            educationLevelRG.check(R.id.radio_university);
        else if (educationLevel.equals(Education.NONE.getEducation()))
            educationLevelRG.check(R.id.radio_none);
    }

    private void clearField() {
        nameET.setText(null);
        passwordET.setText(null);
        confirmPasswordET.setText(null);
        educationLevelRG.clearCheck();
        studentQuestionRG.clearCheck();
    }

    private boolean validateProfileFlied() {
        boolean flag = true;
        try {
            name = nameET.getText().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (name.trim().isEmpty()) {
            nameET.setError("Required");
            flag = false;
        }

        return flag;
    }

    private boolean validatePasswordFlied() {
        boolean flag = true;
        try {
            password = passwordET.getText().toString();
            confirmPassword = confirmPasswordET.getText().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
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
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.success_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("Ok", (dialogInterface, i) -> {
            navController.popBackStack();
            clearField();
            updateProfileViewModel.clearObserver();
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
