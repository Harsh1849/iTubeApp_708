package com.itube.app;

import static com.itube.app.Extensions.adjustFullScreen;
import static com.itube.app.Extensions.checkEmptyString;
import static com.itube.app.Extensions.hideKeyboard;
import static com.itube.app.Extensions.showToast;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.itube.app.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        manageClicks();
        databaseHelper = new DatabaseHelper(this);
    }

    private void manageClicks() {
        binding.signUpCardView.setOnClickListener(v -> {
            if (isValid()) {
                hideKeyboard(this, binding.getRoot());
                boolean checkName = databaseHelper.CheckUserName(Objects.requireNonNull(binding.usernameTiet.getText()).toString());

                if (!checkName) {
                    ModelClass model = new ModelClass(Objects.requireNonNull(binding.fullNameTiet.getText()).toString(), binding.usernameTiet.getText().toString(), Objects.requireNonNull(binding.passwordTiet.getText()).toString(), Objects.requireNonNull(binding.confirmPasswordTiet.getText()).toString());

                    model.setFullName(binding.fullNameTiet.getText().toString());
                    model.setUserName(binding.usernameTiet.getText().toString());
                    model.setPassword(binding.passwordTiet.getText().toString());
                    model.setCpassword(binding.confirmPasswordTiet.getText().toString());
                    boolean insertData = databaseHelper.InsertUserData(model);

                    if (insertData) {
                        showToast(this, "User Register Successfully");
                        finish();
                    }
                } else {
                    showToast(this, "User Already Exists!");
                }
            }
        });
    }

    private boolean isValid() {
        if (checkEmptyString(binding.fullNameTiet.getText())) {
            binding.fullNameTil.setErrorEnabled(true);
            binding.fullNameTil.setError(getString(R.string.please_enter_fullName));
            return false;
        } else if (checkEmptyString(binding.usernameTiet.getText())) {
            binding.usernameTil.setErrorEnabled(true);
            binding.usernameTil.setError(getString(R.string.please_enter_username));
            return false;
        } else if (checkEmptyString(binding.passwordTiet.getText())) {
            binding.passwordTil.setErrorEnabled(true);
            binding.passwordTil.setError(getString(R.string.please_enter_password));
            return false;
        } else if (binding.passwordTiet.getText().length() < 8) {
            binding.passwordTil.setErrorEnabled(true);
            binding.passwordTil.setError(getString(R.string.enter_valid_password));
            return false;
        } else if (checkEmptyString(binding.confirmPasswordTiet.getText())) {
            binding.confirmPasswordTil.setErrorEnabled(true);
            binding.confirmPasswordTil.setError(getString(R.string.please_enter_confirm_password));
            return false;
        } else if (!binding.passwordTiet.getText().toString().matches(binding.confirmPasswordTiet.getText().toString().trim())) {
            binding.confirmPasswordTil.setErrorEnabled(true);
            binding.confirmPasswordTil.setError(getString(R.string.password_not_match));
            return false;
        }
        return true;
    }
}