package com.m21438255.proyectosnapchat.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.m21438255.proyectosnapchat.R;

import java.io.IOException;

public class SplashToroActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();

    AssetManager assetManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_toro);
        assetManager = this.getAssets();
        reproducirSonido();
        final ImageView imgToro = (ImageView)findViewById(R.id.imageView2);
        AnimationSet animacion = new AnimationSet(false);
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        final Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotaciontoro);
        animacion.addAnimation(rotation);
        animacion.addAnimation(shake);

        imgToro.startAnimation(animacion);
        openApp(true);
    }

    public void reproducirSonido(){
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor =
                    assetManager.openFd("transition_music1.mp3");

            mediaPlayer.setDataSource(
                    descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(),
                    descriptor.getLength()
            );

            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashToroActivity.this
                        , MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
