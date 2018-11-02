package commonlib.location;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class AngleToNorthCalculator {
    public float calculatePlaneAngleBetweenVectorAndNorth(Vector3f vector){
        Vector2f northernVector = new Vector2f(0,1);
        Vector2f angleToNorthVector = new Vector2f(vector.getX(), vector.getZ());
        return northernVector.angleBetween(angleToNorthVector);
    }
}
