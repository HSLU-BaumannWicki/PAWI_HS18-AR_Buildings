package eu.kudan.ar.ar.position.location;

import com.jme3.math.Vector3f;

import commonlib.location.AngleToNorthCalculator;
import commonlib.rotation.Rotator;

public class ModelToRelativePositionRotator {
    private final Rotator modelRotator;
    private final AngleToNorthCalculator angleToNorthCalculator;
    private float deltaAngle=0;
    private float rotatedAngle=0;

    public ModelToRelativePositionRotator(Rotator modelRotator, AngleToNorthCalculator angleToNorthCalculator){
        this.modelRotator = modelRotator;
        this.angleToNorthCalculator = angleToNorthCalculator;
    }

    public void rotateObjectForRelativePosition(Vector3f vector){
        float angle = this.angleToNorthCalculator.calculatePlaneAngleBetweenVectorAndNorth(vector);
        float deltaAngle = angle - this.rotatedAngle;
        if (-0.2 < deltaAngle && deltaAngle < 0.2 ){
            this.deltaAngle += deltaAngle;
        }else{
            this.modelRotator.rotateByRadOverY(this.deltaAngle);
            this.deltaAngle = 0;
        }
        this.rotatedAngle = angle;
    }
}
