package com.example.bookspace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookspace.R;
import com.example.bookspace.app.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sayed";
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private AppBarLayout.LayoutParams params;
    private BottomNavigationView navigationView;
    private Toolbar toolbar;
    private CoordinatorLayout.LayoutParams navParam;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = SessionManager.getInstance(getApplicationContext());

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        navController = Navigation.findNavController(this, R.id.mainNavHost);

        appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.homeFragment, R.id.interestedFragment, R.id.uploadFragment, R.id.profileFragment, R.id.categoryFragment)
                .build();

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            getSupportActionBar().show();
            if (destination.getId() == R.id.reviewFragment || destination.getId() == R.id.bookDetailsFragment) {
                stopScrollingAppBar();
                hideBottomNavigation();
            } else if (destination.getId() == R.id.updateProfileFragment) {
                scrollAppBar();
                showBottomNavigation();
            } else if (destination.getId() == R.id.profileFragment) {
                stopScrollingAppBar();
                showBottomNavigation();
            } else if (destination.getId() == R.id.searchFragment) {
                stopScrollingAppBar();
                hideBottomNavigation();
                getSupportActionBar().hide();
            } else {
                scrollAppBar();
                showBottomNavigation();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_logout:
                showDialog();
                return true;
            case R.id.searchFragment:
                navController.navigate(R.id.searchFragment);
                return true;
            default:
                return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void scrollAppBar() {
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
    }

    private void stopScrollingAppBar() {
        params.setScrollFlags(0);
    }

    private void showBottomNavigation() {
        navigationView.setVisibility(View.VISIBLE);
    }

    private void hideBottomNavigation() {
        navigationView.setVisibility(View.GONE);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do You Want To Logout ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            sessionManager.logoutUser();
            startActivity(new Intent(this, AuthenticationActivity.class));
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
