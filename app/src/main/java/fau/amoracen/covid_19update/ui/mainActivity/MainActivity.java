package fau.amoracen.covid_19update.ui.mainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.Toast;

import fau.amoracen.covid_19update.R;

/**
 * MainActivity displays the welcome screen
 */
public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if (Navigation.findNavController(this, R.id.my_nav_host_fragment).getCurrentDestination().getId() == R.id.loginFragment
                || Navigation.findNavController(this, R.id.my_nav_host_fragment).getCurrentDestination().getId() == R.id.registrationFragment) {
            // handle back button
            Navigation.findNavController(this, R.id.my_nav_host_fragment).navigate(R.id.welcomeFragment);
            return;
        } else if (Navigation.findNavController(this, R.id.my_nav_host_fragment).getCurrentDestination().getId() == R.id.welcomeFragment) {
            this.finishAffinity();
            return;
        }
        super.onBackPressed();
    }
}
