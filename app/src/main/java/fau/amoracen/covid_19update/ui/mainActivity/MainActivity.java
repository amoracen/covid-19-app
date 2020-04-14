package fau.amoracen.covid_19update.ui.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.service.FirebaseUtil;
import fau.amoracen.covid_19update.service.GoogleSingInClient;
import fau.amoracen.covid_19update.ui.homeActivity.HomeActivity;

/**
 * MainActivity shows the WelcomeFragment
 * Manages Google Sing In
 */
public class MainActivity extends AppCompatActivity implements FirebaseUtil.FirebaseUtilListener, WelcomeFragment.WelcomeFragmentListener {
    private GoogleSingInClient googleSingInClient;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    /**
     * Listener for Google Sing In Button
     *
     * @param view a view object
     */
    @Override
    public void onClickEvent(View view) {
        this.view = view;
        googleSingInClient = new GoogleSingInClient(this, this);
        googleSingInClient.signIn();
    }

    /**
     * Get result from Google Sing in Intent
     *
     * @param requestCode an int
     * @param resultCode  an int
     * @param data        intent object
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GoogleSingInClient.GOOGLE_SIGN_IN) {
            googleSingInClient.handleResponse(data);
        }
    }

    /**
     * Listener for Firebase
     * Go to Home Screen if the user logged in successfully
     * Show error if login failed
     *
     * @param result a string
     */
    @Override
    public void onCompleteSendResult(String result) {
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        if (result.contains("Success")) {
            //If the user is logged in, go to Home Activity
            goToHomeScreen();
        } else {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Intent to Go to Home Screen
     */
    public void goToHomeScreen() {
        Intent goToHomeScreen = new Intent(this, HomeActivity.class);
        startActivity(goToHomeScreen);
        Objects.requireNonNull(this).finishAffinity();
    }

    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(Navigation.findNavController(this, R.id.my_nav_host_fragment).getCurrentDestination()).getId() == R.id.loginFragment
                || Objects.requireNonNull(Navigation.findNavController(this, R.id.my_nav_host_fragment).getCurrentDestination()).getId() == R.id.registrationFragment) {
            // handle back button
            Navigation.findNavController(this, R.id.my_nav_host_fragment).navigate(R.id.welcomeFragment);
            return;
        } else if (Objects.requireNonNull(Navigation.findNavController(this, R.id.my_nav_host_fragment).getCurrentDestination()).getId() == R.id.welcomeFragment) {
            this.finishAffinity();
            return;
        }
        super.onBackPressed();
    }


    /**
     * Checks if the user is logged in
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseUtil.getInstance().getCurrentUser() != null) {
            //If the user is logged in, go to Home Activity
            goToHomeScreen();
        }
    }
}
