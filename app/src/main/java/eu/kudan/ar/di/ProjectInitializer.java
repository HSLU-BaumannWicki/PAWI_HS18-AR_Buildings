package eu.kudan.ar.di;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.jme3.math.Vector3f;

import commonlib.GPSBuildingPositioner;
import commonlib.location.LocationDistanceCalculator;
import commonlib.location.LocationFilter;
import commonlib.location.NorthSensorListener;
import commonlib.location.PhysicalNorthInitializer;
import commonlib.location.rotation.Rotator;
import commonlib.location.rotation.VectorRotator;
import commonlib.model.BuildingModelSampleImpl;
import commonlib.model.texture.Texturizer;
import commonlib.model.texture.TexturizerModelConcreteGlass;
import commonlib.storage.FloatMeanRingBuffer;
import commonlib.storage.LocationMeanRingbufferImp;
import commonlib.storage.MeanRingBufferAbstract;
import eu.kudan.ar.ar.ARBuildingsPositioner;
import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARGyroManager;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;

public class ProjectInitializer {

    @NonNull
    public static ARBuildingsPositioner initGPSSingleBuildingSolution(Context context){
        KudanDevAPIKey();

        ARGyroManager gyroManager = ARGyroManager.getInstance();
        gyroManager.initialise();
        ARModelImporter importer = new ARModelImporter();
        importer.loadFromAsset("ARBuilding.armodel");
        ARModelNode model = importer.getNode();
        gyroManager.getWorld().addChild(model);


        // model.rotateByRadians((float) Math.toRadians(-100), 0,1,0);


        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Texturizer variableTexture = new TexturizerModelConcreteGlass();
        BuildingModelSampleImpl buildingModel = new BuildingModelSampleImpl(model, variableTexture);
        LocationDistanceCalculator locationDistanceCalculator = new LocationDistanceCalculator();
        MeanRingBufferAbstract<Location> locationMean = new LocationMeanRingbufferImp(10);
        LocationFilter locationFilter = new LocationFilter(locationMean, locationManager);
        MeanRingBufferAbstract<Float> angleRingBuffer = new FloatMeanRingBuffer(100);
        NorthSensorListener northSensorListener = new NorthSensorListener(sensorManager, angleRingBuffer, 100);
        Rotator<Vector3f> vector3fRotator = new VectorRotator(new Vector3f());

        PhysicalNorthInitializer physicalNorthInitializer = new PhysicalNorthInitializer(northSensorListener, vector3fRotator);

        GPSBuildingPositioner gpsPositioner = new GPSBuildingPositioner(locationFilter, physicalNorthInitializer, locationDistanceCalculator, buildingModel);
        return new ARBuildingsPositioner(gpsPositioner);
    }

    private static void KudanDevAPIKey(){
        ARAPIKey key = ARAPIKey.getInstance();
        // TODO: Using the development key, change in the future?
        key.setAPIKey("agWZcpYLYjBxCbWf2qZx6k+PWISqeGtFCqKaZwYtwS+kdn1HKiQAmsJ55STRBe9BqCw3VwG6qL+ESI5ntTF/iV/uekLG3PCokaUE0/uTzqhaYlxRdmuNBIduzBCjq3mV2na+gy3ffHH9Ipc7eIN0geTj3p+ppsmK0U399iGmN38ndIh6k2y16cByWIecMSU3yw3Ztw7gHRqf83hVhZ5T2ACGK4SNkQhhdKp+CTaR5W3amYCJBgwumqFqNFyI9UniuMk70T/cQObRQum2U51OjjbMfmEAwIBt8Q8jD2yACzye6K4/1O4pZhbGEbiDeLrAfxqMwBAe5o6vnYIilGNnpDhfi3wOHhRaqtLOVvB58GUIFTnAPvmYFVnLWRJmCUZ9FJNDyX3ALCl/alFEWh+A/a6NFjcwLGKI9drPuGG4ONFg4p0l+p3b9DZoLzszlmWAflI/UFzQa++kQn3/sclO9i0vPnpi0LWoABm5vGswLVAIX/0k6384GXxfkADI6fjGtf62XJ5ImaVDiiREa9mabWEQGoifghQG1sGNDYgBIYEpiaLsVzOfTALpe20Q7kFCMjedJImQhhuLtEK1BXfXJEed1QqUOsG9IeKxKk28GbOtOF9w3yrSF3gnJslzZxF2kEF3C6ckog8byagS+4p37FJmbpPsiKNH1Qm0LuouGcQ=");
    }
}
