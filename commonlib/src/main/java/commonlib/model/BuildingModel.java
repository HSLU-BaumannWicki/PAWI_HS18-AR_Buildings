package commonlib.model;

import android.location.Location;

import com.jme3.math.Vector3f;

public interface BuildingModel {
    void rotateModelByRad(float radiant, float x, float y, float z);
    void rotateModelByRadOverY(float radiant);
    void rotateModelByDegOverY(float Deg);
    double getAltitude();
    double getLongitude();
    double getLatitude();
    Location getLocation();
    void setModelPosition(Vector3f position);
    void hide();
    void unhide();
}
