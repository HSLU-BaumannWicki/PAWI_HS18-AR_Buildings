package eu.kudan.ar.location;

import android.location.Location;
import android.location.LocationListener;

public interface LocationStorageListener extends LocationListener {
    public Location getLastLocation();
}
