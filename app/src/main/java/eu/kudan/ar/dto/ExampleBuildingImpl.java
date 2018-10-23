package eu.kudan.ar.dto;

import android.location.Location;

import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;

public class ExampleBuildingImpl implements Building {
    private ARModelNode myModel;
    private Location location;
    public static final double ALTITUDE = -1.619869351387024;
    public static final double LATITUDE = 47.143457499999999;
    public static final double LONGITUDE = 8.433371093750011;

    public ExampleBuildingImpl(){
        location = new Location("");
        location.setAccuracy(0);
        location.setAltitude(ALTITUDE);
        location.setLongitude(LONGITUDE);
        location.setLatitude(LATITUDE);


        ARModelImporter importer = new ARModelImporter();
        importer.loadFromAsset("ARBuilding.armodel");
        myModel = importer.getNode();



        ARLightMaterial concreteMaterial = new ARLightMaterial();
        concreteMaterial.setAmbient(1f, 1f, 1f);

        for(ARMeshNode meshNode : myModel.getMeshNodes()) {
            meshNode.setMaterial(concreteMaterial);
        }
    }

    @Override
    public ARModelNode getKudanModelNode() {
        return myModel;
    }

    @Override
    public Location getBuildingLocation() {
        return location;
    }
}
