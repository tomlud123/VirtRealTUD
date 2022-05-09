package com.example.virtrealtud;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Arrays;
import java.util.Queue;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorAcc;
    private Sensor sensorGrav;
    private GravityListener gravListener;
    private MediaPlayer mp;
    private boolean antiLoopMutex = false;
    Queue<Float> accBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGrav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gravListener = new GravityListener();
        accBuffer = new CircularFifoQueue<>(5);
        mp = MediaPlayer.create(this, R.raw.electr);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_FASTEST);
//        sensorManager.registerListener(gravListener, sensorGrav, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(gravListener);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println(Arrays.toString(sensorEvent.values));
        accBuffer.add(sensorEvent.values[0]);
        float tmp = 0;
        for (int i = 0; i < 5; i++) {
            tmp = tmp + accBuffer.element();
        }
        System.out.println(tmp);
        if (tmp > 125) {
            if (!mp.isPlaying() && !antiLoopMutex /*&& gravListener.isRotatedToHit()*/) {
                antiLoopMutex = true;
                mp.start();
                System.out.println("FIRE" + gravListener.isRotatedToHit());
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

    public void onSettingsClick(View v){
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

}