package eu.kudan.ar.ar.position.location;

import com.jme3.math.Vector3f;

import commonlib.rotation.Rotator;

public class PhysicalNorthInitializer {
    private final NorthSensorListener northSensorListener;
    private final Rotator<Vector3f> vectorRotator;

    public PhysicalNorthInitializer(NorthSensorListener northSensorListener, Rotator<Vector3f> vectorRotator){
        this.northSensorListener = northSensorListener;
        this.vectorRotator = vectorRotator;
    }

    public Vector3f getPhysicalNorthCorrectedVector(Vector3f gpsVectorToObject){
        this.vectorRotator.setRotatingObject(new Vector3f(gpsVectorToObject));
        final float rad = this.northSensorListener.getRadiant();
        return this.vectorRotator.rotateByRadOverY(-rad);
    }

    public void stopNorthSensor(){
        this.northSensorListener.stopTrackingNorth();
    }

    public void startNothSensor(){
        this.northSensorListener.startTrackingNorth();
    }
}
