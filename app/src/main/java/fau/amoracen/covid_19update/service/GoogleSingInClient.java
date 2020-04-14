package fau.amoracen.covid_19update.service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import fau.amoracen.covid_19update.R;

/**
 * Starts Intent to sign in using a google account
 */
public class GoogleSingInClient {
    private GoogleSignInClient mGoogleSignInClient;
    public static final int GOOGLE_SIGN_IN = 123;
    private FirebaseUtil.FirebaseUtilListener firebaseUtilListener;
    private Activity activity;

    /**
     * Constructor
     *
     * @param activity             activity implementing this class
     * @param firebaseUtilListener listener used to return result
     */
    public GoogleSingInClient(Activity activity, FirebaseUtil.FirebaseUtilListener firebaseUtilListener) {
        this.activity = activity;
        this.firebaseUtilListener = firebaseUtilListener;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /**
     * Start Intent to Sign in
     */
    public void signIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    /**
     * Get user's response
     *
     * @param data object returned by the intent
     */
    public void handleResponse(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                Log.i("TAG", "GoogleSignInAccount Success");
                FirebaseUtil.getInstance().firebaseAuthWithGoogle(account, firebaseUtilListener);
            }
        } catch (ApiException e) {
            FirebaseUtil.getInstance().firebaseAuthWithGoogle(null, firebaseUtilListener);
            Log.w("TAG", "Google sign in failed", e);
        }
    }
}
