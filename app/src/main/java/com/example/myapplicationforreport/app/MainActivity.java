package com.example.myapplicationforreport.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplicationforreport.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigation();
    }

    private void setupNavigation() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FragmentContainerView 사용 시 getSupportFragmentManager() 대신
        // NavHostFragment.findNavController() 로 가져와야 null 방지
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);

        if (navHostFragment == null) return;
        navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.listFragment,
                R.id.editFragment
        ).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}