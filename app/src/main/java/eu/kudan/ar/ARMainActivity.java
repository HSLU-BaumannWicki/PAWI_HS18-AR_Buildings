package eu.kudan.ar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import eu.kudan.ar.dto.Building;
import eu.kudan.ar.dto.ExampleBuildingImpl;
import eu.kudan.ar.location.LocationFound;
import eu.kudan.ar.location.LocationStorageListener;
import eu.kudan.ar.location.OnLocationChangeListener;
import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARGyroManager;
import eu.kudan.kudan.ARModelNode;

public class ARMainActivity extends ARActivity implements LocationFound, SensorEventListener {
    private LocationStorageListener myLocationListener;
    private LocationManager locationManager;
    private Building building;
    private ARGyroManager gyroManager;
    private SensorManager mSensorManager;
    private Sensor mMagneticSensor;
    private Sensor mAccelerometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private double degree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        building = new ExampleBuildingImpl();

        // Comment this out for the time being unless you plan to create UI elements
        setContentView(R.layout.ar_main_activity);

        ARAPIKey key = ARAPIKey.getInstance();
        // TODO: Using the development key, change in the future?
        key.setAPIKey("agWZcpYLYjBxCbWf2qZx6k+PWISqeGtFCqKaZwYtwS+kdn1HKiQAmsJ55STRBe9BqCw3VwG6qL+ESI5ntTF/iV/uekLG3PCokaUE0/uTzqhaYlxRdmuNBIduzBCjq3mV2na+gy3ffHH9Ipc7eIN0geTj3p+ppsmK0U399iGmN38ndIh6k2y16cByWIecMSU3yw3Ztw7gHRqf83hVhZ5T2ACGK4SNkQhhdKp+CTaR5W3amYCJBgwumqFqNFyI9UniuMk70T/cQObRQum2U51OjjbMfmEAwIBt8Q8jD2yACzye6K4/1O4pZhbGEbiDeLrAfxqMwBAe5o6vnYIilGNnpDhfi3wOHhRaqtLOVvB58GUIFTnAPvmYFVnLWRJmCUZ9FJNDyX3ALCl/alFEWh+A/a6NFjcwLGKI9drPuGG4ONFg4p0l+p3b9DZoLzszlmWAflI/UFzQa++kQn3/sclO9i0vPnpi0LWoABm5vGswLVAIX/0k6384GXxfkADI6fjGtf62XJ5ImaVDiiREa9mabWEQGoifghQG1sGNDYgBIYEpiaLsVzOfTALpe20Q7kFCMjedJImQhhuLtEK1BXfXJEed1QqUOsG9IeKxKk28GbOtOF9w3yrSF3gnJslzZxF2kEF3C6ckog8byagS+4p37FJmbpPsiKNH1Qm0LuouGcQ=");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new OnLocationChangeListener(this);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mMagneticSensor, SensorManager.SENSOR_DELAY_GAME);
        mAccelerometer  = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void setup()
    {
        super.setup();
        // Initialise ArbiTrack.
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        gyroManager = ARGyroManager.getInstance();
        gyroManager.initialise();

        showBuilding();
    }

    public void showBuilding(){
        ARModelNode myModel = building.getKudanModelNode();
        gyroManager.getWorld().addChild(myModel);
        gyroManager.start();
        //myModel.setPosition(new Vector3f(-10,-20,0));
        myModel.scaleByUniform(1f);

    }

    @Override
    public void onPause(){
        super.onPause();
        this.locationManager.removeUpdates(this.myLocationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume(){
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ((LocationListener) this.myLocationListener));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


    @Override
    public void locationUpdate(Location location) {
        ARModelNode myModel = building.getKudanModelNode();
        myModel.setPosition(0,-10, -1 * location.distanceTo(building.getBuildingLocation()));
        Log.i("Debug", "Distance To: " + location.distanceTo(building.getBuildingLocation()));
        // Rotation matrix based on current readings from accelerometer and magnetometer.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == mMagneticSensor){
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mSensorManager.unregisterListener(this, mMagneticSensor);
        }else if(event.sensor == mAccelerometer){
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mSensorManager.unregisterListener(this, mAccelerometer);

        }
        float[] mR = new float[9];
        float[] orientationAngles = new float[3];
        SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
        SensorManager.getOrientation(mR, orientationAngles);
        this.degree = Math.toDegrees(orientationAngles[0]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

