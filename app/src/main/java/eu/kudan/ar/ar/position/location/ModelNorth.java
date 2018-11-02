package eu.kudan.ar.ar.position.location;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import commonlib.location.AngleToNorthCalculator;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARWorld;

public class ModelNorth {
    private final AngleToNorthCalculator angleToNorthCalc;
    private ARNode myModel;
    private final ARWorld gyroWorld;

    public ModelNorth(ARWorld gyroWorld, AngleToNorthCalculator northernAngleCalculator){
        this.gyroWorld = gyroWorld;
        this.myModel = new ARNode();
        this.myModel.setPosition(1,0,0);
        this.myModel.setVisible(true);
        gyroWorld.addChild(this.myModel);
        this.angleToNorthCalc = northernAngleCalculator;
    }

    public float getPositionToNorthInRad(){
        Vector3f modelVector = this.myModel.getFullPosition();
        Vector2f northernVector = new Vector2f(0,1);
        Vector2f angleToNorthVector = new Vector2f(modelVector.getY(), modelVector.getZ());
        return northernVector.angleBetween(angleToNorthVector);
    }
}
