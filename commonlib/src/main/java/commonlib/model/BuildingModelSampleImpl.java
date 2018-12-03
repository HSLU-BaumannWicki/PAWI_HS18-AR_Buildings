package commonlib.model;

import android.location.Location;

import com.jme3.math.Vector3f;

import commonlib.model.texture.Texturizer;
import eu.kudan.kudan.ARModelNode;

public class BuildingModelSampleImpl implements BuildingModel {
    private final static double ALTITUDE = 428;
    private final static double LATITUDE = 47.14332;
    private final static double LONGITUDE = 8.4329;
    private Location locationOfTheBuilding;
    private ARModelNode myModel;

    public BuildingModelSampleImpl(ARModelNode model, Texturizer texturizer){
        this.myModel = model;
        texturizer.setTexture(this.myModel);
        this.locationOfTheBuilding = new Location("");
        this.locationOfTheBuilding.setAltitude(ALTITUDE);
        this.locationOfTheBuilding.setLongitude(LONGITUDE);
        this.locationOfTheBuilding.setLatitude(LATITUDE);
        myModel.rotateByDegrees((450), 0,1,0 );
        this.hide();
    }


    @Override
    public void rotateModelByRad(float radiant, float x, float y, float z) {
        this.myModel.rotateByRadians(radiant, x, y, z);
    }

    @Override
    public void rotateModelByRadOverY(float radiant) {
        this.myModel.rotateByRadians(radiant, 0, 1, 0);
    }

    @Override
    public void rotateModelByDegOverY(float Deg) {
        this.myModel.rotateByDegrees(Deg, 0,1,0);
    }

    @Override
    public double getAltitude() {
        return ALTITUDE;
    }

    @Override
    public double getLongitude() {
        return LONGITUDE;
    }

    @Override
    public double getLatitude() {
        return LATITUDE;
    }

    @Override
    public Location getLocation() {
        if (this.locationOfTheBuilding == null){
            this.locationOfTheBuilding = new Location("");
            this.locationOfTheBuilding.setAltitude(ALTITUDE);
            this.locationOfTheBuilding.setLongitude(LONGITUDE);
            this.locationOfTheBuilding.setLatitude(LATITUDE);
        }
        return this.locationOfTheBuilding;

    }

    @Override
    public void setModelPosition(Vector3f position) {
        this.myModel.setPosition(position);
    }

    @Override
    public void hide() {
        this.myModel.setVisible(false);
    }

    @Override
    public void unhide() {
        this.myModel.setVisible(true);
    }
}
