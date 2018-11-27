package eu.kudan.ar;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import commonlib.location.NorthAngleCalculator;
import commonlib.storage.FloatMeanRingBuffer;
import commonlib.storage.MeanRingBuffer;

public class NorthInitializer extends AppCompatActivity implements SensorEventListener {
    private final static float MINIMAL_ANGLE_RAD = -0.01f;
    private final static float MAXIMAL_ANGLE_RAD = 0.01f;

    private final static int MAGNET_SENSOR_ID = 0b1;
    private final static int ACCELERATOR_SENSOR_ID = 0b10;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private SensorManager sensorManager;
    private int state;
    private MeanRingBuffer<Float> angleMeanBuffer = new FloatMeanRingBuffer(10);

    private Sensor magneticSensor;
    private Sensor accelerometer;
    private NorthAngleCalculator northAngleCalculator;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_north_initializer);
        this.imageView = findViewById(R.id.imageView);
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.northAngleCalculator = new NorthAngleCalculator();
        this.sensorManager = sensorManager;
        this.magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == magneticSensor){
            this.state |= MAGNET_SENSOR_ID;
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            this.northAngleCalculator.setMagnetometerData(event.values);
        }else if(event.sensor == accelerometer){
            this.state |= ACCELERATOR_SENSOR_ID;
            this.northAngleCalculator.setAcceleratorData(event.values);
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
        }
        if(this.state == (MAGNET_SENSOR_ID | ACCELERATOR_SENSOR_ID)){
            this.state &= ~MAGNET_SENSOR_ID;
            this.state &= ~ACCELERATOR_SENSOR_ID;
            float[] mR = new float[9];
            float[] orientationAngles = new float[3];
            SensorManager.getRotationMatrix(mR, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(mR, orientationAngles);
            System.out.println("Calculated:   " + this.northAngleCalculator.calculateNorthAngleBasedOnData());
            float mean = this.angleMeanBuffer.getNewMean(orientationAngles[0]*-1);
            this.imageView.setRotation((float)Math.toDegrees(mean));
            if(isInLegalNorthAngle(mean)){
                ((ImageView)findViewById(R.id.northpositionerindicator)).setImageResource(R.drawable.northpositionersuccessfull);
                this.startNextState();
            }else{
                ((ImageView)findViewById(R.id.northpositionerindicator)).setImageResource(R.drawable.northpositioner);
            }
        }
    }

    private boolean isInLegalNorthAngle(float mean) {
        return mean >= MINIMAL_ANGLE_RAD && mean <= MAXIMAL_ANGLE_RAD;
    }

    private void startNextState() {
        this.sensorManager.unregisterListener(this);
        if(isInLegalNorthAngle(this.angleMeanBuffer.getNewMean())){
            Intent startARActivityIntent = new Intent(this, ARMainActivity.class);
            startARActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //startARActivityIntent.putExtra("angleToNorth", this.angleMeanBuffer.getNewMean());
            this.startActivity(startARActivityIntent);
            ActivityCompat.finishAffinity(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
