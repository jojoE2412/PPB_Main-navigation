package com.example.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {
    private static final String PREF_NAME = "news_bookmarks";
    private static final String KEY_BOOKMARKS = "bookmarks_list";

    public static List<News> getBookmarks(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KEY_BOOKMARKS, null);
        
        if (json == null) {
            return new ArrayList<>();
        }
        
        Gson gson = new Gson();
        Type type = new TypeToken<List<News>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private static void saveBookmarks(Context context, List<News> list) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_BOOKMARKS, json);
        editor.apply();
    }

    public static boolean isBookmarked(Context context, News news) {
        List<News> list = getBookmarks(context);
        for (News n : list) {
            if (n.getUrl() != null && news.getUrl() != null && n.getUrl().equals(news.getUrl())) {
                return true;
            }
        }
        return false;
    }

    public static void addBookmark(Context context, News news) {
        List<News> list = getBookmarks(context);
        if (!isBookmarked(context, news)) {
            list.add(news);
            saveBookmarks(context, list);
        }
    }

    public static void removeBookmark(Context context, News news) {
        List<News> list = getBookmarks(context);
        News found = null;
        for (News n : list) {
            if (n.getUrl() != null && news.getUrl() != null && n.getUrl().equals(news.getUrl())) {
                found = n;
                break;
            }
        }
        if (found != null) {
            list.remove(found);
            saveBookmarks(context, list);
        }
    }
}
