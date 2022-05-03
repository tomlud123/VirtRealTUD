package com.example.virtrealtud;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Arrays;

public class GravityListener implements SensorEventListener {
    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("G"+ Arrays.toString(event.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean isRotatedToHit(){
        return true;
    }
}
