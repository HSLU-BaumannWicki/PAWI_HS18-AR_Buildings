package eu.kudan.ar.ar.position;

import android.location.Location;

import com.jme3.math.Vector3f;

import commonlib.location.LocationDistanceCalculator;
import commonlib.model.BuildingModel;
import eu.kudan.ar.ar.position.location.LocationFilter;
import eu.kudan.ar.ar.position.location.LocationFilterListener;
import eu.kudan.ar.ar.position.location.ModelToRelativePositionRotator;
import eu.kudan.ar.ar.position.location.PhysicalNorthRotatorProblem;

public class GPSBuildingPositioner implements BuildingPositioner, LocationFilterListener {
    private final LocationFilter locationFilter;
    private final PhysicalNorthRotatorProblem physicalNorthRotatorProblem;
    private final ModelToRelativePositionRotator modelToRelativePositionRotator;
    private final LocationDistanceCalculator locationDistanceCalculator;
    public final BuildingModel buildingModel;

    public GPSBuildingPositioner(LocationFilter locationFilter, PhysicalNorthRotatorProblem physicalNorthRotatorProblem, ModelToRelativePositionRotator modelToRelativePositionRotator, LocationDistanceCalculator locationDistanceCalculator, BuildingModel buildingModel){
        this.locationFilter = locationFilter;
        this.physicalNorthRotatorProblem = physicalNorthRotatorProblem;
        this.modelToRelativePositionRotator = modelToRelativePositionRotator;
        this.locationDistanceCalculator = locationDistanceCalculator;
        this.buildingModel = buildingModel;
    }

    @Override
    public void onNewLocationUpdate(Location location) {

        Vector3f physicalPlaceForBuilding = locationDistanceCalculator.getDistancesBetween(location, this.buildingModel.getLocation());
        //this.modelToRelativePositionRotator.rotateObjectForRelativePosition(physicalPlaceForBuilding);
        Vector3f correctedBuildingPosition = this.physicalNorthRotatorProblem.getPhysicalNorthCorrectedVector(physicalPlaceForBuilding);
        this.buildingModel.setModelPosition(correctedBuildingPosition);
        this.buildingModel.unhide();
    }

    @Override
    public void startPositioning() {
        this.locationFilter.registerLocationFilterListener(this);
        this.locationFilter.startLocationFilter();
        this.physicalNorthRotatorProblem.startNothSensor();
    }

    @Override
    public void stopPositioning() {
        this.locationFilter.unregisterLocationFilterListener(this);
        this.locationFilter.stopLocationFilter();
        this.physicalNorthRotatorProblem.stopNorthSensor();
        this.buildingModel.hide();
    }
}
