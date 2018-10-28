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
import android.view.View;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import java.util.List;

import eu.kudan.ar.dto.Building;
import eu.kudan.ar.dto.ExampleBuildingImpl;
import eu.kudan.ar.location.LocationFound;
import eu.kudan.ar.location.LocationStorageListener;
import eu.kudan.ar.location.MyLocation;
import eu.kudan.ar.location.OnLocationChangeListener;
import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARCamera;
import eu.kudan.kudan.ARGyroManager;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARRenderer;
import eu.kudan.kudan.ARWorld;

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
    private float rotatedAngle=0;
    private float deltaAngle = 0;

    private int state = 0;

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
        Vector3f myVector = MyLocation.getDistancesBetween(location, building.getBuildingLocation());
        Vector2f myAngleVector = new Vector2f(myVector.getX(), myVector.getZ());
        Vector2f referenceVector = new Vector2f(1, 0);

        float angle = referenceVector.angleBetween(myAngleVector);
        float deltaAngle = angle - this.rotatedAngle;

        if (-0.2 < deltaAngle && deltaAngle < 0.2 ){
            this.deltaAngle += deltaAngle;
        }else{
            building.getKudanModelNode().rotateByRadians(this.deltaAngle, 0,1,0);
            this.deltaAngle = 0;
        }
        this.rotatedAngle = angle;

        Log.i("DistanceVector", MyLocation.getDistancesBetween(location, building.getBuildingLocation()).toString());
        myModel.setPosition(MyLocation.getDistancesBetween(location, building.getBuildingLocation()));

        //List<ARNode> myNodes = this.gyroManager.getWorld().getChildren();
        //this.gyroManager.getWorld().rotateByDegrees((float) this.degree, 0,1,0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == mMagneticSensor){
            this.state |= 0b10;
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mSensorManager.unregisterListener(this, mMagneticSensor);
        }else if(event.sensor == mAccelerometer){
            this.state |= 0b1;
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mSensorManager.unregisterListener(this, mAccelerometer);
        }
        float[] mR = new float[9];
        float[] orientationAngles = new float[3];
        SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
        SensorManager.getOrientation(mR, orientationAngles);
        this.degree = Math.toDegrees(orientationAngles[0]);
        if(this.state == 3){
            Log.e("OMG", ARRenderer.getInstance().getWorldCameraPosition().toString());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onButtonClicked(View element){
        Log.i("String", "String2");
    }

    private void rotateOverZAxis(Vector3f vectorToRotate, float radian){
    }
}

