package fau.amoracen.covid_19update.ui.mainActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import fau.amoracen.covid_19update.R;

/**
 * WelcomeFragment displays the welcome message and
 * the buttons to navigate to registration
 * ,login with email or login with Google.
 */
public class WelcomeFragment extends Fragment {

    private WelcomeFragmentListener WelcomeFragmentListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = view.findViewById(R.id.loginWithEmailButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_loginFragment, null);
            }
        });
        TextView registerTextView = view.findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_registrationFragment, null);
            }
        });
        Button loginWithGoogle = view.findViewById(R.id.loginWithGoogleButton);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        loginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                WelcomeFragmentListener.onClickEvent(getView());
            }
        });
    }

    /**
     * Interface used in MainActivity to Login with Google Account
     */
    public interface WelcomeFragmentListener {
        void onClickEvent(View view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WelcomeFragmentListener) {
            WelcomeFragmentListener = (WelcomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WelcomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        WelcomeFragmentListener = null;
    }
}
