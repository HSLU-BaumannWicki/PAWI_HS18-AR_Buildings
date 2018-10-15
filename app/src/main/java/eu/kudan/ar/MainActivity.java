package eu.kudan.ar;
import android.os.Bundle;
import android.util.Log;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARArbiTrack;
import eu.kudan.kudan.ARArbiTrackListener;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;

public class MainActivity extends ARActivity implements ARArbiTrackListener, ARImageTrackableListener {

    // Check if this run is the first ArbiTrackStarted run
    private boolean firstRun = false;
    private ARModelNode arBuilding;
    private ARImageTrackable qrMarker;

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
    public void setup()
    {
        super.setup();

        // Initialise image trackable
        qrMarker = new ARImageTrackable("QR Marker");
        qrMarker.loadFromAsset("QRMarker.png");
        qrMarker.addListener(new QRMarkerListener("QR Marker"));
        qrMarker.addListener(this);
        qrMarker.setName("QRMarker_Building");

        ARModelImporter arModelImporter = new ARModelImporter();

        // Load the building model
        arModelImporter.loadFromAsset("ARBuilding.armodel");
        arBuilding = arModelImporter.getNode();
        arBuilding.setName("AR_Building_Model");

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

        ARImageTracker imageTracker = ARImageTracker.getInstance();
        imageTracker.initialise();

        imageTracker.addTrackable(qrMarker);

        qrMarker.getWorld().addChild(arBuilding);

        // Scale and rotate the model
        arBuilding.scaleByUniform(8f);
        arBuilding.rotateByDegrees(90f, 1,0,0);

        // Initialise ArbiTrack
        ARArbiTrack arbiTrack = ARArbiTrack.getInstance();
        arbiTrack.initialise();

        //Add the activity as an ArbiTrack delegate
        arbiTrack.addListener(this);

        // Use the image trackable's world as the target node.
        // This causes ArbiTrack to start tracking at the trackable's position.
        arbiTrack.setTargetNode(qrMarker.getWorld());
    }

    @Override
    public void arbiTrackStarted() {
        Log.d("AR", "ArbiTrack started");
                if(firstRun) {
                    Log.i("AR", "Entered firstRun Setup");

                    ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();

                    //Initialise an ARImageNode, find marker node,  and attach to ARArbitrack
                    ARModelNode localBuilding = arBuilding;
                    arArbiTrack.getWorld().addChild(localBuilding);

                    localBuilding.remove();

                    // Add the image node as a child of the trackable's world
                    qrMarker.getWorld().addChild(localBuilding);
                    firstRun = false;
                }

                if(qrMarker.getDetected()) {

                    // Get arbi track instance
                    ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();

                    //Get the markerless space to trackable space
                    Matrix4f transform = arArbiTrack.getWorld().getFullTransform().invert().mult(qrMarker.getWorld().getFullTransform());

                    //Get the rotation out of the full transform
                    Quaternion orInArbiTrack = new Quaternion().fromRotationMatrix(transform.toRotationMatrix());

                    //Get the position out of the full transform
                    Vector3f posInArbiTrack = transform.mult(new Vector3f());

                    // Add the image node as a child of arbi track
                    arArbiTrack.getWorld().addChild(arBuilding);

                    // Change image nodes position to be relative to the marker nodes world
                    arBuilding.setPosition(posInArbiTrack);

                    // Change image nodes orientation to be relative to the marker nodes world
                    arBuilding.setOrientation(orInArbiTrack);
                }

            }

    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        ARArbiTrack arArbiTrack = ARArbiTrack.getInstance();
        if (arArbiTrack.getIsInitialised()) {
            Log.i("AR", "Changed to ArbiTrack");
            firstRun = true;
            arArbiTrack.start();
        }
    }

    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {

    }

    @Override
    public void didLose(ARImageTrackable arImageTrackable) {

    }
}
