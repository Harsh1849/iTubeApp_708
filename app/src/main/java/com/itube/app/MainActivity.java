package com.itube.app;

import static com.itube.app.Extensions.USERDATA;
import static com.itube.app.Extensions.USERNAME;
import static com.itube.app.Extensions.adjustFullScreen;
import static com.itube.app.Extensions.checkEmptyString;
import static com.itube.app.Extensions.hideKeyboard;
import static com.itube.app.Extensions.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.itube.app.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        manageClicks();
        databaseHelper = new DatabaseHelper(this);

        getOnBackPressedDispatcher().addCallback(callback);
    }

    private void manageClicks() {
        binding.signInCardView.setOnClickListener(v -> {
            if (isValid()) {
                hideKeyboard(this, binding.getRoot());
                boolean checkData = databaseHelper.CheckLogIn(Objects.requireNonNull(binding.usernameTiet.getText()).toString(), Objects.requireNonNull(binding.passwordTiet.getText()).toString());
                if (checkData) {
                    showToast(this, "Login Successfully");
                    sharedPreferences = getSharedPreferences(USERDATA, MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(USERNAME, binding.usernameTiet.getText().toString());
                    editor.commit();
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    showToast(this, "Invalid Username or Password!");
                }
            }
        });

        binding.signUpCardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }

    private boolean isValid() {
        if (checkEmptyString(binding.usernameTiet.getText())) {
            binding.usernameTil.setErrorEnabled(true);
            binding.usernameTil.setError(getString(R.string.please_enter_username));
            return false;
        } else if (checkEmptyString(binding.passwordTiet.getText())) {
            binding.passwordTil.setErrorEnabled(true);
            binding.passwordTil.setError(getString(R.string.please_enter_password));
            return false;
        }
        return true;
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            // Back is pressed... Finishing the activity
            finishAffinity();
        }
    };
}