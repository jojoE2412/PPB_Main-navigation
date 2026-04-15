package com.example.navigation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;
    private boolean isBookmarkPage;

    public NewsAdapter(List<News> newsList, boolean isBookmarkPage) {
        this.newsList = new ArrayList<>(newsList);
        this.isBookmarkPage = isBookmarkPage;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        int currentPos = holder.getBindingAdapterPosition();
        if (currentPos == RecyclerView.NO_POSITION) return;
        
        News news = newsList.get(currentPos);
        Context context = holder.itemView.getContext();
        
        holder.title.setText(news.getTitle());
        holder.desc.setText(news.getDescription());
        
        Glide.with(context)
                .load(news.getUrlToImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.image);

        if (isBookmarkPage) {
            holder.btnBookmark.setImageResource(android.R.drawable.ic_menu_revert);
            holder.btnBookmark.setAlpha(1.0f);
        } else {
            holder.btnBookmark.setImageResource(R.drawable.ic_favorites);
            updateBookmarkIcon(holder.btnBookmark, news, context);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("url", news.getUrl());
            v.getContext().startActivity(intent);
        });

        holder.btnBookmark.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            News clickedNews = newsList.get(pos);

            if (BookmarkManager.isBookmarked(context, clickedNews)) {
                BookmarkManager.removeBookmark(context, clickedNews);
                
                if (isBookmarkPage) {
                    newsList.remove(pos);
                    notifyItemRemoved(pos);
                    int remainingItems = newsList.size() - pos;
                    if (remainingItems > 0) {
                        notifyItemRangeChanged(pos, remainingItems);
                    }
                } else {
                    updateBookmarkIcon(holder.btnBookmark, clickedNews, context);
                }

                Snackbar.make(v, "Berita dihapus dari Bookmark", Snackbar.LENGTH_LONG)
                        .setAction("Undo", view -> {
                            BookmarkManager.addBookmark(context, clickedNews);
                            if (isBookmarkPage) {
                                if (pos <= newsList.size()) {
                                    newsList.add(pos, clickedNews);
                                    notifyItemInserted(pos);
                                    int itemsAfter = newsList.size() - pos;
                                    if (itemsAfter > 0) {
                                        notifyItemRangeChanged(pos, itemsAfter);
                                    }
                                }
                            } else {
                                updateBookmarkIcon(holder.btnBookmark, clickedNews, context);
                            }
                        }).show();
            } else {
                BookmarkManager.addBookmark(context, clickedNews);
                updateBookmarkIcon(holder.btnBookmark, clickedNews, context);
                Toast.makeText(context, "Berita disimpan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBookmarkIcon(ImageView icon, News news, Context context) {
        if (!isBookmarkPage) {
            if (BookmarkManager.isBookmarked(context, news)) {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.yellow_dark));
                icon.setAlpha(1.0f);
            } else {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.gray_text));
                icon.setAlpha(0.5f);
            }
        }
    }

    @Override
    public int getItemCount() {
        return newsList != null ? newsList.size() : 0;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image, btnBookmark;
        TextView title, desc;

        public NewsViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.news_image);
            title = v.findViewById(R.id.news_title);
            desc = v.findViewById(R.id.news_desc);
            btnBookmark = v.findViewById(R.id.btn_bookmark);
        }
    }
}
