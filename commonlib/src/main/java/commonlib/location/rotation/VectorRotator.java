package commonlib.location.rotation;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class VectorRotator implements Rotator<Vector3f> {
    private Vector3f vector;

    public VectorRotator(Vector3f vector){
        this.vector = vector;
    }
    
    @Override
    public Vector3f rotateByRadOverY(float rad) {
        Vector2f rotatorVector = new Vector2f(this.vector.getX(), this.vector.getZ());
        final float cos = (float) Math.cos(rad);
        final float sin = (float) Math.sin(rad);
        final float minusSin = sin * -1;
        final Matrix3f rotationMatrixY = new Matrix3f(cos, 0, sin, 0, 1, 0, minusSin, 0, cos);
        this.vector = rotationMatrixY.mult(this.vector);
        return this.vector;
    }

    @Override
    public Vector3f rotateByDegOverY(float deg) {
        return this.rotateByRadOverY((float) Math.toRadians(deg));
    }

    @Override
    public Vector3f rotateByRadOverX(float rad) {
        final float cos = (float) Math.cos(rad);
        final float sin = (float) Math.sin(rad);
        final Matrix3f rotationMatrixY = new Matrix3f(1, 0, 0, 0, cos, -sin, 0, sin, cos);
        this.vector = rotationMatrixY.mult(this.vector);
        return this.vector;
    }

    @Override
    public Vector3f rotateByRadOverZ(float rad) {
        final float cos = (float) Math.cos(rad);
        final float sin = (float) Math.sin(rad);
        final Matrix3f rotationMatrixY = new Matrix3f(cos, -sin, 0, sin, cos, 0, 0, 0, 1);
        this.vector = rotationMatrixY.mult(this.vector);
        return this.vector;
    }

    @Override
    public Vector3f getRotatingObject() {
        return vector;
    }

    @Override
    public void setRotatingObject(Vector3f object) {
        this.vector = object;
    }
}
