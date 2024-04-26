package com.itube.app;

import static com.itube.app.Extensions.VIDEO_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class PlayListAdapter  extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> arrayList;

    public PlayListAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.youtubeUrlTextview.setText(arrayList.get(position));
        holder.youtubeUrlTextview.setOnClickListener(v -> context.startActivity(new Intent(context, PlayerActivity.class)
                .putExtra(VIDEO_URL, Objects.requireNonNull(arrayList.get(position)))));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView youtubeUrlTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            youtubeUrlTextview = itemView.findViewById(R.id.youtube_url_textview);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}