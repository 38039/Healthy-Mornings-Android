package com.nforge.healthymornings.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.nforge.healthymornings.R;
import com.nforge.healthymornings.databinding.ActivityMainBinding;

import com.nforge.healthymornings.model.fragment.AboutUsFragment;
import com.nforge.healthymornings.model.fragment.AccountEditFragment;
import com.nforge.healthymornings.model.fragment.PasswordEditFragment;
import com.nforge.healthymornings.model.fragment.StatisticsFragment;
import com.nforge.healthymornings.model.fragment.TaskAddFragment;
import com.nforge.healthymornings.model.fragment.TaskAllFragment;
import com.nforge.healthymornings.model.fragment.TaskListFragment;
import com.nforge.healthymornings.model.repository.UserRepository;
import com.nforge.healthymornings.model.utils.SessionManager;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;
    private SessionManager sessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        sessionHandler = new SessionManager( getApplicationContext() );

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        // Pokaż domyślny fragment (TaskListFragment)
        if (savedInstanceState == null)
            loadFragment(new TaskListFragment());

        toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.nav_open,
                R.string.nav_close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.taskListFragment)
            selectedFragment = new TaskListFragment();

        if (item.getItemId() == R.id.taskAddFragment)
            selectedFragment = new TaskAddFragment();

        if (item.getItemId() == R.id.aboutUsFragment)
            selectedFragment = new AboutUsFragment();

        if (item.getItemId() == R.id.shareFragment)
            selectedFragment = new StatisticsFragment();

        if (item.getItemId() == R.id.accountEditFragment)
            selectedFragment = new AccountEditFragment();

        if (item.getItemId() == R.id.passwordEditFragment)
            selectedFragment = new PasswordEditFragment();

        if (item.getItemId() == R.id.nav_logout) {
            UserRepository userRepository = new UserRepository(getApplicationContext());
            if (!userRepository.logoutUser()) {
                Toast.makeText(this, "Nie udało się wylogować użytkownika", Toast.LENGTH_LONG).show();
                return false;
            }
            getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().clear().apply();
            Log.v("MainActivity", "onNavigationItemSelected(): Wylogowano użytkownika");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (selectedFragment != null)
            loadFragment(selectedFragment);

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        //  Implementacja backstacka doprowadza do sytuacji gdzie po zabiciu fragmentu użytkownik nie powcałby do listy zadań
        //  transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
