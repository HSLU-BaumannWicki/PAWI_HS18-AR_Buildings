package eu.kudan.ar;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARArbiTrackListener;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARTexture2D;

public class MainActivity extends ARActivity implements ARArbiTrackListener, GestureDetector.OnGestureListener {
    private ARModelNode arBuilding;
    private ARImageTrackable qrMarker;
    private GestureDetectorCompat gestureDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comment this out for the time being unless you plan to create UI elements
        //setContentView(R.layout.activity_main);

        ARAPIKey key = ARAPIKey.getInstance();
        // TODO: Using the development key, change in the future?
        key.setAPIKey("agWZcpYLYjBxCbWf2qZx6k+PWISqeGtFCqKaZwYtwS+kdn1HKiQAmsJ55STRBe9BqCw3VwG6qL+ESI5ntTF/iV/uekLG3PCokaUE0/uTzqhaYlxRdmuNBIduzBCjq3mV2na+gy3ffHH9Ipc7eIN0geTj3p+ppsmK0U399iGmN38ndIh6k2y16cByWIecMSU3yw3Ztw7gHRqf83hVhZ5T2ACGK4SNkQhhdKp+CTaR5W3amYCJBgwumqFqNFyI9UniuMk70T/cQObRQum2U51OjjbMfmEAwIBt8Q8jD2yACzye6K4/1O4pZhbGEbiDeLrAfxqMwBAe5o6vnYIilGNnpDhfi3wOHhRaqtLOVvB58GUIFTnAPvmYFVnLWRJmCUZ9FJNDyX3ALCl/alFEWh+A/a6NFjcwLGKI9drPuGG4ONFg4p0l+p3b9DZoLzszlmWAflI/UFzQa++kQn3/sclO9i0vPnpi0LWoABm5vGswLVAIX/0k6384GXxfkADI6fjGtf62XJ5ImaVDiiREa9mabWEQGoifghQG1sGNDYgBIYEpiaLsVzOfTALpe20Q7kFCMjedJImQhhuLtEK1BXfXJEed1QqUOsG9IeKxKk28GbOtOF9w3yrSF3gnJslzZxF2kEF3C6ckog8byagS+4p37FJmbpPsiKNH1Qm0LuouGcQ=");
    }

    @Override
    public void setup() {
        super.setup();

        // Initialise image trackable
        qrMarker = new ARImageTrackable("QR Marker");
        qrMarker.loadFromAsset("QRMarker.png");
        qrMarker.addListener(new QRMarkerListener("QR Marker"));
        qrMarker.setName("QRMarker_Building");

        arBuilding = createModelNode("ARBuilding.armodel", "AR_Building_Model");

        // Scale and rotate the model
        arBuilding.scaleByUniform(8f);
        arBuilding.rotateByDegrees(90f, 1, 0, 0);

        ARImageTracker imageTracker = ARImageTracker.getInstance();
        imageTracker.initialise();
        imageTracker.addTrackable(qrMarker);

        setUpArbiTrack(arBuilding, arBuilding);

        gestureDetect = new GestureDetectorCompat(this,this);
    }

    private void setUpArbiTrack(ARNode targetNode, ARNode childNode)
    {
        // Get the arbitrack manager and initialise it
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        ARGyroPlaceManager gyroPlaceManager = ARGyroPlaceManager.getInstance();
        gyroPlaceManager.initialise();

        gyroPlaceManager.getWorld().addChild(targetNode);

        arbiTrack.getWorld().setParent(ARImageTracker.getInstance().getBaseNode());
        arbiTrack.setTargetNode(targetNode);

        // Add the tracking image node to the arbitrack world
        arbiTrack.getWorld().addChild(childNode);

        // Add this activity as a listener of arbitrack
        arbiTrack.addListener(this);
    }

    private ARModelNode createModelNode(String assetName, String nodeName) {
        ARModelImporter arModelImporter = new ARModelImporter();
        ARModelNode resultNode;

        // Load the building model
        arModelImporter.loadFromAsset(assetName);
        resultNode = arModelImporter.getNode();
        resultNode.setName(nodeName);

        // Add Texture to the model
        ARTexture2D concreteTexture = new ARTexture2D();
        concreteTexture.loadFromAsset("concrete.jpg");

        // Add ambient lighting
        ARLightMaterial concreteMaterial = new ARLightMaterial();
        concreteMaterial.setTexture(concreteTexture);
        concreteMaterial.setAmbient(0.8f, 0.8f, 0.8f);

        for (ARMeshNode meshNode : arModelImporter.getMeshNodes()) {
            meshNode.setMaterial(concreteMaterial);
        }

        return resultNode;
    }


    @Override
    public void arbiTrackStarted() {
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        ARImageTracker imageTracker = ARImageTracker.getInstance();
        ARGyroPlaceManager arGyroPlaceManager = ARGyroPlaceManager.getInstance();

        Vector3f imageTrackerWorldPosition = imageTracker.getBaseNode().getWorldPosition();
        Quaternion imageTrackerWorldOrientation = imageTracker.getBaseNode().getWorldOrientation();

        Log.i("AR", "imageTracker - WorldPosition: " + imageTrackerWorldPosition.toString());
        Log.i("AR", "imageTracker - WorldOrientation: " + imageTrackerWorldOrientation.toString());

        Quaternion targetOrientation = qrMarker.getWorld().getOrientation();
        Vector3f targetPosition = qrMarker.getWorld().getPosition();

        Log.i("AR", "QR Marker - Position" + targetPosition.toString());
        Log.i("AR", "QR Marker - Orientation" + targetOrientation.toString());

        Log.i("AR", "ArbiTrack - Position" + arbiTrack.getWorld().getPosition().toString());
        Log.i("AR", "ArbiTrack - Orientation" + arbiTrack.getWorld().getOrientation().toString());

        arbiTrack.getWorld().setOrientation(imageTrackerWorldOrientation);
        arbiTrack.getWorld().setPosition(imageTrackerWorldPosition);
        arbiTrack.getTargetNode().

        Log.i("AR", "GyroManager - Position" + arGyroPlaceManager.getWorld().getPosition().toString());
        Log.i("AR", "GyroManager - Orientation" + arGyroPlaceManager.getWorld().getOrientation().toString());

        arGyroPlaceManager.getWorld().setOrientation(imageTrackerWorldOrientation);
        arGyroPlaceManager.getWorld().setPosition(imageTrackerWorldPosition);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        boolean gyroStatus = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size() > 0;
        Log.i("System", "Gyroscope available: " + gyroStatus);

        ARNode trackingNode = arbiTrack.getWorld().getChildren().get(0);
        trackingNode.setOrientation(targetOrientation);
        trackingNode.setPosition(targetPosition);
        trackingNode.setVisible(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        gestureDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        arbiTrack.start();

        return false;
    }

    // We also need to implement the other overrides of the GestureDetector, though we don't need them for this sample.
    @Override
    public boolean onDown(MotionEvent e)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        return false;
    }
}
