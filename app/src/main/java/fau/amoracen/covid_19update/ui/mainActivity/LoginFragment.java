package fau.amoracen.covid_19update.ui.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Objects;

import fau.amoracen.covid_19update.ui.homeActivity.HomeActivity;
import fau.amoracen.covid_19update.R;

public class LoginFragment extends Fragment {

    private EditText emailEditText, passwordEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
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
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    /*TODO VALIDATE EMAIL*/
                    emailEditText.setError("Please enter your email address");
                    emailEditText.setText("");
                    emailEditText.requestFocus();
                } else if (password.isEmpty()) {
                    passwordEditText.setError("Please enter your password");
                    passwordEditText.setText("");
                    passwordEditText.requestFocus();
                } else {
                    /*TODO FIREBASE*/
                    //Go to Dashboard
                    Intent goToDashboard = new Intent(getContext(), HomeActivity.class);
                    startActivity(goToDashboard);
                    Objects.requireNonNull(getActivity()).finishAffinity();
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
}
