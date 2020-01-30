package com.example.android.myfavlocalesapp.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.android.myfavlocalesapp.R;
import com.example.android.myfavlocalesapp.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    @BindView(R.id.email_address_edittext)
    EditText loginEmailEditText;

    @BindView(R.id.password_edittext)
    EditText loginPasswordEditText;

    @BindView(R.id.signup_email_address_edittext)
    EditText signUpEmailEditText;

    @BindView(R.id.signup_username_edittext)
    EditText signUpUserNameEditText;

    @BindView(R.id.signup_password_edittext)
    EditText signUpPasswordEditText;

    @BindView(R.id.signup_verify_password_edittext)
    EditText signUpVerifyPasswordEditText;

    @BindView(R.id.signup_layout)
    ConstraintLayout signUpLayout;

    @BindView(R.id.login_layout)
    ConstraintLayout logInLayout;

    private LoginDelegator loginDelegator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragement_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

    }


    private boolean checkloginUserInput() {
        if (loginEmailEditText.getText().toString().trim().length() == 0
                || loginPasswordEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    private boolean checkSignUpUserInput() {
        if (signUpEmailEditText.getText().toString().trim().length() == 0
                || signUpPasswordEditText.getText().toString().trim().length() == 0) {

            Toast.makeText(getContext(), "Field cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;

        } else if (signUpPasswordEditText.getText().toString().trim().length() < 8) {

            Toast.makeText(getContext(), "Password must be longer than 8 characters",
                    Toast.LENGTH_SHORT).show();
            return false;

        } else if (!signUpPasswordEditText.getText().toString().trim()
                .equals(signUpVerifyPasswordEditText.getText().toString().trim())) {

            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;

        } else
            return true;
    }

    @OnClick(R.id.login_button)
    public void loginUser() {
        if (checkloginUserInput()) {
            String userName = signUpUserNameEditText.getText().toString().trim();
            String emailAddress = loginEmailEditText.getText().toString().trim();
            String password = loginPasswordEditText.getText().toString().trim();

            loginDelegator.loginUser(new User(emailAddress, password));

        }
    }

    @OnClick(R.id.signup_button)
    public void signUpUser() {
        if (checkSignUpUserInput()) {
            String userName = signUpUserNameEditText.getText().toString().trim();
            String emailAddress = signUpEmailEditText.getText().toString().trim();
            String password = signUpPasswordEditText.getText().toString().trim();
            loginDelegator.signUpNewUser(new User(emailAddress, password));
        }
    }

    @OnClick(R.id.signup_textview)
    public void openSignUp() {
        signUpLayout.setVisibility(View.VISIBLE);
        logInLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.signup_back_imageview)
    public void onCloseSignUp() {
        signUpLayout.setVisibility(View.GONE);
        logInLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loginDelegator = (MapsActivity) context;

    }

    interface LoginDelegator {
        void signUpNewUser(User signUpUser);

        void loginUser(User loginUser);
    }
}
