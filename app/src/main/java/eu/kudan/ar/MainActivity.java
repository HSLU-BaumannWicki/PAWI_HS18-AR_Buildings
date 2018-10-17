package eu.kudan.ar;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARArbiTrackListener;
import eu.kudan.kudan.ARGyroPlaceManager;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARNode;
import eu.kudan.kudan.ARRenderer;
import eu.kudan.kudan.ARRendererListener;
import eu.kudan.kudan.ARTexture2D;

public class MainActivity extends ARActivity implements ARArbiTrackListener, ARImageTrackableListener, ARRendererListener {
    private ARModelNode arBuilding;
    private ARImageTrackable qrMarker;
    private boolean startArbitrack = false;
    private int counter = 0;

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
        qrMarker.addListener(this);
        qrMarker.setName("QRMarker_Building");

        arBuilding = createModelNode("ARBuilding.armodel", "AR_Building_Model");

        // Scale and rotate the model
        arBuilding.scaleByUniform(8f);
        arBuilding.rotateByDegrees(90f, 1, 0, 0);

        ARImageTracker imageTracker = ARImageTracker.getInstance();
        imageTracker.initialise();
        imageTracker.addTrackable(qrMarker);

        setUpArbiTrack(qrMarker.getWorld(), arBuilding);
        ARRenderer.getInstance().addListener(this);
    }

    private void setUpArbiTrack(ARNode targetNode, ARNode childNode)
    {
        // Get the arbitrack manager and initialise it
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        ARGyroPlaceManager gyroPlaceManager = ARGyroPlaceManager.getInstance();
        gyroPlaceManager.initialise();

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
        if (startArbitrack) {
            // Get ArbiTrack instance
            ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();

            // Get model nodes position relative to camera
            Vector3f fullPos = arArbiTrack.getTargetNode().getFullTransform().mult(Vector3f.ZERO);

            // Create empty target Node
            ARNode targetNode = new ARNode();
            targetNode.setPosition(fullPos);
            arArbiTrack.setTargetNode(targetNode);

            // Get models position relative to ArbiTracks world.
            Vector3f posInArbiTrack = arArbiTrack.getWorld().getFullTransform().invert().mult(fullPos);

            // Get models orientation relative to ArbiTracks world.
            Quaternion orInArbiTrack = arArbiTrack.getWorld().getFullOrientation().inverse().mult((arBuilding.getFullOrientation()));

            // Change model nodes position to be relative to the marker nodes world
            arBuilding.setPosition(posInArbiTrack);

            // Change model nodes orientation to be relative to the marker nodes world
            arBuilding.setOrientation(orInArbiTrack);

            arBuilding.setVisible(true);
        }
    }

    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();

        if (arImageTrackable.getName().equals("QRMarker_Building") && arbiTrack.getIsInitialised()) {
            Log.i("AR", "Found Marker and starting ArbiTrack");
            startArbitrack = true;
            arbiTrack.start();
        }
    }

    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {

    }

    @Override
    public void didLose(ARImageTrackable arImageTrackable) {

    }


    @Override
    public void preRender() {

    }

    @Override
    public void postRender() {
        if (startArbitrack) {
            if (counter >= 20) {
                ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
                Log.i("AR", "ArbiTrack - Position" + arbiTrack.getWorld().getPosition().toString());
                Log.i("AR", "ArbiTrack - Orientation" + arbiTrack.getWorld().getOrientation().toString());

                Log.i("AR", "ARBuilding - Position" + arBuilding.getWorld().getPosition().toString());
                Log.i("AR", "ARBuilding - Orientation" + arBuilding.getWorld().getOrientation().toString());
                counter = 0;
            }
            counter++;
        }
    }

    @Override
    public void rendererDidPause() {

    }

    @Override
    public void rendererDidResume() {

    }
}
