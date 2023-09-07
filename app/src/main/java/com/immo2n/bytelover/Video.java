package com.immo2n.bytelover;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.immo2n.bytelover.databinding.ActivityVideoBinding;

import java.util.Objects;

public class Video extends AppCompatActivity {
    private Global global;
    private static final int UI_ANIMATION_DELAY = 200;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = this::hide;
    private ExoPlayer player;
    private RelativeLayout nav;
    private StyledPlayerView playerView;
    private ViewGroup.MarginLayoutParams playerParams;
    private TextView error_reason;
    private String videoUrl;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.immo2n.bytelover.databinding.ActivityVideoBinding binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            String receivedLink = intent.getStringExtra("link");
            if (receivedLink != null) {
                videoUrl = receivedLink;
            }
            else {
                this.finish();
                Toast.makeText(this, "No video to play!", Toast.LENGTH_SHORT).show();
            }
        }

        //THE INFO MENU
        findViewById(R.id.info).setOnClickListener(v-> {
            Dialog info_dialogue = global.makeDialogue(R.layout.dialogue_video_info, R.drawable.dialogue_background);
            info_dialogue.setCancelable(true);
            info_dialogue.getWindow().findViewById(R.id.exit).setOnClickListener(v2 -> {

            });
        });

        Uri uri = Uri.parse(videoUrl);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(videoUrl);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(uri)
                .setMimeType(mimeType)
                .build();

        global = new Global(this);

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black_robust));

        nav = findViewById(R.id.nav_bar);

        mVisible = true;
        mContentView = binding.fullscreenContent;
        mContentView.setOnClickListener(view -> toggle());

        Objects.requireNonNull(getSupportActionBar()).hide();

        findViewById(R.id.back).setOnClickListener(v -> {
            player.stop();
            finish();
        });

        //Player
        playerView = findViewById(R.id.fullscreen_content);
        playerParams = (ViewGroup.MarginLayoutParams) playerView.getLayoutParams();

        ExoPlayer.Builder playerBuilder = new ExoPlayer.Builder(this);
        player = playerBuilder.build();

        player.setPlayWhenReady(true);
        playerView.setPlayer(player);
        playerView.setBackgroundColor(ContextCompat.getColor(this, R.color.black_robust));

        player.setMediaItem(mediaItem);
        player.prepare();

        Dialog failure_dialogue = global.makeDialogue(R.layout.dialogue_class_load_failed, R.drawable.dialogue_background);
        failure_dialogue.setCancelable(false);
        error_reason = failure_dialogue.getWindow().findViewById(R.id.error_reason);
        failure_dialogue.getWindow().findViewById(R.id.exit).setOnClickListener(v -> {
            player.stop();
            finish();
        });

        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
                Log.d("MOON-LINK", videoUrl);
                error_reason.setText(error.getMessage());
                failure_dialogue.show();
                show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        if(player.isPlaying()){
            return;
        }
        mVisible = false;
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
        playerParams.topMargin = global.dpToPixels(0);
        playerView.setLayoutParams(playerParams);
        nav.setVisibility(View.GONE);
    }

    private void show() {
        if(player.isPlaying()){
            return;
        }
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        nav.setVisibility(View.VISIBLE);
        playerParams.topMargin = global.dpToPixels(60);
        playerView.setLayoutParams(playerParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}