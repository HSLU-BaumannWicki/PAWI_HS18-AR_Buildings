package eu.kudan.ar.ar.position.location;

import android.location.Location;

public interface LocationFilterListener {
    void onNewLocationUpdate(Location location);
}
