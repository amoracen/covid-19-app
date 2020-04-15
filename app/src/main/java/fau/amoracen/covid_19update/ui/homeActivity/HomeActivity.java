package fau.amoracen.covid_19update.ui.homeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import fau.amoracen.covid_19update.service.FirebaseUtil;
import fau.amoracen.covid_19update.ui.homeActivity.countries.CountriesFragment;
import fau.amoracen.covid_19update.ui.homeActivity.news.NewsFragment;
import fau.amoracen.covid_19update.ui.homeActivity.us_states.USstatesFragment;
import fau.amoracen.covid_19update.ui.homeActivity.world.WorldFragment;
import fau.amoracen.covid_19update.ui.mainActivity.MainActivity;
import fau.amoracen.covid_19update.R;

/**
 * Controls navigation between fragments
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // get username email textView
        TextView userEmail = headerView.findViewById(R.id.userEmailTextView);
        // set username
        //userEmail.setText(mFireBaseAuth.getCurrentUser().getEmail());
        userEmail.setText("email@gmail.com");

        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new WorldFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_worldwidePage);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_worldwidePage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorldFragment()).commit();
                break;
            case R.id.nav_CountriesPage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CountriesFragment()).commit();
                break;
            case R.id.nav_USStatesPage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new USstatesFragment()).commit();
                break;
            case R.id.nav_newsPage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NewsFragment()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                // Method that allows you to logout via  logout button
                //mFireBaseAuth.getInstance().signOut();
                FirebaseUtil.getInstance().signOut();
                Intent inToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inToMain);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
