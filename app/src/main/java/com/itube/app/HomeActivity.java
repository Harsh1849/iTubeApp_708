package com.itube.app;

import static com.itube.app.Extensions.USERDATA;
import static com.itube.app.Extensions.USERNAME;
import static com.itube.app.Extensions.VIDEO_URL;
import static com.itube.app.Extensions.adjustFullScreen;
import static com.itube.app.Extensions.checkEmptyString;
import static com.itube.app.Extensions.hideKeyboard;
import static com.itube.app.Extensions.isYouTubeUrl;
import static com.itube.app.Extensions.showToast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.itube.app.databinding.ActivityHomeBinding;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        manageClicks();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(USERDATA, MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME, "");

        getOnBackPressedDispatcher().addCallback(callback);
    }

    private void manageClicks() {
        binding.playCardView.setOnClickListener(v -> {
            if (isValid()) {
                binding.urlTil.setErrorEnabled(false);
                binding.urlTil.setError("");
                hideKeyboard(this, binding.getRoot());
                startActivity(new Intent(this, PlayerActivity.class)
                        .putExtra(VIDEO_URL, Objects.requireNonNull(binding.urlTiet.getText()).toString()));
            }
        });
        binding.addToPlayListCardView.setOnClickListener(v -> {
            if (isValid()) {
                binding.urlTil.setErrorEnabled(false);
                binding.urlTil.setError("");
                hideKeyboard(this, binding.getRoot());
                boolean checkPlayList = databaseHelper.CheckPlayList(Objects.requireNonNull(username), Objects.requireNonNull(binding.urlTiet.getText()).toString());
                if (!checkPlayList) {
                    boolean insertData = databaseHelper.InsertPlayListData(username, binding.urlTiet.getText().toString());

                    if (insertData) {
                        showToast(this, "Added successfully in your playlist");
                    }
                } else {
                    showToast(this, "This video already added in your playlist!");
                }
            }
        });
        binding.myPlaylistCardView.setOnClickListener(v -> startActivity(new Intent(this, MyPlaylistActivity.class)));
    }

    private boolean isValid() {
        if (checkEmptyString(binding.urlTiet.getText())) {
            binding.urlTil.setErrorEnabled(true);
            binding.urlTil.setError(getString(R.string.enter_youtube_url));
            return false;
        } else if (!isYouTubeUrl(binding.urlTiet.getText().toString())) {
            binding.urlTil.setErrorEnabled(true);
            binding.urlTil.setError(getString(R.string.enter_valid_youtube_url));
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