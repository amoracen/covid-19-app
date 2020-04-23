package fau.amoracen.covid_19update.ui.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.HashMap;
import java.util.Objects;

import fau.amoracen.covid_19update.service.FirebaseUtil;
import fau.amoracen.covid_19update.ui.homeActivity.HomeActivity;
import fau.amoracen.covid_19update.R;

/**
 * Manages Login Form
 */
public class LoginFragment extends Fragment implements FirebaseUtil.FirebaseUtilListener {

    private EditText emailEditText, passwordEditText;
    private String email, password;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);

        CheckBox passwordCheckBox = view.findViewById(R.id.showHidePasswordCheckBox);
        passwordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //Show Password
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //Hide Password
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmailAndPassword()) {
                    signIn();
                }
            }
        });

        TextView registerTextView = view.findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_RegistrationPassword, null);
            }
        });
    }

    /**
     * Connect with Firebase to sign in
     */
    private void signIn() {
        FirebaseUtil.getInstance().signInWithEmailAndPassword(email, password, this);
    }

    /**
     * Validate Email and Password
     *
     * @return true if both are valid
     */
    private boolean checkEmailAndPassword() {
        email = emailEditText.getText().toString();

        HashMap hmap = FirebaseUtil.getInstance().validateEmail(getContext(), email);
        if (!Objects.equals(hmap.get("message"), "Valid")) {
            emailEditText.setError(Objects.requireNonNull(hmap.get("message")).toString());
            emailEditText.setText("");
            emailEditText.requestFocus();
            return false;
        }
        password = passwordEditText.getText().toString();
        hmap = FirebaseUtil.getInstance().validatePassword(getContext(), password);
        if (!Objects.equals(hmap.get("message"), "Valid")) {
            passwordEditText.setError(Objects.requireNonNull(hmap.get("message")).toString());
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Get result from Firebase sign in listener
     *
     * @param result a string
     */
    @Override
    public void onCompleteSendResult(String result) {
        if (result.contains("Success")) {
            /*Go to Home Screen*/
            Intent goToHomeScreen = new Intent(getContext(), HomeActivity.class);
            startActivity(goToHomeScreen);
            Objects.requireNonNull(getActivity()).finishAffinity();
        } else {
            Log.i("Failed Login", result);
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
