package com.example.navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private PackgerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Portal.id");
        }
        
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        adapter = new PackgerAdapter(this, 2);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Beranda");
            } else {
                tab.setText("Bookmark");
            }
        }).attach();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.option_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Cari topik berita...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void performSearch(String query) {
        viewPager.setCurrentItem(0);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + 0);
        if (fragment instanceof MainFragment) {
            ((MainFragment) fragment).fetchNews(null, query);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.option_settings) {
            showSettingsDialog();
            return true;
        } else if (itemId == R.id.option_about) {
            showAboutDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsDialog() {
        String[] options = {"Indonesia", "Internasional (Global)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Wilayah Berita");
        builder.setItems(options, (dialog, which) -> {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + 0);
            if (fragment instanceof MainFragment) {
                if (which == 0) {
                    // PERBAIKAN: Gunakan query "indonesia" agar data selalu ada
                    ((MainFragment) fragment).fetchNews(null, "indonesia");
                    Toast.makeText(this, "Berita Indonesia dimuat", Toast.LENGTH_SHORT).show();
                } else {
                    ((MainFragment) fragment).fetchNews("us", null);
                    Toast.makeText(this, "Berita Internasional dimuat", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Tentang Portal.id")
                .setMessage("Portal.id adalah aplikasi agregator berita pintar yang menyajikan informasi terkini dari sumber terpercaya di Indonesia.\n\nVersi: 1.0.0\nDeveloper: Joshua Project")
                .setPositiveButton("Tutup", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }
}
