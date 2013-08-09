package com.example.sensor;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

/**
 * MainActivity
 * @author naotaro
 */
public class MainActivity extends Activity{
    private TextView values;
    private SensorManager mSensorManager;
    private float mAccel;          // acceleration apart from gravity
    private float mAccelCurrent;   // current acceleration including gravity
    private float mAccelLast;      // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
          float x = se.values[0];
          float y = se.values[1];
          float z = se.values[2];
          //values[0] X軸方向の加速度（左右方向　右方向が正）
          //values[1] Y軸方向の加速度（上下方向 上方向が正）
          //values[2] Z軸方向の加速度（前後方向 前面方向が正）
          mAccelLast = mAccelCurrent;
          mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
          float delta = mAccelCurrent - mAccelLast;
          mAccel = mAccel * 0.9f + delta; // perform low-cut filter
          // シェイクされた時の判定
          if(mAccel  > 2){
              values.setText("シェイク中\n" + mAccel);
              values.setBackgroundColor(Color.RED);
          }else{
              values.setText("シェイクなし\n" + mAccel);
              values.setBackgroundColor(Color.WHITE);
          }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        values = (TextView)findViewById(R.id.text);
        // 加速度センサー
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        // 初期値
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
      // Listenerの登録
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
}
