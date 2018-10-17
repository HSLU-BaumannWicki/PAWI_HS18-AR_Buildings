package eu.kudan.ar;

import android.util.Log;

import eu.kudan.kudan.ARImageTrackable;
import eu.kudan.kudan.ARImageTrackableListener;

public class QRMarkerListener implements ARImageTrackableListener {
    private String markerName;
    private boolean alreadyTracked = false;

    public QRMarkerListener(String markerName) {
        if (markerName != null) {
            this.markerName = markerName;
        }
        else {
            this.markerName = "undefined";
        }
    }

    @Override
    public void didDetect(ARImageTrackable arImageTrackable) {
        Log.i("AR", "Found image trackable on marker: " + markerName);
    }

    @Override
    public void didTrack(ARImageTrackable arImageTrackable) {
        if (!alreadyTracked) {
            Log.i("AR", "Tracked image trackable on marker: " + markerName);
            alreadyTracked = true;
        }
    }

    @Override
    public void didLose(ARImageTrackable arImageTrackable) {
        Log.i("AR", "Lost image trackable on marker: " + markerName);
        alreadyTracked = false;
    }
}
