package com.example.virtrealtud;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Arrays;

public class GravityListener implements SensorEventListener {

    //indicator of last gravity direction
    private boolean buffer;

    public GravityListener() {
        this.buffer = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println("G"+ Arrays.toString(event.values));
        if (event.values[0]>0){
            buffer = false;
        } else {
            buffer = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public boolean isRotatedToHit(){
        return buffer;
    }
}
