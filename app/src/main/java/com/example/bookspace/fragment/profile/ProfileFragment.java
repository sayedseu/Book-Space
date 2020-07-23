package com.example.bookspace.fragment.profile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookspace.R;
import com.example.bookspace.activity.AuthenticationActivity;
import com.example.bookspace.app.SessionManager;
import com.example.bookspace.model.model_class.remote.User;

public class ProfileFragment extends Fragment {
    private static final String TAG = "sayed";
    private SessionManager sessionManager;
    private NavController navController;
    private TextView nameTV, phoneTV, emailTV;
    private CheckBox listCB, updateCB, workCB, faqCB, logoutCB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionManager = SessionManager.getInstance(requireContext());
        updateUI(sessionManager.getUserDetails());
        addListenerOnCheckBox();
    }


    private void addListenerOnCheckBox() {
        listCB.setOnClickListener(click -> {
            navController.navigate(R.id.action_profileFragment_to_interestedFragment);
            clearCB();
        });
        updateCB.setOnClickListener(click -> {
            navController.navigate(R.id.action_profileFragment_to_updateProfileFragment);
            clearCB();
        });
        workCB.setOnClickListener(click -> {
        });
        faqCB.setOnClickListener(click -> {

        });
        logoutCB.setOnClickListener(view -> {
            showDialog();
            clearCB();
        });
    }

    private void clearCB() {
        listCB.setChecked(false);
        updateCB.setChecked(false);
        workCB.setChecked(false);
        faqCB.setChecked(false);
        logoutCB.setChecked(false);
    }

    private void initView(View view) {
        nameTV = view.findViewById(R.id.profileNameTV);
        phoneTV = view.findViewById(R.id.profileNumberTV);
        emailTV = view.findViewById(R.id.profileEmailTV);
        listCB = view.findViewById(R.id.listCB);
        updateCB = view.findViewById(R.id.updateCB);
        workCB = view.findViewById(R.id.worksCB);
        faqCB = view.findViewById(R.id.faqCB);
        logoutCB = view.findViewById(R.id.logoutCB);
    }

    private void updateUI(User user) {
        Log.i(TAG, "updateUI: profile fragment current user" + user.toString());
        nameTV.setText(user.getName());
        phoneTV.setText(user.getPhoneNumber());
        emailTV.setText(user.getEmail());
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout.");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            sessionManager.logoutUser();
            startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
            requireActivity().finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.searchFragment) {
            Log.i(TAG, "onOptionsItemSelected: ");
            navController.navigate(R.id.searchFragment);
        }
        return super.onOptionsItemSelected(item);
    }
}
