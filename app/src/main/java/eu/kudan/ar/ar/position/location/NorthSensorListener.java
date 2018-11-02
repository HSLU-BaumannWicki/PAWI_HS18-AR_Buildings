package eu.kudan.ar.ar.position.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import commonlib.storage.RingBufferImpl;

public class NorthSensorListener implements SensorEventListener {
    private final static int MAGNET_SENSOR_ID = 0b1;
    private final static int ACCELERATOR_SENSOR_ID = 0b10;

    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];

    private final SensorManager sensorManager;
    private final Sensor magneticSensor;
    private final Sensor accelerometer;
    private final RingBufferImpl<Float> radiantRingBuffer;
    private int state;
    private int numberOfDatas = 0;
    private final int maxNumberOfDatas;

    public NorthSensorListener(SensorManager sensorManager,
                               RingBufferImpl<Float> ringBufferForRad,
                               final int maxNumberOfDatas){
        this.maxNumberOfDatas = maxNumberOfDatas;
        this.radiantRingBuffer = ringBufferForRad;
        this.sensorManager = sensorManager;
        this.magneticSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorManager.registerListener(this, this.magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void startTrackingNorth(){
        if(this.numberOfDatas < this.maxNumberOfDatas) {
            this.sensorManager.registerListener(this, this.magneticSensor, SensorManager.SENSOR_DELAY_GAME);
            this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void stopTrackingNorth(){
        this.sensorManager.unregisterListener(this, this.magneticSensor);
        this.sensorManager.unregisterListener(this, this.accelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == magneticSensor){
            this.state |= MAGNET_SENSOR_ID;
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
        }else if(event.sensor == accelerometer){
            this.state |= ACCELERATOR_SENSOR_ID;
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
        }
        if(this.state == (MAGNET_SENSOR_ID | ACCELERATOR_SENSOR_ID)){
            if(this.numberOfDatas >= this.maxNumberOfDatas){
                this.stopTrackingNorth();
            }
            this.numberOfDatas += 1;
            this.state &= ~MAGNET_SENSOR_ID;
            this.state &= ~ACCELERATOR_SENSOR_ID;
            float[] mR = new float[9];
            float[] orientationAngles = new float[3];
            SensorManager.getRotationMatrix(mR, null, lastAccelerometer, lastMagnetometer);
            SensorManager.getOrientation(mR, orientationAngles);
            this.radiantRingBuffer.addObjectForMeanCalculation(orientationAngles[0]*-1);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getRadiant(){
        return this.radiantRingBuffer.getNewMean();
    }
}
