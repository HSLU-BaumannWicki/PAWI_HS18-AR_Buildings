package commonlib.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import commonlib.storage.MeanRingBufferAbstract;

public class NorthSensorListener implements SensorEventListener{
    private final NorthAngleCalculator northAngleCalculator;
    private final SensorManager sensorManager;
    private final Sensor magneticSensor;
    private final Sensor accelerometer;
    private final MeanRingBufferAbstract<Float> radiantRingBuffer;
    private final int maxNumberOfListenings;
    private int numberOfListenings;

    public NorthSensorListener(SensorManager sensorManager,
                               MeanRingBufferAbstract<Float> ringBufferForRad,
                               NorthAngleCalculator northAngleCalculator,
                               int maxNumberOfListenings){
        this.radiantRingBuffer = ringBufferForRad;
        this.sensorManager = sensorManager;
        this.northAngleCalculator = northAngleCalculator;
        this.maxNumberOfListenings = maxNumberOfListenings;
        this.magneticSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void startTrackingNorth(){
        if(this.isContinuingTrackingAllowed()) {
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
            this.northAngleCalculator.setMagnetometerData(event.values);
        }else if(event.sensor == accelerometer){
            this.northAngleCalculator.setAcceleratorData(event.values);
        }
        if(this.northAngleCalculator.isAngleWithCurrentDataCalculable()){
            if (!this.isContinuingTrackingAllowed()) {
                this.stopTrackingNorth();
            }
            this.radiantRingBuffer.addObjectForMeanCalculation(this.northAngleCalculator.calculateNorthAngleBasedOnData()*-1);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float getRadiant(){
        return this.radiantRingBuffer.getNewMean();
    }

    private boolean isContinuingTrackingAllowed() {
        return this.numberOfListenings < this.maxNumberOfListenings;
    }
}
