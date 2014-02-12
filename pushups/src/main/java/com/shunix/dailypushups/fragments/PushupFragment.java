package com.shunix.dailypushups.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunix.dailypushups.BuildConfig;
import com.shunix.dailypushups.R;
import com.shunix.dailypushups.ui.HoloCircularProgressBar;
import com.shunix.dailypushups.utils.SharedPreferenceHelper;

/**
 * @author Ray WANG <admin@shunix.com>
 * @version 1.0.0
 * @since Feb 9th, 2014
 */
public class PushupFragment extends Fragment implements SensorEventListener {

    /**
     * Show the countdown.
     */
    private HoloCircularProgressBar progressBar;
    private ObjectAnimator animator;
    /**
     * Sensor manager to get the P-sensor.
     */
    private SensorManager sensorManager;
    /**
     * Prevent the screen from lock.
     */
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    /**
     * Count of the pushups.
     */
    private int count;
    /**
     * Status indicator for sensors.
     * 0 -- init
     * 1 -- close
     * 2 -- far
     */
    private int indicator = 0;
    /**
     * Helper to save the count.
     */
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pushup_layout, container, false);
        progressBar = (HoloCircularProgressBar) view.findViewById(R.id.holoCircularProgressBar);
        progressBar.setNumber(0);
        showCountDown(5);
        return view;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Do nothing here.
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize sensor manager etc.
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "PushupFragment");
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(getActivity());
        // Set the count to 0 on every startup.
        count = 0;
    }

    /**
     * Start the progress bar for counter down.
     */
    public void showCountDown(int seconds) {
        /**
         * Store the count on previous circle.
         */
        final int preCount = count;
        animator = ObjectAnimator.ofFloat(progressBar, "progress", 1f);
        if (animator != null) {
            animator.cancel();
        }
        animator.setDuration(seconds * 1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                progressBar.setProgress(0f);
                if (count == preCount) {
                    animator.cancel();
                    // TODO Mission failed.
                    if (BuildConfig.DEBUG) {
                        Log.d("Failed", "You failed, score is " + String.valueOf(count));
                        count = 0;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
//        animator.reverse();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressBar.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });
        progressBar.setMarkerProgress(1f);
        animator.start();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Get data from P-sensor.
        float[] values = sensorEvent.values;
        if (BuildConfig.DEBUG) {
            for (float i : values) {
                Log.d("Sensor Raw Data", String.valueOf(i));
            }
        }
        // Check the sensor type.
        if ((values != null) && (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)) {
            /**
             * values[0] indicates the distance.
             */
            if (values[0] == 0.0) {
                indicator = 0;
                indicator++;
            } else {
                indicator++;
            }
            if (indicator == 2) {
                count++;
                /**
                 * Restart the countdown.
                 */
                progressBar.setNumber(count);
                animator.cancel();
                progressBar.setProgress(0f);
                showCountDown(5);
                if (BuildConfig.DEBUG) {
                    Log.d("Count", String.valueOf(count));
                }
            }
            if (BuildConfig.DEBUG) {
                if (values[0] == 0.0) {
                    Log.d("SensorEvent", "Close");
                } else {
                    Log.d("SensorEvent", "Far");
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the count.
        sharedPreferenceHelper.saveData("count", String.valueOf(count));
        // Release wake lock when invisible to user.
        wakeLock.release();
        // Unregister the sensor listener.
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if count is empty.
        String savedInstanceState = sharedPreferenceHelper.getValue("count");
        if (!savedInstanceState.equals("")) {
            // Read the count.
            count = Integer.parseInt(savedInstanceState);
            // Remove the previous saved count.
            sharedPreferenceHelper.removeData("count");
        }
        // Acquire wake lock when resumed.
        wakeLock.acquire();
        // Register the sensor listener
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_FASTEST);
    }
}
