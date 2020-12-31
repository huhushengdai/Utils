package com.huhushengdai.mapdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Handler handler = new Handler(Looper.getMainLooper());

    private String mMockProviderName = LocationManager.GPS_PROVIDER;
//    private String mMockNetName = "fused";
//    private String mMockNet2Name = LocationManager.NETWORK_PROVIDER;
    private LocationManager locationManager;

    private EditText mLatitude;
    private EditText mLongitude;

    private View initDevice;
    private CheckBox correctBox;
    private CheckBox saveBox;

    private static final double correctLongitude = 0.011409;
    private static final double correctLatitude = 0.002537;

    /**
     * inilocation 初始化 位置模拟
     */
    private void init_location() throws Exception {



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        initLocationManager(mMockProviderName);
//        initLocationManager(mMockNetName);
//        initLocationManager(mMockNet2Name);
        initDevice.setVisibility(View.GONE);
        Toast.makeText(this, "初始化成功，可以点击确定更改定位", Toast.LENGTH_SHORT).show();
    }

    private void initLocationManager(String providerName) throws Exception{
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有获取定位权限", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.addTestProvider(providerName, false, true, false, false, true, true, true, 0, 5);
        locationManager.setTestProviderEnabled(providerName, true);
        locationManager.requestLocationUpdates(providerName, 0, 0, this);
    }

    @SuppressLint("NewApi")
    private void setLocation(double longitude, double latitude) {
        setLocation(mMockProviderName,longitude,latitude);
//        setLocation(mMockNetName,longitude,latitude);
//        setLocation(mMockNet2Name,longitude,latitude);
    }

    private void setLocation(String providerName,double longitude, double latitude){
        Location location = new Location(providerName);
        location.setTime(System.currentTimeMillis());
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAltitude(2.0f);
        location.setAccuracy(3.0f);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        locationManager.setTestProviderLocation(providerName, location);
    }

    private static final String sp_name = "map";
    private static final String sp_latitude = "latitude";
    private static final String sp_longitude = "longitude";

    private static final String[] p = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitude = findViewById(R.id.latitude);
        mLongitude = findViewById(R.id.longitude);
        initDevice = findViewById(R.id.init);
        correctBox = findViewById(R.id.correct);
        saveBox = findViewById(R.id.save);

        SharedPreferences sp = getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        String latitude = sp.getString(sp_latitude, "");
        if (!TextUtils.isEmpty(latitude)) {
            Log.i(TAG, "set latitude = " + latitude);
            mLatitude.setText(latitude);
        } else {
            Log.i(TAG, "set latitude = null");
        }
        String longitude = sp.getString(sp_longitude, "");
        if (!TextUtils.isEmpty(longitude)) {
            Log.i(TAG, "set longitude = " + longitude);
            mLongitude.setText(longitude);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(p, 102);
        } else {
            try {
                init_location();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "没有开启模拟定位", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                try {
                    init_location();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "没有开启模拟定位", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    double longitude;
    double latitude;
    public void confirm(View view) {
        handler.removeCallbacksAndMessages(null);
        longitude = Double.valueOf(mLongitude.getText().toString());
        latitude = Double.valueOf(mLatitude.getText().toString());
        if (correctBox.isChecked()) {
            longitude = longitude - correctLongitude;
            latitude = latitude - correctLatitude;
        }
        handler.post(new TimerTask());
        if (!saveBox.isChecked()){
            return;
        }
        SharedPreferences sp = getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(sp_longitude, mLongitude.getText().toString());
        editor.putString(sp_latitude, mLatitude.getText().toString());
        boolean result = editor.commit();
        Log.i(TAG, "result = " + result);
    }

    private class TimerTask implements Runnable {

        @Override
        public void run() {

            setLocation(longitude, latitude);
            if (isFinishing()) {
                return;
            }
            handler.postDelayed(new TimerTask(), 1000);
        }
    }

    public void initDevice(View v) {
        try {
            init_location();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有开启模拟定位", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged getLongitude = " + location.getLongitude() + ",getLatitude = " + location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "onStatusChanged provider" + provider + ",status = " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "onProviderEnabled provider = " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled provider = " + provider);
    }
}
