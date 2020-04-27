package fau.amoracen.covid_19update.ui.homeActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.muddzdev.styleabletoast.StyleableToast;

import fau.amoracen.covid_19update.R;
import fau.amoracen.covid_19update.service.CustomAlertDialog;
import fau.amoracen.covid_19update.service.FirebaseUtil;
import fau.amoracen.covid_19update.ui.homeActivity.countries.CountriesFragment;
import fau.amoracen.covid_19update.ui.homeActivity.news.NewsFragment;
import fau.amoracen.covid_19update.ui.homeActivity.ourWorldInData.WorldInDataFragment;
import fau.amoracen.covid_19update.ui.homeActivity.us_states.USStatesFragment;
import fau.amoracen.covid_19update.ui.homeActivity.world.WorldFragment;
import fau.amoracen.covid_19update.ui.mainActivity.MainActivity;

/**
 * Controls navigation between fragments
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CustomAlertDialog.CustomAlertDialogListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private AuthCredential authCredential;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        // get email textView
        TextView userEmail = headerView.findViewById(R.id.userEmailTextView);
        // set email
        userEmail.setText(FirebaseUtil.getInstance().getCurrentUser().getEmail());

        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

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
                        new USStatesFragment()).commit();
                break;
            case R.id.nav_OurWorldInData:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WorldInDataFragment()).commit();
                break;
            case R.id.nav_newsPage:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NewsFragment()).commit();
                break;
            case R.id.nav_logout:
                // Method that allows logout
                FirebaseUtil.getInstance().signOut();
                GoogleLogout();
                returnToMainActivity();
                break;
            case R.id.nav_delete:
                // Method that allows Delete Account
                createAlert();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Closes Activity and returns to main
     */
    public void returnToMainActivity() {
        Intent inToMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(inToMain);
        finish();
    }


    /**
     * Create an Alert to Delete Account
     */
    public void createAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle("Delete Account?")
                .setMessage("Deleting your account removes it from the system.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkUser();
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new WorldFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_worldwidePage);
                    }
                }).show();
    }

    /**
     * Check what type of account was created
     */
    private void checkUser() {
        if (account != null) {
            setAuthCredentialGoogle(account);
            Log.i("Google", account.getEmail());
        } else {
            /*Get Email and Password*/
            Log.i("Email Password", "Open Alert");
            openCustomAlert();
        }
    }

    /**
     * Sign out the Google account
     */
    public void GoogleLogout() {
        if (account == null) return;
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        googleSignInClient.signOut();
    }

    /**
     * Set auth Credential if a Google Account was found
     *
     * @param account google account
     */
    private void setAuthCredentialGoogle(GoogleSignInAccount account) {
        authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        deleteAccount();
    }

    /*
     *Set auth Credentials if an Email and Password were provided to create account
     */
    private void setAuthCredentialEmail(String email, String password) {
        authCredential = EmailAuthProvider.getCredential(email, password);
        deleteAccount();
    }

    /**
     * Open Custom Alert Dialog to Get email and Password
     */
    private void openCustomAlert() {
        CustomAlertDialog customAlertDialog = new CustomAlertDialog();
        customAlertDialog.show(getSupportFragmentManager(), "Login Dialog");
    }

    /**
     * Delete Account in Firebase
     */
    private void deleteAccount() {
        if (authCredential == null) return;
        final FirebaseUser user = FirebaseUtil.getInstance().getCurrentUser();
        user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (account != null) {
                                    GoogleLogout();
                                }
                                // Method that allows logout
                                FirebaseUtil.getInstance().signOut();
                                Log.d("Deleted", "User account deleted!");
                                StyleableToast.makeText(getApplicationContext(), "Account Deleted", R.style.ToastPositive).show();
                                returnToMainActivity();
                            } else {
                                StyleableToast.makeText(getApplicationContext(), task.getException().getMessage(), R.style.ToastError).show();
                            }
                        }
                    });
                } else {
                    StyleableToast.makeText(getApplicationContext(), task.getException().getMessage(), R.style.ToastError).show();
                }
            }
        });
    }

    /**
     * Listener use to get email and password from Alert Dialog
     *
     * @param email    a string
     * @param password a string
     */
    @Override
    public void applyTexts(String email, String password) {
        setAuthCredentialEmail(email, password);
        Log.i("Email", email);
        Log.i("Password", password);
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
