package commonlib.location;

import android.location.Location;

public interface LocationFilterListener {
    void onNewLocationUpdate(Location location);
}
