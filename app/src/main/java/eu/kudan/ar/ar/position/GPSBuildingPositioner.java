package eu.kudan.ar.ar.position;

import android.location.Location;

import com.jme3.math.Vector3f;

import commonlib.location.LocationDistanceCalculator;
import commonlib.model.BuildingModel;
import eu.kudan.ar.ar.position.location.LocationFilter;
import eu.kudan.ar.ar.position.location.LocationFilterListener;
import eu.kudan.ar.ar.position.location.PhysicalNorthInitializer;

public class GPSBuildingPositioner implements BuildingPositioner, LocationFilterListener {
    private final LocationFilter locationFilter;
    private final PhysicalNorthInitializer physicalNorthInitializer;
    private final LocationDistanceCalculator locationDistanceCalculator;
    private final BuildingModel buildingModel;

    public GPSBuildingPositioner(LocationFilter locationFilter, PhysicalNorthInitializer physicalNorthInitializer, LocationDistanceCalculator locationDistanceCalculator, BuildingModel buildingModel){
        this.locationFilter = locationFilter;
        this.physicalNorthInitializer = physicalNorthInitializer;
        this.locationDistanceCalculator = locationDistanceCalculator;
        this.buildingModel = buildingModel;
    }

    @Override
    public void onNewLocationUpdate(Location location) {
        Vector3f physicalPlaceForBuilding = locationDistanceCalculator.getDistancesBetween(location, this.buildingModel.getLocation());
        Vector3f correctedBuildingPosition = this.physicalNorthInitializer.getPhysicalNorthCorrectedVector(physicalPlaceForBuilding);
        this.buildingModel.setModelPosition(correctedBuildingPosition);
        this.buildingModel.unhide();
    }

    @Override
    public void startPositioning() {
        this.locationFilter.registerLocationFilterListener(this);
        this.locationFilter.startLocationFilter();
        this.physicalNorthInitializer.startNothSensor();
    }

    @Override
    public void stopPositioning() {
        this.locationFilter.unregisterLocationFilterListener(this);
        this.locationFilter.stopLocationFilter();
        this.physicalNorthInitializer.stopNorthSensor();
        this.buildingModel.hide();
    }

    @Override
    public void rotateBuildingByDegrees(float degrees) {
        this.buildingModel.rotateModelByDegOverY(degrees);
    }
}
