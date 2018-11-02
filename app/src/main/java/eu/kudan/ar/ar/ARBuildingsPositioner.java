package eu.kudan.ar.ar;

import java.util.LinkedList;
import java.util.List;

import eu.kudan.ar.ar.position.BuildingPositioner;

public class ARBuildingsPositioner {
    private final List<BuildingPositioner> buildingPositioners;

    private ARBuildingsPositioner() {
        this.buildingPositioners = new LinkedList<>();
    }

    public ARBuildingsPositioner(BuildingPositioner buildingPositioner) {
        this();
        this.buildingPositioners.add(buildingPositioner);
    }

    public ARBuildingsPositioner(List<BuildingPositioner> buildingPositioners){
        this();
        this.buildingPositioners.addAll(buildingPositioners);
    }

    public void startPositioning(){
        this.buildingPositioners.forEach(position -> position.startPositioning());
    }

    public void stopPositioning(){
        this.buildingPositioners.forEach(position -> position.stopPositioning());
    }

    public void rotateAllByDeg(float degrees){
        this.buildingPositioners.forEach(position -> position.rotateBuildingByDegrees(degrees));
    }
}
