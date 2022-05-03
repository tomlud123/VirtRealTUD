package com.example.virtrealtud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorAcc;
    private Sensor sensorGrav;
    private SensorEventListener gravListener;
    private MediaPlayer mp;
    private boolean antiLoopMutex = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGrav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gravListener = new GravityListener();
        mp = MediaPlayer.create(this, R.raw.electr);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gravListener, sensorGrav, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(gravListener);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println("A"+Arrays.toString(sensorEvent.values));
        if ((sensorEvent.values[0]) > 12.0) {
            if (!mp.isPlaying() && !antiLoopMutex) {
                antiLoopMutex = true;
                mp.start();
                System.out.println("FIRE");
                System.out.println("FIRE");
                System.out.println("FIRE");
                mp.setOnCompletionListener(mp -> {
                    mp.pause();
                    mp.seekTo(0);
                    System.out.println("STOP");
                });
            }
        } else {
            antiLoopMutex = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}