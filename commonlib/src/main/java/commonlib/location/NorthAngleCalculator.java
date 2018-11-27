package commonlib.location;

import android.hardware.SensorManager;

import java.util.Arrays;

public class NorthAngleCalculator {
    private final static int MAGNET_SENSOR_ID = 0b1;
    private final static int ACCELERATOR_SENSOR_ID = 0b10;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private int state;

    public float calculateNorthAngleBasedOnData(){
        System.out.println("STATE:    " + this.state);
        System.out.print("lastAccel   ");
        Arrays.asList(this.lastAccelerometer).forEach(System.out::print);
        System.out.println("");
        System.out.println("lastMag" + this.lastMagnetometer);
        Arrays.asList(this.lastMagnetometer).forEach(System.out::print);
        this.state &= ~(MAGNET_SENSOR_ID | ACCELERATOR_SENSOR_ID);
        float[] mR = new float[9];
        float[] orientationAngles = new float[3];
        SensorManager.getRotationMatrix(mR, null, lastAccelerometer, lastMagnetometer);
        SensorManager.getOrientation(mR, orientationAngles);
        return orientationAngles[0];
    }

    public void setMagnetometerData(float[] magnetometerData){
        this.state |= MAGNET_SENSOR_ID;
        System.arraycopy(magnetometerData, 0, lastAccelerometer, 0, magnetometerData.length);
    }

    public void setAcceleratorData(float[] acceleratorData){
        this.state |= ACCELERATOR_SENSOR_ID;
        System.arraycopy(acceleratorData, 0, this.lastAccelerometer, 0, acceleratorData.length);
    }

    public boolean isAngleWithCurrentDataCalculable(){
        return this.state == (ACCELERATOR_SENSOR_ID | MAGNET_SENSOR_ID);
    }

}
