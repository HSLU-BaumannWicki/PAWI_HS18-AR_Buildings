package eu.kudan.ar.di;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;

import com.jme3.math.Vector3f;

import commonlib.location.AngleToNorthCalculator;
import commonlib.location.LocationDistanceCalculator;
import commonlib.model.BuildingModelSampleImpl;
import commonlib.model.texture.Texturizer;
import commonlib.model.texture.TexturizerModelBlack;
import commonlib.rotation.ModelRotator;
import commonlib.rotation.Rotator;
import commonlib.rotation.VectorRotator;
import commonlib.storage.FloatMeanRingBuffer;
import commonlib.storage.LocationMeanRingbufferImp;
import commonlib.storage.RingBufferImpl;
import eu.kudan.ar.ar.position.GPSBuildingPositioner;
import eu.kudan.ar.ar.position.location.LocationFilter;
import eu.kudan.ar.ar.position.location.ModelNorth;
import eu.kudan.ar.ar.position.location.ModelToRelativePositionRotator;
import eu.kudan.ar.ar.position.location.NorthSensorListener;
import eu.kudan.ar.ar.position.location.PhysicalNorthRotatorProblem;
import eu.kudan.kudan.ARGyroManager;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;

public class ProjectInitializer {
    public static GPSBuildingPositioner initGPSSolution(Context context){
        ARGyroManager gyroManager = ARGyroManager.getInstance();
        gyroManager.initialise();
        ARModelImporter importer = new ARModelImporter();
        importer.loadFromAsset("ARBuilding.armodel");
        ARModelNode model = importer.getNode();
        gyroManager.getWorld().addChild(model);


        model.rotateByRadians((float) Math.toRadians(-100), 0,1,0);


        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);


        AngleToNorthCalculator angleCalculator = new AngleToNorthCalculator();
        Texturizer blackTexture = new TexturizerModelBlack();
        BuildingModelSampleImpl buildingModel = new BuildingModelSampleImpl(model, blackTexture);
        ModelRotator modelRotator = new ModelRotator(model);
        LocationDistanceCalculator locationDistanceCalculator = new LocationDistanceCalculator();
        RingBufferImpl<Location> locationMean = new LocationMeanRingbufferImp(5);
        LocationFilter locationFilter = new LocationFilter(locationMean, locationManager);
        RingBufferImpl<Float> angleRingBuffer = new FloatMeanRingBuffer(10);
        NorthSensorListener northSensorListener = new NorthSensorListener(sensorManager, angleRingBuffer);
        Rotator<Vector3f> vector3fRotator = new VectorRotator(new Vector3f());

        ModelNorth modelNorth = new ModelNorth(gyroManager.getWorld(), angleCalculator);
        PhysicalNorthRotatorProblem physicalNorthRotatorProblem = new PhysicalNorthRotatorProblem(modelNorth, northSensorListener, vector3fRotator);
        ModelToRelativePositionRotator modelToRelativePositionRotator = new ModelToRelativePositionRotator(modelRotator, angleCalculator);

        return new GPSBuildingPositioner(locationFilter, physicalNorthRotatorProblem, modelToRelativePositionRotator, locationDistanceCalculator, buildingModel);

    }
}
