package com.example.geekbrainsandroidweather;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.geekbrainsandroidweather.fragments.AddCityFragment;
import com.example.geekbrainsandroidweather.fragments.CitiesFragment;
import com.example.geekbrainsandroidweather.fragments.DevelopersInfoFragment;
import com.example.geekbrainsandroidweather.fragments.SettingsFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private VideoView videoViewBg;
    private MediaPlayer mediaPlayer;
    int currentVideoPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupActionBar();
        setCityFragment();
        setOnClickForSideMenuItems();
//        setVideoBackground();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        appBarLayout.setOutlineProvider(null);
        ViewCompat.setElevation(toolbar, 0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

//    private void setVideoBackground() {
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.clear_sky22);
//        videoViewBg.setVideoURI(uri);
//        videoViewBg.start();
//        videoViewBg.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setLooping(true);
//                MainActivity.this.mediaPlayer = mediaPlayer;
//                if(currentVideoPosition != 0) {
//                    mediaPlayer.seekTo(currentVideoPosition);
//                    mediaPlayer.start();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        currentVideoPosition = mediaPlayer.getCurrentPosition();
//        videoViewBg.pause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        videoViewBg.start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.release();
//        mediaPlayer = null;
//    }

    private void initViews() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        videoViewBg = findViewById(R.id.videoView);
    }

    private void setOnClickForSideMenuItems() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_3) {
                    replaceFragment(new DevelopersInfoFragment(), R.id.citiesContainer);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_2) {
                    replaceFragment(new SettingsFragment(), R.id.citiesContainer);
                    drawer.close();
                }
                if (item.getItemId() == R.id.page_1) {
                    replaceFragment(new CitiesFragment(), R.id.citiesContainer);
                    drawer.close();
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment, int containerId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_navigation_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addCity) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.citiesContainer, new AddCityFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setCityFragment() {
        CitiesFragment citiesFragment = new CitiesFragment();
        citiesFragment.setArguments(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.citiesContainer, citiesFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen()) {
            drawer.close();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                navigationView.setCheckedItem(R.id.page_1);
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }

}
