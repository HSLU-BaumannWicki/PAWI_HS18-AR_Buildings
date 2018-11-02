package eu.kudan.ar.ar.position.location;

import com.jme3.math.Vector3f;

import commonlib.rotation.Rotator;

public class PhysicalNorthRotatorProblem {
    private final ModelNorth modelNorth;
    private final NorthSensorListener northSensorListener;
    private final Rotator<Vector3f> vectorRotator;

    public PhysicalNorthRotatorProblem(ModelNorth modelNorth, NorthSensorListener northSensorListener, Rotator<Vector3f> vectorRotator){
        this.modelNorth = modelNorth;
        this.northSensorListener = northSensorListener;
        this.vectorRotator = vectorRotator;
    }

    public Vector3f getPhysicalNorthCorrectedVector(Vector3f gpsVectorToObject){
//        this.vectorRotator.setRotatingObject(new Vector3f(gpsVectorToObject));
//        float modelNorthRand = this.modelNorth.getPositionToNorthInRad();
//        float correctionAngle = modelNorthRand - this.northSensorListener.getRadiant();
//        Log.e("wayne", correctionAngle+"");
//        return this.vectorRotator.rotateByRadOverY(correctionAngle);
//
        this.vectorRotator.setRotatingObject(gpsVectorToObject);
        float physicalNorthRad = this.northSensorListener.getRadiant();
        float modelNorthRad = this.modelNorth.getPositionToNorthInRad();
        float correctionAngle = modelNorthRad - physicalNorthRad;
        return this.vectorRotator.rotateByRadOverY(correctionAngle);
    }

    public void stopNorthSensor(){
        this.northSensorListener.stopTrackingNorth();
    }

    public void startNothSensor(){
        this.northSensorListener.startTrackingNorth();
    }
}
