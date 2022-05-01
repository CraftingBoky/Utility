package com.example.utility;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static MainActivity main;
    VideoView intro;

    public MainActivity() {
        main = this;
    }

    private boolean isNotiPermissionAllowed() {
        Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();

        return notiListenerSet.contains(myPackageName);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Calculator = findViewById(R.id.Calculator);
        Button Memo = findViewById(R.id.Memo);
        Button Menu = findViewById(R.id.Menu);

        VideoView intro = findViewById(R.id.intro);


        Uri introUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        intro.setMediaController(new MediaController(this));
        intro.setVideoURI(introUri);


        intro.setOnPreparedListener(mediaPlayer -> {
            //비디오 시작
            intro.start();
        });

        boolean isPermissionAllowed = isNotiPermissionAllowed();

        if (!isPermissionAllowed) {

            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }

        Calculator.setOnClickListener(view ->
                startActivity(new Intent(this, MathActivity.class)));

        Memo.setOnClickListener(view ->
                startActivity(new Intent(this, MemoActivity.class)));

        Menu.setOnClickListener(view ->
                startActivity(new Intent(this, MenuActivity.class)));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //비디오 일시 정지
        if (intro != null && intro.isPlaying()) intro.pause();
    }

    //액티비티가 메모리에서 사라질때..
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        if (intro != null) intro.stopPlayback();
    }
}