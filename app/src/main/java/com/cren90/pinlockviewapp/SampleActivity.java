package com.cren90.pinlockviewapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cren90.pinlockview.Indicator;
import com.cren90.pinlockview.PinLockListener;
import com.cren90.pinlockview.PinLockView;

import org.jetbrains.annotations.NotNull;

public class SampleActivity extends AppCompatActivity {

    public static final String TAG = "PinLockView";

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onPinSubmit(@NotNull String pin) {
            Log.d(TAG, "Pin submit: " + pin);
        }

        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sample);

        PinLockView pinLockView = findViewById(R.id.pin_lock_view);
        Indicator indicator = findViewById(R.id.indicator);

        pinLockView.attachIndicator(indicator);
        pinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();
    }
}
