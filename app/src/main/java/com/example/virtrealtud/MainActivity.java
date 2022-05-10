package com.example.virtrealtud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Arrays;
import java.util.Queue;

//TODO: add textviews with informations about settings
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensorAcc;
    private MediaPlayer mp;
    private boolean antiLoopMutex = false;
    private Queue<Float> accBuffer;
    private float sensitivity;
    private boolean leftHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mp = MediaPlayer.create(this, R.raw.electr);
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences("settings",0);
        float scanBufferSize = sharedPref.getFloat("buffer size", SettingsActivity.DEFAULT_BUFFER_SIZE);
        accBuffer = new CircularFifoQueue<>((int) scanBufferSize);
        sensitivity = sharedPref.getFloat("sensitivity", SettingsActivity.DEFAULT_SENSITIVITY);
        leftHand = sharedPref.getBoolean("left hand", false);

        sensorManager.registerListener(this, sensorAcc, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        System.out.println(Arrays.toString(sensorEvent.values));
        accBuffer.add(sensorEvent.values[0]);
        float scanResult = 0;
        for (int i = 0; i < accBuffer.size(); i++) {
            scanResult = scanResult + accBuffer.element();
        }
        scanResult = scanResult / accBuffer.size();
        System.out.println(scanResult);

        if (scanResult > sensitivity && !leftHand
        || scanResult < -sensitivity && leftHand) {
            if (!mp.isPlaying() && !antiLoopMutex) {
                antiLoopMutex = true;
                mp.start();
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

    public void onSettingsClick(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}