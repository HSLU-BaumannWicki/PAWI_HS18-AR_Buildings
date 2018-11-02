package commonlib.rotation;

import eu.kudan.kudan.ARModelNode;

public class ModelRotator implements Rotator<ARModelNode> {
    private ARModelNode model;

    public ModelRotator(ARModelNode rotatingModel) {
        this.model = rotatingModel;
    }

    @Override
    public ARModelNode rotateByRadOverY(float rad) {
        this.model.rotateByRadians(rad, 0,1,0);
        return this.model;
    }

    @Override
    public ARModelNode rotateByDegOverY(float deg) {
        this.model.rotateByDegrees(deg, 0,1,0);
        return this.model;
    }

    @Override
    public ARModelNode rotateByRadOverX(float rad) {
        this.model.rotateByRadians(rad, 1,0,0);
        return this.model;
    }

    @Override
    public ARModelNode rotateByRadOverZ(float rad) {
        this.model.rotateByRadians(rad, 0,0,1);
        return this.model;
    }

    @Override
    public ARModelNode getRotatingObject() {
        return this.model;
    }

    @Override
    public void setRotatingObject(ARModelNode object) {
        this.model = object;
    }
}
