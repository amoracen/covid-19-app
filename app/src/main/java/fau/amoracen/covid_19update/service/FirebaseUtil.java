package fau.amoracen.covid_19update.service;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import fau.amoracen.covid_19update.R;

/**
 * A class to manage FirebaseUtil
 */
public class FirebaseUtil {
    private static FirebaseAuth myFirebaseAuth;
    private static FirebaseUtil firebaseUtil;
    private FirebaseUtilListener firebaseUtilListener;

    /**
     * Constructor
     */
    private FirebaseUtil() {
    }

    /**
     * Get Instance
     *
     * @return firebaseUtil object
     */
    public static synchronized FirebaseUtil getInstance() {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            myFirebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseUtil;
    }

    /**
     * Get Firebase user
     *
     * @return FirebaseAuth
     */
    public FirebaseAuth getMyFirebaseAuth() {
        return myFirebaseAuth;
    }

    /**
     * Get current user
     * Null if the user does not exist
     *
     * @return an instance of the current user
     */
    public FirebaseUser getCurrentUser() {
        return myFirebaseAuth.getCurrentUser();
    }

    /**
     * Sign out user
     */
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }


    /**
     * Asynchronously signs in with the given credentials.
     *
     * @param acct                 google account
     * @param firebaseUtilListener listener used to return result
     */
    void firebaseAuthWithGoogle(GoogleSignInAccount acct, final FirebaseUtilListener firebaseUtilListener) {
        this.firebaseUtilListener = firebaseUtilListener;
        if (acct == null) {
            firebaseUtilListener.onCompleteSendResult("Failed to login with Google Account");
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        myFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "signInWithCredential:success");
                    firebaseUtilListener.onCompleteSendResult("Success");
                } else {
                    String error = getError(task, "Failed to login with Google Account");
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    firebaseUtilListener.onCompleteSendResult(error);
                }
            }
        });
    }


    /**
     * Create a new account using email and password
     *
     * @param email                a string
     * @param password             a string
     * @param firebaseUtilListener listener used to return result
     */
    public void createUserWithEmailAndPassword(String email, String password, final FirebaseUtilListener firebaseUtilListener) {
        this.firebaseUtilListener = firebaseUtilListener;
        myFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /*Account Created*/
                    firebaseUtilListener.onCompleteSendResult("Success");
                } else {
                    String error = getError(task, "Failed to create new account");
                    Log.w("TAG", "Failed to create new account:failure", task.getException());
                    firebaseUtilListener.onCompleteSendResult(error);
                }
            }
        });
    }

    /**
     * Sign in using email and password
     *
     * @param email                a string
     * @param password             a string
     * @param firebaseUtilListener listener used to return result
     */
    public void signInWithEmailAndPassword(String email, String password, final FirebaseUtilListener firebaseUtilListener) {
        this.firebaseUtilListener = firebaseUtilListener;

        myFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "signInWithEmailAndPassword:success");
                    firebaseUtilListener.onCompleteSendResult("Success");
                } else {
                    String error = getError(task, "Failed to sign in");
                    Log.w("TAG", "Failed to sign in:failure", task.getException());
                    firebaseUtilListener.onCompleteSendResult(error);
                }
            }
        });
    }

    /**
     * Get error from AuthResult task
     *
     * @param task           AuthResult object
     * @param defaultMessage default message if error is not found
     * @return a string representing the error
     */
    private String getError(Task<AuthResult> task, String defaultMessage) {
        String msg = defaultMessage;
        if (task.getException() instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
            switch (errorCode) {
                case "ERROR_INVALID_CREDENTIAL":
                    msg = "INVALID CREDENTIAL";
                    break;
                case "ERROR_INVALID_EMAIL":
                    msg = "INVALID EMAIL";
                    break;
                case "ERROR_WRONG_PASSWORD":
                    msg = "Password is incorrect";
                    break;
                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                    msg = "An account already exists with the same email address";
                    break;
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    msg = "The email address is already in use by another account.";
                    break;
                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                    msg = "Credential are already associated with a different user account.";
                    break;
                case "ERROR_USER_DISABLED":
                    msg = "The user account has been disabled by an administrator.";
                    break;
                case "ERROR_USER_NOT_FOUND":
                    msg = "There is no user record corresponding to the email.";
                    break;
            }
        } else {
            try {
                throw Objects.requireNonNull(task.getException());
            } catch (FirebaseAuthInvalidUserException e) {
                msg = "Invalid Email";
            } catch (FirebaseAuthInvalidCredentialsException e) {
                msg = "Invalid Password";
            } catch (FirebaseNetworkException e) {
                msg = "Failed to login.No Network available";
            } catch (Exception e) {
                Log.e("Failed", Objects.requireNonNull(e.getMessage()));
            }
        }
        return msg;
    }


    /**
     * Validate Email
     *
     * @param context application context
     * @param email   a string
     * @return HashMap with result from checking email
     */
    public HashMap validateEmail(Context context, String email) {
        HashMap<String, String> hmap = new HashMap<>();
        if (email.isEmpty()) {
            hmap.put("message", context.getString(R.string.email_cannot_be_empty));
            return hmap;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            hmap.put("message", context.getString(R.string.email_not_valid));
            return hmap;
        }
        hmap.put("message", "Valid");
        return hmap;
    }

    /**
     * Check Password
     *
     * @param input a string
     * @return HashMap with result from checking password
     */
    public HashMap validatePassword(Context context, String input) {
        HashMap<String, String> hmap = new HashMap<>();
        Pattern anyLetter = Pattern.compile("(?=.*[a-zA-Z])");
        Pattern oneDigit = Pattern.compile("(?=.*[0-9])");
        Pattern noWhiteSpace = Pattern.compile("\\s");
        Pattern totalCharacters = Pattern.compile(".{6,}");
        /*Checking Password*/
        if (input.isEmpty()) {
            hmap.put("message", context.getString(R.string.field_cannot_be_empty));
            return hmap;
        } else if (!anyLetter.matcher(input).find()) {
            hmap.put("message", context.getString(R.string.password_letters));
            return hmap;
        } else if (!oneDigit.matcher(input).find()) {
            hmap.put("message", context.getString(R.string.password_digits));
            return hmap;
        } else if (noWhiteSpace.matcher(input).find()) {
            hmap.put("message", context.getString(R.string.password_whiteSpaces));
            return hmap;
        } else if (!totalCharacters.matcher(input).find()) {
            hmap.put("message", context.getString(R.string.password_characters));
            return hmap;
        }
        hmap.put("message", "Valid");
        return hmap;
    }

    /**
     * Interface used to return results
     */
    public interface FirebaseUtilListener {
        void onCompleteSendResult(String result);
    }
}
