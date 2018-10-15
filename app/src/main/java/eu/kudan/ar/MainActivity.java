package eu.kudan.ar;
import android.os.Bundle;

import eu.kudan.kudan.ARAPIKey;
import eu.kudan.kudan.ARActivity;
import eu.kudan.kudan.ARImageNode;
import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTracker;
import eu.kudan.kudan.ARLightMaterial;
import eu.kudan.kudan.ARMeshNode;
import eu.kudan.kudan.ARModelImporter;
import eu.kudan.kudan.ARModelNode;
import eu.kudan.kudan.ARTexture2D;

public class MainActivity extends ARActivity {

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
        ARImageTrackable qrMarker = new ARImageTrackable("QR Marker");
        qrMarker.loadFromAsset("QRMarker.png");
        qrMarker.addListener(new QRMarkerListener("QR Marker"));

        ARModelImporter arModelImporter = new ARModelImporter();

        // Load the building model
        arModelImporter.loadFromAsset("ARBuilding.armodel");
        ARModelNode arBuilding = arModelImporter.getNode();

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
        arBuilding.scaleByUniform(4f);
        arBuilding.rotateByDegrees(90f, 1,0,0);
    }
}
