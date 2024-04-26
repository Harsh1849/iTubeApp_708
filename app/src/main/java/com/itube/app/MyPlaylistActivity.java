package com.itube.app;

import static com.itube.app.Extensions.USERDATA;
import static com.itube.app.Extensions.USERNAME;
import static com.itube.app.Extensions.adjustFullScreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.itube.app.databinding.ActivityMyPlaylistBinding;

import java.util.ArrayList;

public class MyPlaylistActivity extends AppCompatActivity {

    private ActivityMyPlaylistBinding binding;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    String username;
    ArrayList<String> playListArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMyPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adjustFullScreen(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(USERDATA, MODE_PRIVATE);
        username = sharedPreferences.getString(USERNAME, "");

        manageData();
    }

    private void manageData() {
        playListArray = databaseHelper.getAllPlayListData(username);
        if (playListArray.isEmpty()) {
            binding.noDataFound.setVisibility(View.VISIBLE);
            binding.playListRecyclerView.setVisibility(View.GONE);
        } else {
            binding.noDataFound.setVisibility(View.GONE);
            binding.playListRecyclerView.setVisibility(View.VISIBLE);
            binding.playListRecyclerView.setAdapter(new PlayListAdapter(this, playListArray));
        }

    }
}